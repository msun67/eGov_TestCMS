package egovframework.cms.member.service.impl;

import org.apache.ibatis.annotations.Param;

import egovframework.cms.member.vo.SignupVO;

public interface UserSignupMapper {
	
	 void insertUsers(SignupVO vo);         // users 테이블 INSERT
	 void insertUserDetails(SignupVO vo);   // user_details 테이블 INSERT
	 int countByUserIdAndType(@Param("userId") String userId, @Param("userType") int userType);		// user_details 테이블에서 id 중복확인(COUNT)
}
