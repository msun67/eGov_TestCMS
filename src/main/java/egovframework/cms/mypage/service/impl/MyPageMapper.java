package egovframework.cms.mypage.service.impl;

import org.apache.ibatis.annotations.Param;

import egovframework.cms.mypage.vo.MyPageVO;

public interface MyPageMapper {
	
	 // 내 정보 조회 (세션의 userId + userType 기준)
    MyPageVO selectMyInfo(@Param("userId") String userId,
                          @Param("userType") int userType);

    // 내 정보 수정 (name/phone/mobile/address/residence)
    void updateMyInfo(MyPageVO vo);

    // 현재 비밀번호 해시 조회
    String selectCurrentPassword(@Param("userNo") int userNo);

    // 비밀번호 업데이트
    void updatePassword(@Param("userNo") int userNo,
                        @Param("encPassword") String encPassword);

    // 비밀번호 이력 insert (생성된 pw_id는 별도 조회 필요)
    int insertPasswordHistory(@Param("userNo") int userNo,
                              @Param("encPassword") String encPassword,
                              @Param("userType") int userType);

    // 같은 커넥션 세션에서 방금 insert된 AUTO_INCREMENT id 반환
    Integer selectLastInsertId();

    // 특정 사용자 기준 가장 최근 pw_id (fallback)
    Integer selectLatestPwIdByUser(@Param("userNo") int userNo);
    
    // users.latest_pw_id 갱신
    void updateLatestPwId(@Param("userNo") int userNo,
            				@Param("pwId") int pwId);

}
