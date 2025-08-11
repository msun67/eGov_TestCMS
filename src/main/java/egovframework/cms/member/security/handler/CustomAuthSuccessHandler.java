package egovframework.cms.member.security.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import egovframework.cms.member.security.LoginVO;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private egovframework.cms.member.security.service.LoginAuditService userLoginService;
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                        throws IOException, ServletException {
		// principal 확인
        if (authentication != null && authentication.getPrincipal() instanceof LoginVO) {
            LoginVO login = (LoginVO) authentication.getPrincipal();
            String userUuid = login.getUserUuid();
            String ip = extractClientIp(request);

            // ✅ 로그인 메타 업데이트: login_datetime=NOW, login_status=1, login_ip=ip
            userLoginService.logLoginSuccess(userUuid, ip);
        }
        
		// 모든 권한 사용자 공통 대시보드로 리다이렉트
	    response.sendRedirect(request.getContextPath() + "/dashboard.do");
    }
	
	// IP저장 프록시/로드밸런서 환경 고려 정규화 
    private String extractClientIp(HttpServletRequest req) {
    	// 1) 프록시/로드밸런서 헤더 우선
        String[] hdrs = {
            "X-Forwarded-For","X-Real-IP","CF-Connecting-IP",
            "Proxy-Client-IP","WL-Proxy-Client-IP","HTTP_CLIENT_IP","HTTP_X_FORWARDED_FOR"
        };
        for (String h : hdrs) {
            String v = req.getHeader(h);
            if (v != null && !v.isEmpty() && !"unknown".equalsIgnoreCase(v)) {
            	// XFF: "client, proxy1, proxy2" → 첫 IP
                int comma = v.indexOf(',');
                String ip = (comma > 0) ? v.substring(0, comma).trim() : v.trim();
                return normalizeIp(ip);
            }
        }
        // 2) 헤더가 없으면 remoteAddr
        return normalizeIp(req.getRemoteAddr());
    }
    
    private String normalizeIp(String ip) {
        if (ip == null) return null;
        // IPv6 루프백 → IPv4 루프백으로 통일
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return "127.0.0.1";
        }
        // IPv6 압축 표기 방지하고 싶으면 여기서 더 처리 가능
        return ip;
    }
}
