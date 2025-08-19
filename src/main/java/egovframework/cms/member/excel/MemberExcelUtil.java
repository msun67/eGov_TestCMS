package egovframework.cms.member.excel;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import egovframework.cms.member.vo.SignupVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;           // xls
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;          // xlsx

public class MemberExcelUtil {

    private static final String[] HEADERS = {
        "번호","아이디","이름","권한","휴대폰","가입일","최근로그인","로그인상태","IP"
    };

    public static void writeXlsx(HttpServletResponse res, String filename, List<SignupVO> rows) throws Exception {
        try (Workbook wb = new XSSFWorkbook()) {
            buildSheet(wb, rows);
            send(res, filename + ".xlsx",
                 "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", wb);
        }
    }

    public static void writeXls(HttpServletResponse res, String filename, List<SignupVO> rows) throws Exception {
        try (Workbook wb = new HSSFWorkbook()) {
            buildSheet(wb, rows);
            send(res, filename + ".xls", "application/vnd.ms-excel", wb);
        }
    }
    
    public static byte[] buildXlsxBytes(List<SignupVO> rows) throws Exception {
    	if (rows == null) rows = java.util.Collections.emptyList();
    	System.out.println("[Excel] buildXlsxBytes start; rows=" + rows.size());
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
             java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
            buildSheet(wb, rows);
            wb.write(baos);
            System.out.println("[Excel] wb.write done; size=" + baos.size());
            return baos.toByteArray();
        }
    }

    public static byte[] buildXlsBytes(List<SignupVO> rows) throws Exception {
    	if (rows == null) rows = java.util.Collections.emptyList();
        try (org.apache.poi.hssf.usermodel.HSSFWorkbook wb = new org.apache.poi.hssf.usermodel.HSSFWorkbook();
             java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
            buildSheet(wb, rows);
            wb.write(baos);
            return baos.toByteArray();
        }
    }

    private static void buildSheet(Workbook wb, List<SignupVO> rows) {
    	System.out.println("[Excel] buildSheet start");
        Sheet sh = wb.createSheet("회원목록");
        // 스타일
        CellStyle head = wb.createCellStyle();
        Font bold = wb.createFont(); bold.setBold(true);
        head.setFont(bold);
        head.setAlignment(HorizontalAlignment.CENTER);
        head.setVerticalAlignment(VerticalAlignment.CENTER);
        head.setBorderTop(BorderStyle.THIN);
        head.setBorderBottom(BorderStyle.THIN);
        head.setBorderLeft(BorderStyle.THIN);
        head.setBorderRight(BorderStyle.THIN);

        CellStyle body = wb.createCellStyle();
        body.setBorderTop(BorderStyle.THIN);
        body.setBorderBottom(BorderStyle.THIN);
        body.setBorderLeft(BorderStyle.THIN);
        body.setBorderRight(BorderStyle.THIN);

        // 헤더
        Row hr = sh.createRow(0);
        for (int c = 0; c < HEADERS.length; c++) {
            Cell cell = hr.createCell(c);
            cell.setCellValue(HEADERS[c]);
            cell.setCellStyle(head);
        }

        // 데이터
        int r = 1;
        int total = rows.size();
        for (int i = 0; i < total; i++) {
            SignupVO m = rows.get(i);
            Row row = sh.createRow(r++);
            int col = 0;

            set(row, col++, String.valueOf(total - i), body); // 번호 역순
            set(row, col++, nz(m.getUserId()), body);
            set(row, col++, maskName(nz(m.getName())), body);
            set(row, col++, roleName(m.getUserType()), body);
            set(row, col++, nz(m.getMobile()), body);
            set(row, col++, nz(m.getSignupDate()), body);
            set(row, col++, nz(m.getLastLogin()), body);
            set(row, col++, loginStatus(m.getLoginStatus()), body);
            set(row, col++, nz(m.getLoginIp()), body);
        }

        // 너비 자동조정
		/*
		 * for (int c = 0; c < HEADERS.length; c++) { sh.autoSizeColumn(c); // 한글 깨짐
		 * 보정(조금 여유) int w = sh.getColumnWidth(c); sh.setColumnWidth(c, Math.min(w +
		 * 512, 255*256)); }
		 */
        System.out.println("[Excel] buildSheet end");
    }

    private static void set(Row row, int col, String val, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(val);
        cell.setCellStyle(style);
    }

    private static String nz(String s){ return (s==null) ? "" : s; }

    private static String roleName(Integer t) {
        if (t == null) return "";
        return switch (t) {
            case 0 -> "관리자";
            case 1 -> "사용자";
            default -> "조직원";
        };
    }

    private static String loginStatus(Integer s) {
        if (s == null) return "";
        return (s == 1) ? "로그인" : "로그아웃";
    }

    // 이름 마스킹: 앞/뒤 1글자만 남김
    private static String maskName(String name) {
        if (name == null || name.isEmpty()) return "";
        int len = name.length();
        if (len == 1) return "*";
        if (len == 2) return name.charAt(0) + "*";
        StringBuilder sb = new StringBuilder();
        sb.append(name.charAt(0));
        for (int i = 1; i < len - 1; i++) sb.append('*');
        sb.append(name.charAt(len - 1));
        return sb.toString();
    }

    private static void send(HttpServletResponse res, String fileName, String contentType, Workbook wb) throws Exception {
        // RFC 5987 방식으로 filename* 추가 (한글 파일명 대응)
        String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        res.setContentType(contentType);
        res.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileName + "\"; filename*=UTF-8''" + encoded);
        res.setHeader("Pragma", "no-cache");
        res.setHeader("Expires", "0");
        wb.write(res.getOutputStream());
        res.flushBuffer();
    }
}
