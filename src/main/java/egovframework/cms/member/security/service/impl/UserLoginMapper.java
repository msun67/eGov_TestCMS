package egovframework.cms.member.security.service.impl;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import egovframework.cms.member.security.LoginVO;

@Mapper
public interface UserLoginMapper {
	LoginVO findAccount(String userId);
	LoginVO findAccountWithType(@Param("userId") String userId,
            @Param("userType") int userType);
}
