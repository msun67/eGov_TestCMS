package egovframework.cms.member.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.cms.member.vo.MemberSearchVO;
import egovframework.cms.member.vo.SignupVO;

public interface MemberMapper {
	List<SignupVO> getUserList(MemberSearchVO searchVO); //회원정보조회
    int getUserListCnt(MemberSearchVO searchVO); //회원정보조회 페이징
    List<SignupVO> getUserListExcel(MemberSearchVO searchVO); //엑셀내보내기
    SignupVO selectUserByUuid(String userUuid);	// 상세 조회 (사용자정보 수정 패널 로딩용)
    // 관리자 수정 (권한/비번/휴대폰)
    void updateUserForAdmin(@Param("userUuid") String userUuid,
                            @Param("userType") Integer userType,
                            @Param("password") String encodedPw,
                            @Param("mobile") String mobile);
}
