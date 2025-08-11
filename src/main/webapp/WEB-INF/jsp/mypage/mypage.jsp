<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<title>내 정보</title>
<!-- 공통스타일 (네비게이션 + 우측 영역) -->
<link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/common.css">
<link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/dashboard.css">
<style>
  :root{--card:#fff; --muted:#6b7280; --bd:#e5e7eb; --pri:#2563eb; --ok:#16a34a; --err:#dc2626;
  }
  body{margin:0;background:var(--bg);font:system-ui,-apple-system,Segoe UI,Roboto,Arial}
  
  a{color:inherit;text-decoration:none}
  .wrap{max-width:1200px;margin:24px auto;padding:0 16px;display:grid;grid-template-columns:260px 1fr;gap:18px}
  .card{background:var(--card);border:1px solid var(--bd);border-radius:14px;box-shadow:0 2px 8px rgba(0,0,0,.04);margin-top: 20px;}
  .side{padding:16px}
  .side h2{margin:4px 8px 12px;font-size:14px;color:#111827}
  .nav{display:flex;flex-direction:column;gap:6px}
  .nav a{padding:10px 12px;border-radius:10px;color:#111827}
  .nav a.active{background:#eef2ff;color:#1d4ed8;font-weight:600;border:1px solid #dbeafe}
  .main{display:flex;flex-direction:column;gap:18px}
  .header{display:flex;align-items:center;justify-content:space-between;padding:16px 18px}
  .title{font-size:20px;font-weight:700}
  .sub{color:var(--muted);font-size:13px;margin-top:4px}
  .grid{display:grid;grid-template-columns:1fr 1fr;gap:18px;padding:18px}
  .section{padding:18px;}
  .kv{display:grid;grid-template-columns:160px 1fr;gap:10px;padding:10px 0;border-bottom:1px dashed var(--bd)}
  .kv:last-child{border-bottom:0}
  .key{color:var(--muted); font-weight: 600;}
  .val{color:#111827}
  .chip{display:inline-flex;align-items:center;gap:6px;font-size:12px;padding:4px 8px;border-radius:999px;background:var(--chip-bg);color:var(--chip);border:1px solid #a7f3d0}
  .btn{display:inline-flex;align-items:center;gap:6px;padding:10px 14px;border-radius:10px;border:1px solid var(--bd);background:#fff;cursor:pointer}
  .btn.primary{background:var(--pri);color:#fff;border-color:var(--pri)}
  .btn.ghost{border: 1px solid #e5e7eb; background: #fff; padding: 3px 10px; border-radius: 999px; cursor: pointer; text-decoration: none; margin-left:5px;font-size: 13px;}
  .btn.ghost:hover{background: #f9fafb;}
  label{display:block;font-weight:600;margin:12px 0 6px}
  input[type=text],input[type=tel],input[type=password],textarea{
    width:100%;padding:10px 12px;border:1px solid var(--bd);border-radius:10px;background:#fff;box-sizing: border-box;
  }
  textarea{min-height:96px;resize:vertical}
  .row{display:grid;grid-template-columns:1fr 1fr;gap:12px}
  .right{display:flex;justify-content:flex-end;gap:8px;margin-top:12px}
  /* modal */
  .backdrop{display:none;position:fixed;inset:0;background:rgba(0,0,0,.35)}
  .modal{display:none;position:fixed;left:50%;top:50%;transform:translate(-50%,-50%);background:#fff;width:460px;max-width:92%;border-radius:12px;border:1px solid var(--bd);box-shadow:0 20px 60px rgba(0,0,0,.25)}
  .modal .head{padding:14px 16px;border-bottom:1px solid var(--bd);font-weight:700}
  .modal .body{padding:16px}
  .modal .foot{padding:12px 16px;border-top:1px solid var(--bd);display:flex;justify-content:flex-end;gap:8px;}
  .oktxt{color:#16a34a} .errtxt{color:#dc2626}
  .okbd{border-color:#16a34a !important} .errbd{border-color:#dc2626 !important}
  @media (max-width: 980px){ .grid{grid-template-columns:1fr} .wrap{grid-template-columns:1fr} }
</style>
</head>
<body>
<%@ include file="/WEB-INF/jsp/include/topmenu.jsp" %>

<!-- layout-container  -->
<div class="layout-container">	
	<!-- 좌측 사이드바 -->
    <aside class="sidebar">
        <div class="sidebar-title">공통 메뉴</div>
        <!-- 공통 메뉴 -->
	    <ul class="menu">
	        <li class="menu-item ${fn:contains(requestURI, '/dashboard.do') ? 'active' : ''}">
	            <a href="<c:url value='/dashboard.do'/>">🏠 대시보드</a>
	        </li>
	        <li class="menu-item ${fn:contains(requestURI, '/profile.do') ? 'active' : ''}">
	            <a href="<c:url value='/mypage/verify.do'/>">🙋‍♂️ 내 정보</a>
	        </li>
	        <li class="menu-item ${fn:contains(requestURI, '/board.do') ? 'active' : ''}">
	            <a href="<c:url value='/board.do'/>">📝 게시판</a>
	        </li>
	    </ul>
	
	    <!-- 관리자 전용 메뉴 -->
	    <sec:authorize access="hasRole('ROLE_ADMIN')">
	        <div class="menu-section">관리자 전용 메뉴</div>
	        <ul class="menu">
	            <li class="menu-item ${fn:contains(requestURI, '/admin/boardMaster/create.do') ? 'active' : ''}">
	                <a href="<c:url value='/admin/boardMaster/create.do'/>">👑 게시판 생성</a>
	            </li>
	            <li class="menu-item ${fn:contains(requestURI, '/admin/userList.do') ? 'active' : ''}">
	                <a href="<c:url value=''/>">🧑‍🤝‍🧑 사용자 관리</a>
	            </li>
	        </ul>
	    </sec:authorize>
	
	    <!-- 일반 사용자 메뉴 (필요 시 항목 추가) -->
	    <sec:authorize access="hasRole('ROLE_USER')">
	        <div class="menu-section">사용자 전용 메뉴</div>
	        <ul class="menu">
	            <li class="menu-item ${fn:contains(requestURI, '/my/posts.do') ? 'active' : ''}">
	                <a href="<c:url value=''/>">📚 내가 쓴 글</a>
	            </li>
	        </ul>
	    </sec:authorize>
	
	    <!-- 부서원 메뉴 -->
	    <sec:authorize access="hasRole('ROLE_ORG')">
	    	<div class="menu-section">부서원 전용 메뉴</div>
	        <ul class="menu">
	            <li class="menu-item ${fn:contains(requestURI, '/org/orglist.do') ? 'active' : ''}">
	                <a href="<c:url value=''/>">🏢 조직원 관리</a>
	            </li>
	        </ul>
	    </sec:authorize>
    </aside>
		<!-- main-content -->
    	<div class="main-content">
	    	<div class="mp">
			  <!-- 메인 -->
			  <section class="card main">
			    <div class="header">
			      <div>
			        <div class="title">내 정보</div>
			        <div class="sub">계정 정보 확인 및 수정</div>
			      </div>
			      <div>
			        <span class="badge success">
			          <sec:authorize access="hasRole('ROLE_ADMIN')">관리자</sec:authorize>
			          <sec:authorize access="hasRole('ROLE_USER')">사용자</sec:authorize>
			          <sec:authorize access="hasRole('ROLE_ORG')">조직원</sec:authorize>
			        </span>
			      </div>
			    </div>
			
			    <!-- 상단 메시지 -->
			    <c:if test="${not empty requestScope.okMessage}">
			      <div class="alert alert-success">${requestScope.okMessage}</div>
			    </c:if>
			    <c:if test="${not empty requestScope.errorMessage}">
			      <div class="alert alert-error">${requestScope.errorMessage}</div>
			    </c:if>
			
			    <!-- 상단 카드 2열: 좌=읽기, 우=수정 -->
			    <div class="grid">
			      <!-- 읽기 카드 -->
			      <div class="card">
			        <div class="section">
			          <div class="kv"><div class="key">아이디</div><div class="val">${me.userId}</div></div>
			          <div class="kv"><div class="key">UUID</div><div class="val" style="color:var(--muted)">${me.userUuid}</div></div>
			          <div class="kv">
			            <div class="key">회원 유형</div>
			            <div class="val">
			              <c:choose>
			                <c:when test="${me.userType == 0}">관리자</c:when>
			                <c:when test="${me.userType == 1}">사용자</c:when>
			                <c:otherwise>조직원</c:otherwise>
			              </c:choose>
			            </div>
			          </div>
			          <div class="kv">
			            <div class="key">비밀번호</div>
			            <div class="val">****** <button class="btn ghost" id="openPw">변경</button></div>
			          </div>
			          <div class="kv"><div class="key">가입일</div><div class="val">${me.signupDate}</div></div>
			        </div>
			      </div>
			
			      <!-- 수정 카드 -->
			      <div class="card">
			        <form method="post" action="${pageContext.request.contextPath}/mypage/update.do">
			          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			          <div class="section">
			            <div class="row">
			              <div>
			                <label for="name">이름</label>
			                <input type="text" id="name" name="name" value="${me.name}" maxlength="100" required/>
			              </div>
			              <div>
			                <label for="residence">거주지</label>
			                <input type="text" id="residence" name="residence" value="${me.residence}" maxlength="100"/>
			              </div>
			            </div>
			            <div class="row">
			              <div>
			                <label for="phone">전화</label>
			                <input type="tel" id="phone" name="phone" value="${me.phone}" maxlength="20" placeholder="02-123-4567"/>
			              </div>
			              <div>
			                <label for="mobile">휴대전화</label>
			                <input type="tel" id="mobile" name="mobile" value="${me.mobile}" maxlength="20" placeholder="010-1234-5678"/>
			              </div>
			            </div>
			            <label for="address">주소</label>
			            <textarea id="address" name="address" maxlength="500">${me.address}</textarea>
			            <div class="right">
			              <button type="submit" class="btn primary">변경 사항 저장</button>
			            </div>
			          </div>
			        </form>
			      </div>
			    </div>
			    <!-- 하단 기타 섹션 필요 시 추가 -->
			  </section>
		</div>
	</div>
</div>

<!-- 비밀번호 변경 모달 -->
<div class="backdrop" id="pwBackdrop"></div>
<div class="modal" id="pwModal" role="dialog" aria-modal="true" aria-labelledby="pwTitle">
  <div class="head" id="pwTitle">비밀번호 변경</div>
  <div class="body">
    <form method="post" action="${pageContext.request.contextPath}/mypage/password.do" id="pwForm">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      
      <label for="currentPassword">현재 비밀번호</label>
      <input type="password" id="currentPassword" name="currentPassword" required/>
      <small id="curPwMsg" style="display:block;margin-top:6px;"></small>
      
      <label for="newPassword">새 비밀번호</label>
      <input type="password" id="newPassword" name="newPassword" required/>
      
      <label for="newPasswordConfirm">새 비밀번호 확인</label>
      <input type="password" id="newPasswordConfirm" name="newPasswordConfirm" required/>
      <small id="matchMsg" style="display:block;margin-top:6px;"></small>      
    </form>
  </div>
  <div class="foot">
    <button class="btn" type="button" id="closePw">취소</button>
    <button class="btn primary" type="button" id="submitPw">변경</button>
  </div>
</div>

<script>
// modal open/close
const $ = (s)=>document.querySelector(s);
const openPw= $('#openPw'), modal=$('#pwModal'), bd=$('#pwBackdrop'),
		closePw=$('#closePw'), submitPw=$('#submitPw');
		
//pw validate & submit
const cur = $('#currentPassword'), npw = $('#newPassword'), rep = $('#newPasswordConfirm');
const curMsg = $('#curPwMsg'), matchMsg = $('#matchMsg');

let curPwTimer = null, curPwOK = false, matchOK = false;

function resetPwModal() {
	  // 타이머 해제
	  if (curPwTimer) { clearTimeout(curPwTimer); curPwTimer = null; }
	  // 폼 입력값 초기화
	  const form = $('#pwForm');
	  if (form) form.reset();

	  // 메시지/스타일 초기화
	  if (curMsg) { curMsg.textContent = ''; curMsg.classList.remove('oktxt','errtxt'); }
	  if (matchMsg) { matchMsg.textContent = ''; matchMsg.classList.remove('oktxt','errtxt'); }
	  [cur, npw, rep].forEach(el => el && el.classList.remove('okbd','errbd'));

	  // 상태 플래그 초기화
	  curPwOK = false;
	  matchOK = false;
}

function openModal(){
	resetPwModal();
	modal.style.display='block'; bd.style.display='block'; $('#currentPassword').focus(); 
}

function closeModal(){ 
	modal.style.display='none'; bd.style.display='none'; $('#pwForm').reset(); 
	resetPwModal();
}

openPw && openPw.addEventListener('click', openModal);
closePw && closePw.addEventListener('click', closeModal);
bd && bd.addEventListener('click', closeModal);

// CSRF
const csrfParam = "${_csrf.parameterName}";
const csrfToken = "${_csrf.token}";

function setState(el, msgEl, ok, okText, errText){
  el.classList.remove('okbd','errbd');
  msgEl.classList.remove('oktxt','errtxt');
  if(ok){
    el.classList.add('okbd'); msgEl.classList.add('oktxt'); msgEl.textContent = okText;
  }else{
    el.classList.add('errbd'); msgEl.classList.add('errtxt'); msgEl.textContent = errText;
  }
}

function checkMatch(){
  const a = npw.value.trim(), b = rep.value.trim();
  if(!a && !b){ matchMsg.textContent=''; npw.classList.remove('okbd','errbd'); rep.classList.remove('okbd','errbd'); matchOK=false; return; }
  matchOK = (a.length>=8 && a===b);
  setState(rep, matchMsg, matchOK, '새 비밀번호가 일치합니다.', (a.length<8?'8자 이상으로 입력하세요.':'새 비밀번호가 일치하지 않습니다.'));
}

async function checkCurrentPw(){
  const val = cur.value.trim();
  if(!val){ curMsg.textContent=''; cur.classList.remove('okbd','errbd'); curPwOK=false; return; }
  try{
    const form = new URLSearchParams();
    form.append('currentPassword', val);
    form.append(csrfParam, csrfToken);
    const res = await fetch('${pageContext.request.contextPath}/mypage/check-current.do', {
      method:'POST',
      headers:{'Content-Type':'application/x-www-form-urlencoded'},
      body: form.toString()
    });
    const json = await res.json();
    curPwOK = !!json.ok;
    setState(cur, curMsg, curPwOK, '현재 비밀번호가 확인되었습니다.', '현재 비밀번호가 일치하지 않습니다.');
  }catch(e){
    curPwOK=false;
    setState(cur, curMsg, false, '', '확인 중 오류가 발생했습니다.');
  }
}

cur.addEventListener('input', ()=>{ clearTimeout(curPwTimer); curPwTimer=setTimeout(checkCurrentPw, 350); });
npw.addEventListener('input', checkMatch);
rep.addEventListener('input', checkMatch);

submitPw.addEventListener('click', ()=>{
  if(!curPwOK){ alert('현재 비밀번호가 일치하지 않습니다.'); cur.focus(); return; }
  if(!matchOK){ alert('새 비밀번호가 일치하지 않거나 8자 미만입니다.'); rep.focus(); return; }
  $('#pwForm').submit();
});

 // simple phone cleanup
 ['#phone','#mobile'].forEach(id=>{
   const el=$(id); if(!el) return;
   el.addEventListener('blur', function(){ this.value=this.value.replace(/[^\d\-]/g,'').replace(/\-+/g,'-'); });
 });
</script>
</body>
</html>
