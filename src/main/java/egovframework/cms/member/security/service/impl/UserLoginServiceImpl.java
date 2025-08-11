package egovframework.cms.member.security.service.impl;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import egovframework.cms.member.security.LoginVO;
import egovframework.cms.member.security.service.LoginAuditService;

@Service("userLoginService")
public class UserLoginServiceImpl implements UserDetailsService, LoginAuditService{
	@Resource
    private UserLoginMapper userLoginMapper;

	
	//스프링 시큐리티 기본 용도
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		/*
		 * LoginVO user = userLoginMapper.findAccount(userId); if (user == null) { throw
		 * new UsernameNotFoundException("해당 ID 없음: " + userId); } return user;
		 */
    	throw new UsernameNotFoundException("userType이 없는 요청은 지원하지 않습니다.");
    }
    
    // ✅ userType까지 받는 새 메서드 추가
    public UserDetails loadUserByUsernameAndType(String userId, int userType)
            throws UsernameNotFoundException {

        LoginVO user = userLoginMapper.findAccountWithType(userId, userType);

        if (user == null) {
            throw new UsernameNotFoundException("ID 또는 권한이 올바르지 않습니다.");
        }
        System.out.println("✅ loadUserByUsername userType = " + user.getUserType());
        
        return user;
    }
    
    // LoginAuditService 구현
    // 로그인 성공 시 갱신 (NOW(), 상태=1, IP)
    @Override
    public void logLoginSuccess(String userUuid, String ip) {
        userLoginMapper.updateLoginSuccessByUuid(userUuid, ip);
    }

    // 로그아웃 시 갱신 (마지막 로그인 시간, 상태=0)
    @Override
    public void logLogout(String userUuid) {
        userLoginMapper.updateLogoutByUuid(userUuid);
    }

}
