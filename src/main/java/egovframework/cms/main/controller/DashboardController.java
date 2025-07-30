package egovframework.cms.main.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import egovframework.cms.member.security.LoginVO;

@Controller
public class DashboardController {
	
	@GetMapping("/dashboard.do")
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
        return "common/dashboard";
    }

}
