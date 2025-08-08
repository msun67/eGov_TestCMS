package egovframework.cms.member.security.filter;

import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import egovframework.cms.member.security.LoginVO;

//로그인 직후엔 값이 있는데 특정 요청에서 사라지는 패턴 잡기 위해 추가

public class SecurityDumpFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        Authentication a = SecurityContextHolder.getContext().getAuthentication();

        if (a == null) {
            System.out.println("[DUMP] " + req.getRequestURI() + " auth=null");
        } else {
            Object p = a.getPrincipal();
            String auths = a.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            System.out.println("[DUMP] " + req.getMethod() + " " + req.getRequestURI());
            System.out.println("       auth=" + a.getClass().getSimpleName()
                    + ", principal=" + (p == null ? "null"
                    : p.getClass().getName() + "@" + System.identityHashCode(p))
                    + ", name=" + a.getName()
                    + ", authorities=[" + auths + "]");

            if (p instanceof LoginVO) {
                LoginVO lv = (LoginVO) p;
                System.out.println("       LoginVO fields -> userId=" + lv.getUserId()
                        + ", userType=" + lv.getUserType()
                        + ", userUuid=" + lv.getUserUuid());
            }
        }

        chain.doFilter(req, res);
    }
}
