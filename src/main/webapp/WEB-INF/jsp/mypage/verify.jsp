<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>본인 확인</title>
<!-- 공통스타일 (네비게이션 + 우측 영역) -->
<link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/common.css">
<link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/dashboard.css">
<style>
  .verify-wrap{max-width:460px;margin:60px auto;background:#fff;border:1px solid #e5e7eb;border-radius:12px;padding:22px}
  h1{margin:0 0 12px}
  .muted{color:#6b7280;margin-bottom:14px}
  label{display:block;font-weight:600;margin:8px 0 6px}
  input[type=password]{width:95%;padding:10px 12px;border:1px solid #e5e7eb;border-radius:10px}
  .btns{display:flex;gap:8px;justify-content:flex-end;margin-top:14px}
  .btn{border: 1px solid #e5e7eb;background: #fff;padding: 8px 12px;border-radius: 10px;cursor: pointer;text-decoration:none;font-size:14px;color:#374151;}
  .btn.primary{background:#2563eb;border-color:#2563eb;color:#fff}
</style>
</head>
<body>
<%@ include file="/WEB-INF/jsp/include/topmenu.jsp" %>

<!-- layout-container  -->
<div class="layout-container">	
	<%@ include file="/WEB-INF/jsp/include/main_leftmenu.jsp" %>
	<!-- main-content -->
   	<div class="main-content">
		<div class="verify-wrap">
		  <h1>본인 확인</h1>
		  <p class="muted">내 정보에 접근하기 전에 현재 비밀번호를 한번 더 입력해주세요.</p>
		
		  <c:if test="${not empty requestScope.errorMessage}">
		    <div class="alert alert-error">${requestScope.errorMessage}</div>
		  </c:if>
		
		  <form method="post" action="${pageContext.request.contextPath}/mypage/verify.do">
		    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		    <input type="hidden" name="returnUrl" value="${empty returnUrl ? '/mypage/view.do' : returnUrl}"/>
		
		    <label for="currentPassword">현재 비밀번호</label>
		    <input type="password" id="currentPassword" name="currentPassword" required/>
		
		    <div class="btns">
		      <a class="btn" href="${pageContext.request.contextPath}/dashboard.do">취소</a>
		      <button type="submit" class="btn primary">확인</button>
		    </div>
		  </form>
		</div>
	</div>
</div>
<script>document.getElementById('currentPassword').focus();</script>
</body>
</html>
