<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date , java.text.SimpleDateFormat" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
  <style>
    * {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
      font-family: 'Inter', sans-serif;
    }
    body {
      background-color: #ffffff;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
      color: #111827;
    }
    .container {
      background-color: #f9fafb;
      padding: 40px;
      border-radius: 16px;
      border: 1px solid #e5e7eb;
      width: 100%;
      max-width: 400px;
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.05);
    }
    h1 {
      font-size: 24px;
      font-weight: 600;
      margin-bottom: 24px;
      text-align: center;
      color: #1e3a8a;
    }
    input[type="text"], input[type="password"] {
      width: 100%;
      padding: 12px;
      margin-bottom: 16px;
      border: 1px solid #d1d5db;
      border-radius: 8px;
      background-color: #ffffff;
      color: #111827;
    }
    input::placeholder {
      color: #9ca3af;
    }
    button {
      width: 100%;
      padding: 12px;
      border: none;
      border-radius: 8px;
      background-color: #3182f6;
      color: white;
      font-weight: 600;
      cursor: pointer;
      transition: background 0.3s ease;
    }
    button:hover {
      background-color: #2563eb;
    }
    .link {
      display: block;
      text-align: center;
      margin-top: 16px;
      color: #3b82f6;
      text-decoration: none;
      font-size: 14px;
    }
    .link:hover {
      text-decoration: underline;
    }
  </style>
<head>
<title>CMS메인화면</title>
<!-- 공통스타일 (네비게이션 + 우측 영역) -->
<link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/common.css">
</head>
<body>
<div class="container">
		
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
	
		<h1>로그인</h1>
		<form action="${pageContext.request.contextPath}/loginProcess.do" method="post">
			<!-- ✅ CSRF 토큰 추가 -->
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<label><input type="radio" name="userType" value="ROLE_ADMIN"> 관리자</label>
			<label><input type="radio" name="userType" value="ROLE_USER" checked> 일반 사용자</label>
  			<label><input type="radio" name="userType" value="ROLE_ORG"> 부서원</label>
			
			<input type="text" name="username" id="userId" placeholder="아이디" required/><br/>
			<input type="password" name="password" id="password" placeholder="비밀번호" required/><br/>
			
			<button type="submit">로그인</button>
			<!-- 회원가입 -->
			<a href="<c:url value='/signup.do'/>" class="link">회원가입 </a>
		</form>		

		<!-- 오류 메시지 출력
		<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
			<p style="color:red;">❌ ${SPRING_SECURITY_LAST_EXCEPTION}</p>
			<c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session"/>
		</c:if> -->
	</div>		
</body>
</html>