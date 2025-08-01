package egovframework.cms.member.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomSecurityHandler 
		implements AuthenticationFailureHandler, AccessDeniedHandler{
	
	// 로그인 실패 시 메시지와 함께 리다이렉트(로그인 실패)
	@Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
                                        throws IOException, ServletException {        
        String errorMessage = exception.getMessage(); // ex) "비밀번호가 잘못되었습니다."
        request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", errorMessage);
        response.sendRedirect(request.getContextPath() + "/main.do?error=true");
    }
	
    // <intercept-url>의 권한별 접근제한 URL에 직접 접속하려할때 리턴
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException {
    	
    	request.getSession().setAttribute("warningMessage", "🔒 권한이 없습니다.");
        response.sendRedirect(request.getContextPath() + "/board.do");

    }
}
