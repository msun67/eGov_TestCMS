package egovframework.cms.member.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.RequestContextUtils;

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
                       AccessDeniedException accessDeniedException)throws IOException {

    	// 1) FlashMap 생성 및 메시지 설정
        FlashMap flashMap = new FlashMap();
        flashMap.put("warningMessage", "🔒 권한이 없습니다.");

        // 2) 리다이렉트 타깃 경로 지정
        String targetPath = "/dashboard.do"; // 컨텍스트 경로 없이 애플리케이션 내부 경로
        flashMap.setTargetRequestPath(targetPath);

        // 3) FlashMap 저장
        FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);
        if (flashMapManager != null) {
            flashMapManager.saveOutputFlashMap(flashMap, request, response);
        }

        // 4) 실제 리다이렉트
        String redirectUrl = request.getContextPath() + targetPath;
        response.sendRedirect(redirectUrl);

    }
}
