package egovframework.cms.member.web;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.cms.member.service.impl.UserSignupServiceImpl;
import egovframework.cms.member.vo.SignupVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SignupController {

	
	private static final Logger logger = LoggerFactory.getLogger(SignupController.class);
	
    @Autowired
    private UserSignupServiceImpl userSignupService;

    // 회원가입 화면이동
    @RequestMapping("/signup.do")
    public String signupForm() {
    	System.out.println("✅ signupForm 진입");
        return "member/signup"; // signup.jsp
    }
    
    // 회원가입 완료 후 처리
    @PostMapping("/signupProcess.do")
    public String signupProcess(@ModelAttribute SignupVO signupVO,
    							HttpServletRequest req) throws Exception {
        System.out.println("✅ signupProcess 진입");

        // userType (String → int 변환)
        String roleType = signupVO.getUserTypeStr(); // ex) ROLE_USER
        int userTypeInt = mapRoleToUserType(roleType);
        signupVO.setUserType(userTypeInt);
        
        // ✅ 가입 IP 세팅
        signupVO.setSignupIp(extractClientIp(req));

        userSignupService.register(signupVO);
        
        String msg = URLEncoder.encode("✔️ 회원가입이 완료되었습니다. 로그인 해주세요.", StandardCharsets.UTF_8);
        return "redirect:/main.do?okMessage=" + msg;
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
    
    // 회원가입시 ID중복처리 (ID와 TYPE값으로 처리)
    @ResponseBody
    @GetMapping("/checkUserId.do")
    public Map<String, Boolean> checkUserId(@RequestParam("userId") String userId,
            								@RequestParam("userType") String userType) {
    	logger.info("👉 userId={}, userType={}", userId, userType);
    	
		int userTypeInt = mapRoleToUserType(userType); // ADMIN:0, USER:1, ORG:2
		boolean available = userSignupService.isUserIdAvailable(userId, userTypeInt);
		Map<String, Boolean> result = new HashMap<>();
		result.put("available", available);
		return result;
		}

	private int mapRoleToUserType(String userType) {
		logger.info("mapRoleToUserType 진입: {}", userType);
		switch (userType) {
        case "ROLE_ADMIN": return 0;
        case "ROLE_USER": return 1;
        case "ROLE_ORG":  return 2;
        default:
            throw new IllegalArgumentException("Invalid role type: " + userType);
		}
	}
}
