package egovframework.cms.member.web;

import java.util.HashMap;
import java.util.Map;

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
    public String signupProcess(@ModelAttribute SignupVO signupVO) throws Exception {
    	System.out.println("✅ signupProcess 진입");
    	userSignupService.register(signupVO);
        return "redirect:/main.do";
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
