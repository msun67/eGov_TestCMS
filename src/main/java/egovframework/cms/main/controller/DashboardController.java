package egovframework.cms.main.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import egovframework.cms.board.service.BoardService;
import egovframework.cms.member.security.LoginVO;

@Controller
public class DashboardController {
	
	private final BoardService boardService;
	
	public DashboardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@GetMapping("/dashboard.do")
	// ✅ principal - 스프링시큐리티에서 현재 인증된 사용자의 정보를 담고 있는 객체
    public String dashboard(HttpServletRequest request, Principal principal) {
        if (principal instanceof Authentication) {
            Object userObj = ((Authentication) principal).getPrincipal();
            if (userObj instanceof LoginVO) {
                LoginVO user = (LoginVO) userObj;
                int userType = user.getUserType();
                request.setAttribute("userType", userType); // EL에서 바로 접근 가능
                //System.out.println("✅ DashboardController userType = " + userType);
            }
        }
        
     // ✅ 전체 게시글 수 조회
        int totalPosts = boardService.countAllPosts();
        // JSP에서 ${stats.totalPosts}로 사용
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPosts", totalPosts);
        request.setAttribute("stats", stats);
        return "common/dashboard";
    }

}
