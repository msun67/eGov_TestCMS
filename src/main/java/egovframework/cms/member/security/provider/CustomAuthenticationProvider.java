package egovframework.cms.member.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import egovframework.cms.member.security.LoginVO;
import egovframework.cms.member.security.service.impl.UserLoginMapper;
import egovframework.cms.member.security.token.CustomAuthenticationToken;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
	

    private UserLoginMapper userLoginMapper;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void setUserLoginMapper(UserLoginMapper userLoginMapper) {
        this.userLoginMapper = userLoginMapper;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthenticationToken token = (CustomAuthenticationToken) authentication;

        String username = token.getName();
        String password = token.getCredentials().toString();
        String userType = token.getUserType();

        // ✅ userType을 int로 변환
        int userTypeInt;

        switch (userType) {
            case "ROLE_ADMIN":
                userTypeInt = 0;
                break;
            case "ROLE_USER":
                userTypeInt = 1;
                break;
            case "ROLE_ORG":
                userTypeInt = 2;
                break;
            default:
            	throw new BadCredentialsException("잘못된 권한입니다.");
        }

        LoginVO user = userLoginMapper.findAccountWithType(username, userTypeInt);
        if (user == null) {
            throw new BadCredentialsException("아이디 또는 권한이 잘못되었습니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 잘못되었습니다.");
        }
        System.out.println("uuid = " + user.getUserUuid());

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
