package egovframework.cms.main.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import egovframework.cms.board.service.BoardService;
import egovframework.cms.board.service.BoardVO;
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

        // ✅ dashboard.jsp - 요약카드 (미답변문의, 공지 진행중은 게시판 기능 구현안되어있으므로 보류.)
        int totalPosts = boardService.countAllPosts();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPosts", totalPosts); //전체 게시글
        stats.put("todayPosts", boardService.countTodayPosts()); //오늘등록
        request.setAttribute("stats", stats); 
        request.setAttribute("today", new java.util.Date()); // 오늘 날짜 (JSTL fmt용)
        
        List<BoardVO> boardRecent = boardService.findRecentWithFileCount(7); //최근게시글
        request.setAttribute("boardRecent", boardRecent);
        
        
     // ✅ dashboard.jsp - 공지사항의 최근 5건
        List<BoardVO> noticeList = boardService.findRecentByBoardCode("notice", 5);
        request.setAttribute("noticeList", noticeList);
        
        return "common/dashboard";
    }
}
