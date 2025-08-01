package egovframework.cms.member.security.handler;
import org.springframework.security.access.AccessDeniedException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(annotations = Controller.class)
public class MethodSecurityExceptionHandler {
	
	// @PreAuthorize 메서드 보안에서 던진 AccessDeniedException을 MVC에서 처리(게시판 공지사항 제한)
	@ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(HttpServletRequest req, RedirectAttributes ra) {
        String boardCode = req.getParameter("boardCode");
        // 목록으로 되돌리며 플래시 메시지 추가
        ra.addFlashAttribute("warningMessage", "🔒 공지사항은 관리자만 작성할 수 있습니다.");

        String redirect = "/board.do";
        if (boardCode != null && !boardCode.isEmpty()) {
            redirect += "?boardCode=" + URLEncoder.encode(boardCode, StandardCharsets.UTF_8);
        }
        return "redirect:" + redirect;  // 컨텍스트 경로는 Dispatcher가 자동으로 붙여줍니다
    }

}
