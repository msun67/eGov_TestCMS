package egovframework.cms.mypage.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import egovframework.cms.mypage.service.MyPageService;
import egovframework.cms.mypage.vo.MyPageVO;



@Controller
@RequestMapping("/mypage")
public class MyPageController {
	
	@Autowired
    private MyPageService myPageService;

	// 내 정보 화면
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/view.do")
	public String mypage(Model model, org.springframework.security.core.Authentication auth) {
	    String userId = auth.getName(); // username은 항상 채워짐
	    int userType = -1;
	    for (var ga : auth.getAuthorities()) {
	        String r = ga.getAuthority();
	        if ("ROLE_ADMIN".equals(r)) { userType = 0; break; }
	        if ("ROLE_USER".equals(r))  { userType = 1; break; }
	        if ("ROLE_ORG".equals(r))   { userType = 2; break; }
	    }

	    MyPageVO vo = myPageService.getMyInfo(userId, userType);
	    model.addAttribute("me", vo);
	    return "mypage/mypage";
	}

    // 내 정보 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update.do")
    public String updateMyInfo(MyPageVO form,
            					org.springframework.security.core.Authentication auth,
            					RedirectAttributes ra) {
		String userId = auth.getName();
		int userType = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ? 0
		 : auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ORG"))   ? 2
		 : 1;
		
		myPageService.updateMyInfo(form, userId, userType);
		ra.addFlashAttribute("okMessage", "내 정보가 저장되었습니다.");
		return "redirect:/mypage/view.do";
	}

    // 비밀번호 변경
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/password.do")
    public String changePassword(@RequestParam String currentPassword,
						            @RequestParam String newPassword,
						            @RequestParam String newPasswordConfirm,
						            org.springframework.security.core.Authentication auth,
						            RedirectAttributes ra,
						            javax.servlet.http.HttpServletRequest request,
		                            javax.servlet.http.HttpServletResponse response) {
		if (!newPassword.equals(newPasswordConfirm)) {
		ra.addFlashAttribute("errorMessage", "새 비밀번호가 일치하지 않습니다.");
		return "redirect:/mypage/view.do";
		}
		 String userId = auth.getName();
		    int userType = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ? 0
		                 : auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ORG"))   ? 2
		                 : 1;

		    myPageService.changePassword(userId, userType, currentPassword, newPassword);

		    // ✅ 세션 무효화(로그아웃)
		    new SecurityContextLogoutHandler().logout(request, response, auth);

		    // ✅ 로그인 화면으로 메시지 쿼리 파라미터 전달
		    String msg = URLEncoder.encode("비밀번호가 변경되었습니다. 다시 로그인 해주세요.", StandardCharsets.UTF_8);
		    return "redirect:/main.do?okMessage=" + msg;
	}
}
