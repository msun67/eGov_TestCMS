package egovframework.cms.member.security.service.impl;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import egovframework.cms.member.security.LoginVO;

@Mapper
public interface UserLoginMapper {
	
	// 스프링 시큐리티 기본
	LoginVO findAccount(String userId);
	
	//스프링 시큐리티 커스텀
	LoginVO findAccountWithType(@Param("userId") String userId,
            @Param("userType") int userType);
	
	// 로그인 성공 시 갱신 (NOW(), 상태=1, IP)
    int updateLoginSuccessByUuid(@Param("userUuid") String userUuid,
                                 @Param("ip") String ip);

    // 로그아웃 시 갱신 (마지막 로그인 시간, 상태=0)
    int updateLogoutByUuid(@Param("userUuid") String userUuid);
}
