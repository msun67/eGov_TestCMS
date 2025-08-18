package egovframework.cms.member.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import egovframework.cms.member.vo.MemberSearchVO;
import egovframework.cms.member.vo.SignupVO;
import lombok.RequiredArgsConstructor;

@Service("memberService")
@RequiredArgsConstructor
public class MemberServiceImpl {
	
	private final MemberMapper memberMapper;

    public List<SignupVO> getUserList(MemberSearchVO searchVO) {
        return memberMapper.getUserList(searchVO);
    }

    public int getUserListCnt(MemberSearchVO searchVO) {
        return memberMapper.getUserListCnt(searchVO);
    }

}
