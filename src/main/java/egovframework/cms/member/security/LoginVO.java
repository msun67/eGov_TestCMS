package egovframework.cms.member.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class LoginVO implements UserDetails {

	// 직렬화된 객체를 저장하고 다시 역직렬화할 때, 클래스 구조가 변경되었는지 체크하기 위한 용도
	private static final long serialVersionUID = 1L;
	
	private String userId;
    private String password;
    private int userType;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    @Override
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String getUsername() { return userId; } // Spring Security가 이 메서드로 사용자명 확인

    public int getUserType() { return userType; }
    public void setUserType(int userType) { this.userType = userType; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + getRoleNameByType(userType));
    }

    private String getRoleNameByType(int userType) {
        switch (userType) {
            case 0: return "ROLE_ADMIN";
            case 1: return "ROLE_USER";
            case 2: return "ROLE_ORG";
            default: return "GUEST";
        }
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

}
