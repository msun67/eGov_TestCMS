package egovframework.cms.member.vo;

public class SignupVO {
	// 회원가입과 관리자전용페이지 회원리스트 함께 사용	
	private int userNo;
    private String userUuid;
    private String userId;
    private String password;
    private String name;
    private int userType;		// DB 저장용
    private String userTypeStr; // form 전달용 (ex. "ROLE_USER")
    private String phone;
    private String mobile;
    private String address;
    private String residence;
    private String signupIp;
    
    // ✅ 목록용 표시 필드(스키마 매핑)
    private String signupDate;   // user_details.signup_date
    private String lastModified; // user_details.last_modified
    private String lastLogin;    // users.last_login
    private Integer loginStatus; // users.login_status
    private String loginIp;      // users.login_ip
    
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getUserUuid() {
		return userUuid;
	}
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	
	public String getUserTypeStr() {
		return userTypeStr;
	}
	public void setUserTypeStr(String userTypeStr) {
		this.userTypeStr = userTypeStr;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getResidence() {
		return residence;
	}
	public void setResidence(String residence) {
		this.residence = residence;
	}
	public String getSignupIp() {
		return signupIp;
	}
	public void setSignupIp(String signupIp) {
		this.signupIp = signupIp;
	}
	public String getSignupDate() {
		return signupDate;
	}
	public void setSignupDate(String signupDate) {
		this.signupDate = signupDate;
	}
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public Integer getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(Integer loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
}
