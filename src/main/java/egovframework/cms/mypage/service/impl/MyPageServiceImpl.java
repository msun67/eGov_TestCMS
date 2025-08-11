package egovframework.cms.mypage.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.cms.mypage.service.MyPageService;
import egovframework.cms.mypage.vo.MyPageVO;


@Service
public class MyPageServiceImpl implements MyPageService{
	
	@Autowired
    private MyPageMapper mapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override //검증
    public boolean verifyCurrentPassword(String userId, int userType, String currentPw) {
        MyPageVO me = mapper.selectMyInfo(userId, userType);
        if (me == null) return false;
        String currentHash = mapper.selectCurrentPassword(me.getUserNo());
        return currentHash != null && passwordEncoder.matches(currentPw, currentHash);
    }

    @Override
    public MyPageVO getMyInfo(String userId, int userType) {
        return mapper.selectMyInfo(userId, userType);
    }

    @Override
    public void updateMyInfo(MyPageVO form, String userId, int userType) {
    	 // 세션 기준으로 본인 PK 재확정
        MyPageVO me = mapper.selectMyInfo(userId, userType);
        if (me == null) {
            throw new AccessDeniedException("사용자 정보를 찾을 수 없습니다.");
        }

        // 폼의 userNo는 신뢰하지 않고, 세션에서 찾은 PK를 강제 지정
        form.setUserNo(me.getUserNo());

        mapper.updateMyInfo(form); // name/phone/mobile/address/residence 업데이트
    }

    @Transactional
    @Override
    public void changePassword(String userId, int userType, String currentPw, String newPw) {
    	MyPageVO me = mapper.selectMyInfo(userId, userType);
        if (me == null) {
            throw new AccessDeniedException("사용자 정보를 찾을 수 없습니다.");
        }

        String currentHash = mapper.selectCurrentPassword(me.getUserNo());
        if (currentHash == null || !passwordEncoder.matches(currentPw, currentHash)) {
            throw new BadCredentialsException("현재 비밀번호가 일치하지 않습니다.");
        }

        String enc = passwordEncoder.encode(newPw);
        mapper.updatePassword(me.getUserNo(), enc);
        
        mapper.insertPasswordHistory(me.getUserNo(), enc, userType);
        
        int pwId = mapper.selectLastInsertId(); // 또는 selectLatestPwIdByUser(me.getUserNo())
        mapper.updateLatestPwId(me.getUserNo(), pwId);
    }

}
