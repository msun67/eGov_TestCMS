package egovframework.cms.member.security.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import egovframework.cms.member.security.LoginVO;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{
	
	@Autowired
    private egovframework.cms.member.security.service.LoginAuditService userLoginService;

	    @Override
	    public void onLogoutSuccess(HttpServletRequest request,
	                                HttpServletResponse response,
	                                Authentication authentication) {
	        if (authentication != null && authentication.getPrincipal() instanceof LoginVO) {
	            String userUuid = ((LoginVO) authentication.getPrincipal()).getUserUuid();
	            // ✅ last_login=NOW, login_status=0
	            userLoginService.logLogout(userUuid);
	        }
	        try {
	            response.sendRedirect(request.getContextPath() + "/main.do");
	        } catch (Exception ignore) {}
	    }
}
