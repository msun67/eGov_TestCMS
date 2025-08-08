<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link rel="stylesheet" href="/demo_cms/css/cms/common.css">
<style>

table.view { width: 100%; border-collapse: collapse; margin-bottom: 18px; }
  table.view th, table.view td { border: 1px solid #ddd; padding: 10px; }
  table.view th { width: 180px; background: #f7f7f7; text-align: left; }
  form .row { display: flex; gap: 12px; }
  form .row > div { flex: 1; }
  label { display: block; font-weight: 600; margin: 12px 0 6px; }
  input[type=text], input[type=tel], textarea, input[type=password] {
    width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 6px;
  }
  textarea { min-height: 90px; }
  .btns { display: flex; gap: 10px; margin-top: 16px; }
  .btn { padding: 10px 14px; border: 1px solid #aaa; background: #fafafa; border-radius: 6px; cursor: pointer; }
  .btn.primary { background: #2e6ef7; color: #fff; border-color: #2e6ef7; }
  .right { text-align: right; }
  .muted { color: #666; }
  /* 모달 */
  .modal-backdrop { display:none; position:fixed; inset:0; background:rgba(0,0,0,0.4); }
  .modal { display:none; position:fixed; left:50%; top:50%; transform:translate(-50%,-50%); background:#fff; width: 420px; max-width: 92%; border-radius:10px; box-shadow: 0 10px 30px rgba(0,0,0,0.25); }
  .modal .head { padding:14px 16px; border-bottom:1px solid #eee; font-weight:700; }
  .modal .body { padding:16px; }
  .modal .foot { padding:12px 16px; border-top:1px solid #eee; display:flex; justify-content:flex-end; gap:8px; }

</style>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/include/topmenu.jsp" %>
	<!-- layout-container  -->
	<div class="layout-container">	
		<%@ include file="/WEB-INF/jsp/include/leftmenu.jsp" %>
		<!-- main-content -->
    	<div class="main-content">
    	
    		<!-- 알림메시지 출력 -->
    		<c:if test="${not empty requestScope.okMessage}">
				<div class="alert alert-success">${requestScope.okMessage}</div>
			</c:if>
			<c:if test="${not empty requestScope.errorMessage}">
				<div class="alert alert-error">${requestScope.errorMessage}</div>
			</c:if>
			
			 <!-- 읽기 전용 정보 -->
			<table class="view">
			    <tr>
			      <th>아이디</th>
			      <td>${me.userId}</td>
			    </tr>
			    <tr>
			      <th>UUID</th>
			      <td class="muted">${me.userUuid}</td>
			    </tr>
			    <tr>
			      <th>회원 유형</th>
			      <td>
			        <c:choose>
			          <c:when test="${me.userType == 0}">관리자</c:when>
			          <c:when test="${me.userType == 1}">사용자</c:when>
			          <c:otherwise>조직원</c:otherwise>
			        </c:choose>
			      </td>
			    </tr>
			    <tr>
			      <th>비밀번호</th>
			      <td>
			        ****** 
			        <button type="button" class="btn" id="openPwModalBtn">변경</button>
			      </td>
			    </tr>
			    <tr>
			      <th>가입일</th>
			      <td>${me.signupDate}</td>
			    </tr>
			  </table>
			
			  <!-- 수정 폼: name / phone / mobile / address / residence -->
			  <form method="post" action="${pageContext.request.contextPath}/mypage/update.do" id="mypageForm">
			    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			
			    <div class="row">
			      <div>
			        <label for="name">이름</label>
			        <input type="text" id="name" name="name" value="${me.name}" required maxlength="100"/>
			      </div>
			      <div>
			        <label for="phone">전화</label>
			        <input type="tel" id="phone" name="phone" value="${me.phone}" maxlength="20" placeholder="02-123-4567"/>
			      </div>
			    </div>
			
			    <div class="row">
			      <div>
			        <label for="mobile">휴대전화</label>
			        <input type="tel" id="mobile" name="mobile" value="${me.mobile}" maxlength="20" placeholder="010-1234-5678"/>
			      </div>
			      <div>
			        <label for="residence">거주지</label>
			        <input type="text" id="residence" name="residence" value="${me.residence}" maxlength="100"/>
			      </div>
			    </div>
			
			    <label for="address">주소</label>
			    <textarea id="address" name="address" maxlength="500">${me.address}</textarea>
			
			    <div class="btns right">
			      <button type="submit" class="btn primary">저장</button>
			      <a class="btn" href="${pageContext.request.contextPath}/mypage/view.do">새로고침</a>
			    </div>
			  </form>
			
			  <!-- 비밀번호 변경 모달 -->
			  <div class="modal-backdrop" id="pwBackdrop"></div>
			  <div class="modal" id="pwModal" role="dialog" aria-modal="true" aria-labelledby="pwModalTitle">
			    <div class="head" id="pwModalTitle">비밀번호 변경</div>
			    <div class="body">
			      <form method="post" action="${pageContext.request.contextPath}/mypage/password.do" id="pwForm">
			        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			
			        <label for="currentPassword">현재 비밀번호</label>
			        <input type="password" id="currentPassword" name="currentPassword" required/>
			
			        <label for="newPassword">새 비밀번호</label>
			        <input type="password" id="newPassword" name="newPassword" required/>
			
			        <label for="newPasswordConfirm">새 비밀번호 확인</label>
			        <input type="password" id="newPasswordConfirm" name="newPasswordConfirm" required/>
			      </form>
			    </div>
			    <div class="foot">
			      <button class="btn" type="button" id="closePwModalBtn">취소</button>
			      <button class="btn primary" type="button" id="submitPwBtn">변경</button>
			    </div>
			  </div>
			</div>
    	</div>	
	</div>

<script>
  // 비밀번호 모달 열기/닫기
  const openBtn = document.getElementById('openPwModalBtn');
  const closeBtn = document.getElementById('closePwModalBtn');
  const submitPwBtn = document.getElementById('submitPwBtn');
  const modal = document.getElementById('pwModal');
  const backdrop = document.getElementById('pwBackdrop');

  function openModal() {
    modal.style.display = 'block';
    backdrop.style.display = 'block';
    document.getElementById('currentPassword').focus();
  }
  function closeModal() {
    modal.style.display = 'none';
    backdrop.style.display = 'none';
    document.getElementById('pwForm').reset();
  }

  openBtn && openBtn.addEventListener('click', openModal);
  closeBtn && closeBtn.addEventListener('click', closeModal);
  backdrop && backdrop.addEventListener('click', closeModal);

  // 비밀번호 기본 클라이언트 검증
  function validatePw() {
    const cur = document.getElementById('currentPassword').value.trim();
    const npw = document.getElementById('newPassword').value.trim();
    const rep = document.getElementById('newPasswordConfirm').value.trim();
    if (!cur || !npw || !rep) {
      alert('모든 항목을 입력해주세요.');
      return false;
    }
    if (npw !== rep) {
      alert('새 비밀번호가 일치하지 않습니다.');
      return false;
    }
    if (npw.length < 8) {
      alert('새 비밀번호는 최소 8자 이상이어야 합니다.');
      return false;
    }
    return true;
  }

  submitPwBtn && submitPwBtn.addEventListener('click', function() {
    if (validatePw()) {
      document.getElementById('pwForm').submit();
    }
  });

  // 전화번호 포맷 가벼운 보정(선택)
  function normalizePhone(id) {
    const el = document.getElementById(id);
    if (!el) return;
    el.addEventListener('blur', function() {
      this.value = this.value.replace(/[^\d\-]/g, '').replace(/\-+/g, '-');
    });
  }
  normalizePhone('phone');
  normalizePhone('mobile');
</script>

</body>
</html>