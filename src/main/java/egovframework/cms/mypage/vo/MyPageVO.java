package egovframework.cms.mypage.vo;

import java.io.Serializable;

public class MyPageVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	 	private Integer userNo;
	    private String userUuid;
	    private Integer userType;
	    private String userId;
	    private String password;     // 화면에는 사용 안 함(****** 처리), 변경 시만 사용
	    private String name;
	    private String phone;
	    private String mobile;
	    private String address;
	    private String residence;
	    private String signupDate;   // 포맷팅된 문자열
	    
	    
		public Integer getUserNo() {
			return userNo;
		}
		public void setUserNo(Integer userNo) {
			this.userNo = userNo;
		}
		public String getUserUuid() {
			return userUuid;
		}
		public void setUserUuid(String userUuid) {
			this.userUuid = userUuid;
		}
		public Integer getUserType() {
			return userType;
		}
		public void setUserType(Integer userType) {
			this.userType = userType;
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
		public String getSignupDate() {
			return signupDate;
		}
		public void setSignupDate(String signupDate) {
			this.signupDate = signupDate;
		}
}
