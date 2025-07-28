package egovframework.cms.main.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import egovframework.cms.member.security.LoginVO;

@Controller
public class DashboardController {
	
	@GetMapping("/dashboard.do")
    public String dashboard(@AuthenticationPrincipal LoginVO loginVO, Model model) {
        int userType = loginVO.getUserType(); // 0: 관리자, 1: 사용자, 2: 부서원
        model.addAttribute("userType", userType); // JSP에서 조건 분기용
        return "common/dashboard"; // 공통 대시보드 JSP
    }

}
