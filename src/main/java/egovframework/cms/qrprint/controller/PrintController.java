package egovframework.cms.qrprint.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.cms.qrprint.service.QRPrinterService;

@Controller
@RequestMapping("/admin/print")
public class PrintController {

	private final QRPrinterService qrPrinter = new QRPrinterService();
	
	// QR출력 화면으로 이동
	@RequestMapping("/qrprint.do")
    public String formPage() {
        return "print/qrprint";
    }
	
	// QR출력
	@PostMapping(value = "/qr.do", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Map<String,Object> print() {
        try {
            qrPrinter.printTicket("https://www.naver.com/",
                    "D:/eGov4.3.0/logo/gangwon_logo.png" /* 또는 클래스패스 경로로 바꾸세요 */);
            return Map.of("ok", true, "message", "출력 완료");
        } catch (Exception e) {
            return Map.of("ok", false, "message", "실패: " + e.getMessage());
        }
	}
}
