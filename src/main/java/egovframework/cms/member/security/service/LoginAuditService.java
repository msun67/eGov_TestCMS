package egovframework.cms.member.security.service;

//로그인/로그아웃 메타 기록용 작은 인터페이스
public interface LoginAuditService {
	void logLoginSuccess(String userUuid, String ip);
    void logLogout(String userUuid);
}
