<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<meta charset="UTF-8">
<title>회원 가입</title>
<!-- 공통스타일 (네비게이션 + 우측 영역) -->
<link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/common.css">
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
      color: #1f2937;
      margin-top: 5px;
    }
    input[type="text"], input[type="password"] {
      width: 100%;
      padding: 7px 14px;
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
	<c:if test="${not empty param.okMessage}">
		<div class="alert alert-success">
		  ${param.okMessage}
		</div>
	</c:if>
	<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
		<div class="alert alert-error">
		 ❌ ${SPRING_SECURITY_LAST_EXCEPTION}
		</div>
	</c:if>
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
		
		<label for="passwordChk">비밀번호 확인</label>
		<input type="password" id="passwordChk" required />
		<span id="passwordChkError" class="error-msg"></span>
		
		<label for="name">이름</label>
		<input type="text" name="name" id="name" required />
		<span id="nameError" class="error-msg"></span>
		
		<label for="phone">전화번호</label>
		<input type="text" name="phone" id="phone" required />
		<span id="phoneError" class="error-msg"></span>
		
		<label for="mobile">핸드폰번호</label>
		<input type="text" name="mobile" id="mobile" required />
		<span id="mobileError" class="error-msg"></span>
		
		<label for="detailAddr">주소</label>
		<div class="btn-row">
			<input type="text" id="zip" placeholder="우편번호" readonly />
			<button type="button" onclick="openPostcode()">주소검색</button>
		</div>
		<input type="text" id="roadAddr" placeholder="도로명주소" readonly />
		<input type="text" id="detailAddr" placeholder="상세주소 입력" />
		<span id="addressError" class="error-msg"></span>
		
		<input type="hidden" name="address" id="address" />
		
		<button id="submitBtn" type="submit" class="submit-btn">가입</button>
	</form>
</div>
<c:if test="${not empty errorMsg}">
	<p style="color: red;">${errorMsg}</p>
</c:if>


<!-- 다음주소검색API -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
let idChecked = false;

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
	document.getElementById("detailAddr").addEventListener("blur", () => {
	  const road = document.getElementById("roadAddr").value.trim();
	  const detail = document.getElementById("detailAddr").value.trim();
	  const msg = document.getElementById("addressError");
	  if (!road) { msg.innerText = "주소검색으로 도로명주소를 입력해주세요."; return; }
	  if (!detail) { msg.innerText = ""; } 
	});

	// 주소 갱신
	function composeAddress(){
		  const zip    = document.getElementById("zip").value.trim();
		  const road   = document.getElementById("roadAddr").value.trim();
		  const detail = document.getElementById("detailAddr").value.trim();
		  const hidden = document.getElementById("address");
		  if (!zip || !road) { hidden.value = ""; return; }
		  hidden.value = detail ? `(${zip}) ${road} ${detail}` : `(${zip}) ${road}`;
		}

		// 다음 주소검색 완료 시
		function openPostcode() {
		  new daum.Postcode({
		    oncomplete: function(data) {
		      document.getElementById('zip').value      = data.zonecode || '';
		      document.getElementById('roadAddr').value = data.roadAddress || data.address || '';
		      document.getElementById('detailAddr').focus();
		      document.getElementById('addressError').innerText = '';
		      composeAddress(); // ✅ 바로 합쳐 넣기
		    }
		  }).open();
		}

		// 상세주소가 바뀔 때마다 합치기
		document.getElementById('detailAddr').addEventListener('input', composeAddress);
 	
	//회원가입 유효성 검사
	function validateForm(){
		
		const userId = document.getElementById("userId").value.trim();
		const password = document.getElementById("password").value.trim();
		const passwordChk = document.getElementById("passwordChk").value.trim();
		const name = document.getElementById("name").value.trim();
		const phone = document.getElementById("phone").value.trim();
		const mobile = document.getElementById("mobile").value.trim();
		
		const zip = document.getElementById("zip");
		const road = document.getElementById("roadAddr");
		const detail = document.getElementById("detailAddr");
		const finalAddr = document.getElementById("address"); // hidden
		
		let isValid = true;
		//에러 메시지 초기화
		document.querySelectorAll(".error-msg").forEach(el => el.innerText = "");
		
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
		
		// 비밀번호 일치 체크 
		if (password !== passwordChk) {
		  document.getElementById("passwordChkError").innerText = "비밀번호가 일치하지 않습니다.";
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
		return isValid;
	}
	
	// ① 비밀번호 길이 즉시 검증
	  document.getElementById("password").addEventListener("input", function(){
	    const v = this.value.trim();
	    const msg = document.getElementById("passwordError");
	    if (v.length < 6) {
	      msg.innerText = "비밀번호는 6자 이상이어야 합니다.";
	    } else {
	      msg.innerText = "";
	    }
	  });

	  // ② 비밀번호 확인 즉시 검증
	  document.getElementById("passwordChk").addEventListener("input", function(){
	    const a = document.getElementById("password").value.trim();
	    const b = this.value.trim();
	    const msg = document.getElementById("passwordChkError");
	    if (!b) { msg.innerText = ""; return; }
	    msg.innerText = (a === b) ? "" : "비밀번호가 일치하지 않습니다.";
	  });	
	
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
	    
	 	// 형식 먼저 확인
	    if (!/^[a-zA-Z0-9]{4,12}$/.test(userId)) {
	      resultSpan.innerText = "영문자+숫자 4~12자";
	      resultSpan.style.color = "red";
	      idChecked = false;
	      submitBtn.disabled = true;
	      return;
	    }

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
	                idChecked = false;
	                submitBtn.disabled = true;
	            } else {
	                resultSpan.innerText = "사용 가능한 아이디입니다.";
	                resultSpan.style.color = "green";
	                idChecked = true; 
	                submitBtn.disabled = false;
	            }
	        })
	        .catch(error => {
	            alert("서버 오류가 발생했습니다.");
	            console.error("❗️ fetch 오류:", error);
	            idChecked = false; 
	            submitBtn.disabled = true;
	        });
	}
	// 아이디 수정 시 중복확인 무효화
	document.getElementById("userId").addEventListener("input", () => {
	  idChecked = false;
	  const submitBtn = document.getElementById("submitBtn");
	  submitBtn.disabled = true;
	  const resultSpan = document.getElementById("idCheckResult");
	  resultSpan.innerText = "";
	}); 
	
	// 폼 제출 훅 : 주소합치기 + 모든 유효성 한 번 더 검증
	const form = document.querySelector('form[action$="signupProcess.do"]');
	form.addEventListener('submit', function(e){
	  let ok = true;
	
	  // 1) 아이디 형식
	  const userId = document.getElementById("userId").value.trim();
	  if (!/^[a-zA-Z0-9]{4,12}$/.test(userId)) {
	    document.getElementById("idCheckResult").innerText = "영문자 + 숫자 4~12자여야 합니다.";
	    ok = false;
	  }
	
	  // 2) 아이디 중복확인 완료 여부
	  if (!idChecked) {
	    document.getElementById("idCheckResult").innerText = "아이디 중복확인을 해주세요.";
	    ok = false;
	  }
	
	  // 3) 숫자 필드
	  const numberRegex = /^[0-9]+$/;
	  if (!numberRegex.test(document.getElementById("phone").value.trim())) {
	    document.getElementById("phoneError").innerText = "전화번호는 숫자만 입력해주세요."; ok = false;
	  }
	  if (!numberRegex.test(document.getElementById("mobile").value.trim())) {
	    document.getElementById("mobileError").innerText = "핸드폰번호는 숫자만 입력해주세요."; ok = false;
	  }
	  if (!document.getElementById("name").value.trim()) {
	    document.getElementById("nameError").innerText = "이름을 입력해주세요."; ok = false;
	  }
	
	  // 4) 주소 합치기
	  const zip    = document.getElementById("zip").value.trim();
	  const road   = document.getElementById("roadAddr").value.trim();
	  const detail = document.getElementById("detailAddr").value.trim();
	  const hidden = document.getElementById("address");
	  
	  if (!zip || !road) {
		    document.getElementById("addressError").innerText =
		      "주소검색으로 우편번호/도로명주소를 입력해주세요.";
		    ok = false;
		  } else {
		    document.getElementById("addressError").innerText = "";
		    // ✅ 여기서 '무조건' 최종 주소를 다시 세팅 (composeAddress()에 의존 X)
		    const full = detail ? "(" + zip + ") " + road + " " + detail : "(" + zip + ") " + road;
		    hidden.value = full;
		  }

		  // 디버깅: 실제 전송될 최종 값 눈으로 확인
		  console.log('[SIGNUP] zip=', zip, ', road=', road, ', detail=', detail,
		              ', final address(hidden)=', hidden.value);

		  if (!ok) { e.preventDefault(); return false; }
		});

</script>
</html>