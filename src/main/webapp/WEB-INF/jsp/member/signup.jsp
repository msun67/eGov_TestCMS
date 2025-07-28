<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<meta charset="UTF-8">
<title>회원 가입</title>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
    * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Inter', sans-serif; }
    body { background-color: #ffffff; display: flex; justify-content: center; align-items: center; min-height: 100vh; }
    .container {
      background-color: #f9fafb;
      padding: 40px;
      border-radius: 16px;
      border: 1px solid #e5e7eb;
      width: 100%;
      max-width: 420px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
    }
    h2 {
      text-align: center;
      font-size: 24px;
      font-weight: 600;
      color: #1e3a8a;
      margin-bottom: 28px;
    }
    label {
      display: block;
      font-weight: 500;
      margin-bottom: 6px;
      color: #1f2937;
    }
    input[type="text"], input[type="password"] {
      width: 100%;
      padding: 12px 14px;
      margin-bottom: 12px;
      border: 1px solid #cbd5e1;
      border-radius: 8px;
      background-color: #ffffff;
      font-size: 15px;
    }
    .error-msg {
      font-size: 12px;
      color: #ef4444;
      height: 14px;
      margin-top: -8px;
      margin-bottom: 12px;
      display: block;
    }
    .btn-row {
      display: flex;
      gap: 8px;
      align-items: center;
      margin-bottom: 12px;
    }
    .btn-row input[type="text"] {
      flex: 1;
    }
    .btn-row button {
      padding: 11px 18px;
      font-size: 14px;
      margin-bottom: auto;
    }
    button {
      padding: 12px 20px;
      background-color: #3182f6;
      color: #ffffff;
      font-weight: 600;
      border: none;
      border-radius: 8px;
      cursor: pointer;
      transition: background 0.2s ease-in-out;
      font-size: 15px;
    }
    button:hover {
      background-color: #2563eb;
    }
    .submit-btn {
      width: 100%;
      margin-top: 16px;
    }
</style>

<div class ="container">
	<h2>회원가입</h2>
	<form action="${pageContext.request.contextPath}/signupProcess.do" method="post">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	
		<input type="radio" name="userTypeStr" value="ROLE_ADMIN"> 관리자
		<input type="radio" name="userTypeStr" value="ROLE_USER" checked> 일반 사용자
		<input type="radio" name="userTypeStr" value="ROLE_ORG"> 부서원
	
		<label for="userId">아이디</label>
		<div class="btn-row">
			<input type="text" name="userId" id="userId" required />
			<button type="button" onclick="checkUserId()">중복확인</button>
		</div> 
		<span id="idCheckResult" class="error-msg"></span>
		
		<label for="password">비밀번호</label>
		<input type="password" name="password" id="password" required />
		<span id="passwordError" class="error-msg"></span>
		
		<label for="name">이름</label>
		<input type="text" name="name" id="name" required />
		<span id="nameError" class="error-msg"></span>
		
		<label for="phone">전화번호</label>
		<input type="text" name="phone" id="phone" required />
		<span id="phoneError" class="error-msg"></span>
		
		<label for="mobile">핸드폰번호</label>
		<input type="text" name="mobile" id="mobile" required />
		<span id="mobileError" class="error-msg"></span>
		
		<label for="address">주소</label>
		<input type="text" name="address" id="address" required />
		<span id="addressError" class="error-msg"></span>
		
		<button id="submitBtn" type="submit" id="submit" class="submit-btn">가입</button>
	</form>
</div>
<c:if test="${not empty errorMsg}">
	<p style="color: red;">${errorMsg}</p>
</c:if>


<script>
	
	//유효성 검사
	function validateFormCommon(inputId, spanId, regex, message){
		const value = document.getElementById(inputId).value.trim();
		const span = document.getElementById(spanId);
		if(!regex.test(value)){
			span.innerText = message;
			span.style.color = "red";
			return false;

		}else{
			span.innerText = "";
			return true;
		}
	}
	
	// 이벤트 등록
	document.getElementById("userId").addEventListener("blur", () => {
	    validateFormCommon("userId", "idCheckResult", /^[a-zA-Z0-9]{4,12}$/, "아이디는 영문자+숫자 4~12자");
	});

	document.getElementById("password").addEventListener("blur", () => {
	    validateFormCommon("password", "passwordError", /^.{6,}$/, "비밀번호는 6자 이상이어야 합니다.");
	});
	
	document.getElementById("phone").addEventListener("blur", () => {
	    validateFormCommon("phone", "phoneError", /^[0-9]+$/, "전화번호는 숫자만 입력해주세요.");
	});

	document.getElementById("mobile").addEventListener("blur", () => {
	    validateFormCommon("mobile", "mobileError", /^[0-9]+$/, "핸드폰번호는 숫자만 입력해주세요.");
	});

	document.getElementById("address").addEventListener("blur", () => {
	    validateFormCommon("address", "addressError", /^.+$/, "주소를 입력해주세요.");
	});
	

	
 	
	//회원가입 유효성 검사
	function validateForm(){
		const userId = document.getElementById("userId").value.trim();
		const password = document.getElementById("password").value.trim();
		const name = document.getElementById("name").value.trim();
		const phone = document.getElementById("phone").value.trim();
		const mobile = document.getElementById("mobile").value.trim();
		const address = document.getElementById("address").value.trim();
		
		let isValid = true;
		//에러 메시지 초기화
		document.querySelectorAll(".error-msg").forEach(el => el.innerText = "");
		
		//위와 동일
		/* const errorElements = document.querySelectorAll(".error-msg");
		for (let i = 0; i < errorElements.length; i++) {
		    errorElements[i].innerText = "";
		} */
		
 		const idRegex = /^[a-zA-Z0-9]{4,12}$/;
		//아이디 형식 체크
		if (!idRegex.test(userId)) {
			document.getElementById("idCheckResult").innerText = "영문자 + 숫자 4~12자여야 합니다.";
			isValid = false;
		}
		
		//비밀번호 형식 체크
		if (password.length < 6) {
			document.getElementById("passwordError").innerText = "비밀번호는 6자 이상이어야 합니다.";
			isValid = false;
		}
		
		//전화번호 형식 체크
		const numberRegex = /^[0-9]+$/;
		if (!numberRegex.test(phone)) {
			document.getElementById("phoneError").innerText = "전화번호는 숫자만 입력해주세요.";
			isValid = false;
		}
		
		//핸드폰번호 형식 체크
		if (!numberRegex.test(mobile)) {
			document.getElementById("mobileError").innerText = "핸드폰번호는 숫자만 입력해주세요.";
			isValid = false;
		}

		//이름 형식 체크
		if (name === "") {
			document.getElementById("nameError").innerText = "이름을 입력해주세요.";
			isValid = false;
		}

		//주소 형식 체크
		if (address === "") {
			document.getElementById("addressError").innerText = "주소를 입력해주세요.";
			isValid = false;
		}

		return isValid;
	}
	
	//아이디 중복 검사와 중복 검사하지 않았을 때 가입 버튼 비활성화
 	//JSP의 Context Path(프로젝트 경로)를 가져옴
 	<%--
 	 표현식 오해를 피하기 위해 <%= >% 앞뒤 꼭 띄어쓰기하기
 	 JSP의 경우 주석 내용도 조심할 것!!!! 주석 코드도 읽어버리는 경우 발생 
 	--%>
 	
	
    
    /* 아이디 중복 체크 및 가입 완료시 아이디 중복 체크 여부 확인 */
	function checkUserId() {
		const userId = document.getElementById("userId").value.trim();
	    const userType = document.querySelector('input[name="userTypeStr"]:checked').value;
	    const resultSpan = document.getElementById("idCheckResult");
	    const submitBtn = document.getElementById("submitBtn");
	    const ctx = '<%= request.getContextPath() %>';

	    console.log("🔍 userType =", userType);

	    fetch(ctx + "/checkUserId.do?userId=" + encodeURIComponent(userId) + "&userType=" + encodeURIComponent(userType))
	        .then(response => {
	            if (!response.ok) {
	                throw new Error("❌ 서버 응답 오류! status=" + response.status);
	            }
	            return response.json(); // JSON 응답 파싱
	        })
	        .then(result => {
	            if (result.available === false) {
	                resultSpan.innerText = "이미 사용 중인 아이디입니다.";
	                resultSpan.style.color = "red";
	                submitBtn.disabled = true;
	            } else {
	                resultSpan.innerText = "사용 가능한 아이디입니다.";
	                resultSpan.style.color = "green";
	                submitBtn.disabled = false;
	            }
	        })
	        .catch(error => {
	            alert("서버 오류가 발생했습니다.");
	            console.error("❗️ fetch 오류:", error);
	            submitBtn.disabled = true;
	        });
	}

</script>
</html>