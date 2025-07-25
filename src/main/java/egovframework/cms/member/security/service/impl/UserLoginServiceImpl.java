package egovframework.cms.member.security.service.impl;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import egovframework.cms.member.security.LoginVO;

@Service("userLoginService")
public class UserLoginServiceImpl implements UserDetailsService{
	@Resource
    private UserLoginMapper userLoginMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        LoginVO user = userLoginMapper.findAccount(userId);
        if (user == null) {
            throw new UsernameNotFoundException("해당 ID 없음: " + userId);
        }
        return user;
    }

}
