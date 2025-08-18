package egovframework.cms.member.service.impl;

import java.util.List;

import egovframework.cms.member.vo.MemberSearchVO;
import egovframework.cms.member.vo.SignupVO;

public interface MemberMapper {
	List<SignupVO> getUserList(MemberSearchVO searchVO); //회원정보조회
    int getUserListCnt(MemberSearchVO searchVO); //회원정보조회 페이징
}
