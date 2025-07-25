package egovframework.cms.member.security.service.impl;

import org.apache.ibatis.annotations.Mapper;

import egovframework.cms.member.security.LoginVO;

@Mapper
public interface UserLoginMapper {
	LoginVO findAccount(String userId);
}
