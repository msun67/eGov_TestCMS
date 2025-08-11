package egovframework.cms.mypage.service;

import egovframework.cms.mypage.vo.MyPageVO;

public interface MyPageService {
	
	boolean verifyCurrentPassword(String userId, int userType, String currentPw); //검증
	MyPageVO getMyInfo(String userId, int userType);
    void updateMyInfo(MyPageVO form, String userId, int userType);
    void changePassword(String userId, int userType, String currentPw, String newPw);
    
}
