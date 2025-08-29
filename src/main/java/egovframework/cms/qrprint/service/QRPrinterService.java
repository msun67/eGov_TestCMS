package egovframework.cms.qrprint.service;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QRPrinterService {

    private static final String PRINTER_NAME = "KPOS_58 Printer"; // 제어판 프린터 이름
    private static final int MAX_WIDTH_DOTS = 384; // 58mm 헤드 해상도(대부분 384 dots)

    /** 상단 로고(이미지) + 가운데 QR + 오른쪽 하단 시간 출력 */
    public void printTicket(String qrData, String logoPath) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // ESC @ (프린터 초기화)
        out.write(new byte[]{0x1B, 0x40});

        // ── 1) 로고 (가운데 정렬)
        if (logoPath != null) {
            BufferedImage logo = ImageIO.read(new File(logoPath));
            out.write(new byte[]{0x1B, 0x61, 0x01}); // ESC a 1 -> Center
            appendRasterImage(out, logo, MAX_WIDTH_DOTS);
            out.write('\n');
        }

        // ── 2) QR (가운데 정렬)
        out.write(new byte[]{0x1B, 0x61, 0x01});              // Center
        appendQr(out, qrData, /*module*/6, /*EC level M*/0x31);
        out.write("\n\n".getBytes("US-ASCII"));

        // ── 3) 시간 (오른쪽 정렬 + 작은 폰트)
        out.write(new byte[]{0x1B, 0x61, 0x02});              // ESC a 2 -> Right
        out.write(new byte[]{0x1B, 0x4D, 0x01});              // ESC M 1 -> Font B(작은 글씨)

        String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // 한국어 필요시 UTF-8로 변경
        out.write(("Printed " + ts).getBytes("US-ASCII"));

        out.write("\n\n\n\n\n".getBytes("US-ASCII"));             // 여유 줄
        out.write(new byte[]{0x1D, 0x56, 0x00});              	 // GS V 0 -> 컷팅

        sendToPrinter(out.toByteArray());
    }

    /** ESC/POS QR 출력 */
    private void appendQr(ByteArrayOutputStream baos, String data, int module, int ecLevelCode) throws Exception {
        // 모듈 크기 (1~16)
        baos.write(new byte[]{0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, (byte) module});
        // 오류보정 레벨: 48(L) 49(M) 50(Q) 51(H)
        baos.write(new byte[]{0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x45, 0x31}); // M

        byte[] bytes = data.getBytes("UTF-8");
        int len = bytes.length + 3;
        byte pL = (byte) (len & 0xFF);
        byte pH = (byte) ((len >> 8) & 0xFF);

        // 데이터 전송
        baos.write(new byte[]{0x1D, 0x28, 0x6B, pL, pH, 0x31, 0x50, 0x30});
        baos.write(bytes);

        // 프린트 트리거
        baos.write(new byte[]{0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30});
    }

    /** 래스터 이미지(로고 등) 출력: GS v 0 */
    private void appendRasterImage(ByteArrayOutputStream baos, BufferedImage src, int maxWidth) throws Exception {
        int targetW = Math.min(maxWidth, src.getWidth());
        int targetH = (int) Math.round((double) src.getHeight() * targetW / src.getWidth());

        // 스케일 + 그레이스케일
        BufferedImage gray = new BufferedImage(targetW, targetH, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = gray.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, 0, 0, targetW, targetH, null);
        g.dispose();

        // 임계값(160)으로 1비트화 → 바이트로 패킹
        int bytesPerRow = (targetW + 7) / 8;
        baos.write(new byte[]{0x1D, 0x76, 0x30, 0x00,
                (byte) (bytesPerRow & 0xFF), (byte) ((bytesPerRow >> 8) & 0xFF),
                (byte) (targetH & 0xFF), (byte) ((targetH >> 8) & 0xFF)});

        for (int y = 0; y < targetH; y++) {
            int b = 0, n = 0;
            for (int x = 0; x < targetW; x++) {
                int rgb = gray.getRGB(x, y);
                int luminance = (rgb >> 16) & 0xFF; // R=G=B in gray
                int bit = (luminance < 160) ? 1 : 0; // 밝기 임계값
                b = (b << 1) | bit;
                if (++n == 8) { baos.write((byte) b); b = 0; n = 0; }
            }
            if (n > 0) baos.write((byte) (b << (8 - n)));
        }
        baos.write('\n');
    }

    private void sendToPrinter(byte[] bytes) throws PrintException {
    	ClassLoader orig = Thread.currentThread().getContextClassLoader();
    	try { // JRE 메모리 누수 예방
    		Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
    	        
	        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
	        PrintService target = null;
	        for (PrintService s : PrintServiceLookup.lookupPrintServices(null, null)) {
	            if (s.getName().equalsIgnoreCase(PRINTER_NAME)) { target = s; break; }
	        }
	        if (target == null) throw new PrintException("Printer not found: " + PRINTER_NAME);
	
	        DocPrintJob job = target.createPrintJob();
	        DocAttributeSet das = new HashDocAttributeSet();
	        job.print(new SimpleDoc(bytes, flavor, das), null);
    	}finally {
    		Thread.currentThread().setContextClassLoader(orig);
    	}
    }

    /* (옵션) 한글 소제목/시간을 이미지로 그려서 출력하고 싶을 때 사용
       - 폰트 제약 없이 어떤 문구도 가능, 정렬은 ESC a 로 제어 가능
    */
    public BufferedImage textToImage(String text, Font font, int paddingLR) {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        int textW = (int) (font.getStringBounds(text, frc).getWidth());
        int textH = (int) (font.getStringBounds(text, frc).getHeight());

        int w = Math.min(MAX_WIDTH_DOTS, textW + paddingLR * 2);
        int h = textH + 4;

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE); g.fillRect(0, 0, w, h);
        g.setColor(Color.BLACK); g.setFont(font);
        g.drawString(text, paddingLR, font.getSize());
        g.dispose();
        return img;
    }
}
