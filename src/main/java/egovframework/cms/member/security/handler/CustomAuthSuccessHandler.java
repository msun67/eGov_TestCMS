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
	    
	    
		/* // 권한에 따라 리다이렉트 
		 * LoginVO user = (LoginVO) authentication.getPrincipal();
		 * 
		 * switch (user.getUserType()) { case 0: // ROLE_ADMIN
		 * response.sendRedirect(request.getContextPath() + "/admin/board.do"); break;
		 * case 1: // ROLE_USER response.sendRedirect(request.getContextPath() +
		 * "/user/board.do"); break; case 2: // ROLE_ORG
		 * response.sendRedirect(request.getContextPath() + "/org/board.do"); break;
		 * default: response.sendRedirect(request.getContextPath() + "/main.do"); break;
		 * }
		 */
    }
}
