package egovframework.cms.mypage.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
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

	private static final String VERIFY_KEY = "MYPAGE_VERIFIED_AT";
	private static final long VERIFY_TTL_MILLIS = 10 * 60 * 1000L; // 10분

	private boolean verified(HttpSession session) {
		Object v = session.getAttribute(VERIFY_KEY);
		if (!(v instanceof Long))
			return false;
		return (System.currentTimeMillis() - (Long) v) <= VERIFY_TTL_MILLIS;
	}

	private int resolveUserType(org.springframework.security.core.Authentication auth) {
		for (GrantedAuthority ga : auth.getAuthorities()) {
			String r = ga.getAuthority();
			if ("ROLE_ADMIN".equals(r))
				return 0;
			if ("ROLE_ORG".equals(r))
				return 2;
			if ("ROLE_USER".equals(r))
				return 1;
		}
		return 1; // 기본 사용자
	}

	@Autowired
	private MyPageService myPageService;

	// 마이페이지 진입 전 본인확인 화면
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/verify.do")
	public String verifyForm(
			@RequestParam(value = "returnUrl", required = false, defaultValue = "/mypage/view.do") String returnUrl,
			Model model) {
		model.addAttribute("returnUrl", returnUrl);
		return "mypage/verify"; // /WEB-INF/jsp/mypage/verify.jsp
	}

	// 본인확인 처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/verify.do")
	public String verify(@RequestParam String currentPassword,
			@RequestParam(defaultValue = "/mypage/view.do") String returnUrl,
			org.springframework.security.core.Authentication auth, javax.servlet.http.HttpSession session,
			RedirectAttributes ra) {

		String userId = auth.getName();
		int userType = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ? 0
				: auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")) ? 1 : 2;

		boolean ok = myPageService.verifyCurrentPassword(userId, userType, currentPassword);
		if (!ok) {
			ra.addFlashAttribute("errorMessage", "❌ 현재 비밀번호가 일치하지 않습니다.");
			ra.addFlashAttribute("returnUrl", returnUrl);
			return "redirect:/mypage/verify.do";
		}
		ra.addFlashAttribute("okMessage", "✔️ 마이페이지로 이동합니다.");
		session.setAttribute(VERIFY_KEY, System.currentTimeMillis());
		return "redirect:" + returnUrl;
	}

	// 마이페이지 진입 화면
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/view.do")
	public String mypage(Model model, org.springframework.security.core.Authentication auth, HttpSession session,
			RedirectAttributes ra) {

		// ✅ 최근 본인확인 없으면 verify로
		if (!verified(session)) {
			String ret = URLEncoder.encode("/mypage/view.do", StandardCharsets.UTF_8);
			return "redirect:/mypage/verify.do?returnUrl=" + ret;
		}

		String userId = auth.getName(); // username은 항상 채워짐
		int userType = resolveUserType(auth);

		MyPageVO vo = myPageService.getMyInfo(userId, userType);

		model.addAttribute("me", vo);
		return "mypage/mypage";
	}
	
	// 비밀번호변경 모달창 실시간 검증
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value="/check-current.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public java.util.Map<String,Object> checkCurrent(@RequestParam("currentPassword") String currentPassword,
	                                                 org.springframework.security.core.Authentication auth) {
	    String userId = auth.getName();
	    int userType = resolveUserType(auth);
	    boolean ok = myPageService.verifyCurrentPassword(userId, userType, currentPassword);
	    java.util.Map<String,Object> res = new java.util.HashMap<>();
	    res.put("ok", ok);
	    return res;
	}

	// 내 정보 수정
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update.do")
	public String updateMyInfo(MyPageVO form, org.springframework.security.core.Authentication auth,
			HttpSession session, RedirectAttributes ra) {

		// ✅ 가드
		if (!verified(session)) {
			String ret = URLEncoder.encode("/mypage/view.do", StandardCharsets.UTF_8);
			return "redirect:/mypage/verify.do?returnUrl=" + ret;
		}

		String userId = auth.getName();
		int userType = resolveUserType(auth);

		myPageService.updateMyInfo(form, userId, userType);
		ra.addFlashAttribute("okMessage", "✔️ 내 정보가 저장되었습니다.");
		return "redirect:/mypage/view.do";
	}

	// 비밀번호 변경
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/password.do")
	public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
			@RequestParam String newPasswordConfirm, org.springframework.security.core.Authentication auth,
			RedirectAttributes ra, javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response, HttpSession session) {

		if (!newPassword.equals(newPasswordConfirm)) {
			ra.addFlashAttribute("errorMessage", "❌ 새 비밀번호가 일치하지 않습니다.");
			return "redirect:/mypage/view.do";
		}

		// ✅ 가드
		if (!verified(session)) {
			String ret = URLEncoder.encode("/mypage/view.do", StandardCharsets.UTF_8);
			return "redirect:/mypage/verify.do?returnUrl=" + ret;
		}

		String userId = auth.getName();
		int userType = resolveUserType(auth);

		myPageService.changePassword(userId, userType, currentPassword, newPassword);

		// ✅ 세션 무효화(로그아웃)
		new SecurityContextLogoutHandler().logout(request, response, auth);

		// ✅ 로그인 화면으로 메시지 쿼리 파라미터 전달
		String msg = URLEncoder.encode("✔️ 비밀번호가 변경되었습니다. 다시 로그인 해주세요.", StandardCharsets.UTF_8);
		return "redirect:/main.do?okMessage=" + msg;
	}
}
