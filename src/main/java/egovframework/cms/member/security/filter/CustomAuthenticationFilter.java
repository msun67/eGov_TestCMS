package egovframework.cms.member.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import egovframework.cms.member.security.token.CustomAuthenticationToken;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	
	private String userTypeParameter = "userType"; // 기본값 설정 가능

    public void setUserTypeParameter(String userTypeParameter) {
        this.userTypeParameter = userTypeParameter;
    }
    public String getUserTypeParameter() {
        return userTypeParameter;
    }
	
	
	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/loginProcess.do"); // 로그인 URL 지정
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType"); // ✅ 추가된 파라미터

        CustomAuthenticationToken authRequest =
                new CustomAuthenticationToken(username, password, userType);

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
