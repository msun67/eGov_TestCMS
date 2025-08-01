package egovframework.cms.member.security.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                        throws IOException, ServletException {
        
		// 모든 권한 사용자 공통 대시보드로 리다이렉트
	    response.sendRedirect(request.getContextPath() + "/dashboard.do");
	   
    }
}
