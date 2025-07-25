package egovframework.cms.member.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.cms.member.vo.SignupVO;

@Service("userSignupService")
public class UserSignupServiceImpl extends EgovAbstractServiceImpl {
	@Autowired private UserSignupMapper userSignupMapper;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    public void register(SignupVO signupVO) throws Exception {
    	try {
    		String generatedUuid = generateShortUUID();
    		signupVO.setUserUuid(generatedUuid);
    		
    		String encodedPassword = passwordEncoder.encode(signupVO.getPassword());
    		signupVO.setPassword(encodedPassword);
    		
    		System.out.println("✅ 회원정보 저장: " + signupVO.getUserId());
    		
    		userSignupMapper.insertUsers(signupVO);
    		System.out.println("✅ 사용자 userNo: " + signupVO.getUserNo());
    		
    		userSignupMapper.insertUserDetails(signupVO);
    		System.out.println("✅ 상세정보 저장 완료");
    		
    	}catch(Exception e) {
    		e.printStackTrace(); // 콘솔 로그 확인
            throw e; // 위로 예외 전파
    	}
    }

    //  UUID를 이용한 짧은 8자리 회원 고유 ID생성.
    private String generateShortUUID() throws NoSuchAlgorithmException {
    	// UUID문자열 변환
        String uuid = UUID.randomUUID().toString();
        // 변환된 UUID를 UTF-8기반으로 인코딩된 바이트 배열로 변환
        byte[] utf8Bytes = uuid.getBytes(StandardCharsets.UTF_8);
        // 해시함수를 이용하여 바이트로 변환
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha256.digest(utf8Bytes);
        // 변환한 바이트에 대해 앞 4글자의 각 바이트를 2자리의 16진수 문자열로 저장
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) sb.append(String.format("%02x", hash[i]));
        return sb.toString();
        // 총 8자리의 고유 값 생성
    }
    
    // 회원가입시 아이디 중복확인
	public boolean isUserIdAvailable(String userId, int userType) {
		int count = userSignupMapper.countByUserIdAndType(userId, userType);
	    return count == 0;
	}

}
