<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>사용자 관리</title>
<link rel="stylesheet" type="text/css"
	href="/demo_cms/css/cms/common.css">
<link rel="stylesheet" type="text/css"
	href="/demo_cms/css/cms/dashboard.css">
	<link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/list.css">
</head>

<body>
	<%@ include file="/WEB-INF/jsp/include/topmenu.jsp"%>

	<!-- layout-container  -->
	<div class="layout-container">
		<!-- 좌측 사이드바 -->
		<aside class="sidebar">
			<div class="sidebar-title">공통 메뉴</div>
			<!-- 공통 메뉴 -->
			<ul class="menu">
				<li
					class="menu-item ${fn:contains(requestURI, '/dashboard.do') ? 'active' : ''}">
					<a href="<c:url value='/dashboard.do'/>">🏠 대시보드</a>
				</li>
				<li
					class="menu-item ${fn:contains(requestURI, '/profile.do') ? 'active' : ''}">
					<a href="<c:url value='/mypage/verify.do'/>">🙋‍♂️ 내 정보</a>
				</li>
				<li
					class="menu-item ${fn:contains(requestURI, '/board.do') ? 'active' : ''}">
					<a href="<c:url value='/board.do'/>">📝 게시판</a>
				</li>
				<li
					class="menu-item ${fn:contains(requestURI, '/posts.do') ? 'active' : ''}">
					<a href="<c:url value='/posts.do'/>">📚 내가 쓴 글</a>
				</li>
			</ul>

			<!-- 관리자 전용 메뉴 -->
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<div class="menu-section">관리자 전용 메뉴</div>
				<ul class="menu">
					<li
						class="menu-item ${fn:contains(requestURI, '/admin/boardMaster/create.do') ? 'active' : ''}">
						<a href="<c:url value='/admin/boardMaster/create.do'/>">👑 게시판
							생성</a>
					</li>
					<li
						class="menu-item ${fn:contains(requestURI, '/admin/userList.do') ? 'active' : ''}">
						<a href="<c:url value='/admin/member/userList.do'/>">🧑‍🤝‍🧑 사용자 관리</a>
					</li>
				</ul>
			</sec:authorize>

			<!-- 일반 사용자 메뉴 (필요 시 항목 추가) -->
			<sec:authorize access="hasRole('ROLE_USER')">
				<div class="menu-section">사용자 전용 메뉴</div>
				<ul class="menu">
					<li
						class="menu-item ${fn:contains(requestURI, '/my/posts.do') ? 'active' : ''}">
						<a href="<c:url value=''/>">📚 내가 쓴 글</a>
					</li>
				</ul>
			</sec:authorize>

			<!-- 부서원 메뉴 -->
			<sec:authorize access="hasRole('ROLE_ORG')">
				<div class="menu-section">부서원 전용 메뉴</div>
				<ul class="menu">
					<li
						class="menu-item ${fn:contains(requestURI, '/org/orglist.do') ? 'active' : ''}">
						<a href="<c:url value=''/>">🏢 조직원 관리</a>
					</li>
				</ul>
			</sec:authorize>
		</aside>
		<!-- main-content -->
		<div class="main-content">
			<h1>사용자 관리</h1>
        	
		<!-- 검색 폼 -->
        <div class="board-actions">
			<form method="get" action="<c:url value='/admin/member/userList.do'/>" class="board-actions-form">
				<select name="condition">
					<option value="all" ${searchVO.condition=='all' ? 'selected':''}>전체</option>
					<option value="id" ${searchVO.condition=='id' ? 'selected':''}>아이디</option>
					<option value="name" ${searchVO.condition=='name' ? 'selected':''}>이름</option>
					<option value="mobile"
						${searchVO.condition=='mobile' ? 'selected':''}>휴대폰</option>
				</select> 
				<input type="text" name="keyword" value="${searchVO.keyword}"
					placeholder="키워드를 입력해 주세요." /> <select name="userType">
					<option value="">권한 전체</option>
					<option value="0" ${searchVO.userType==0 ? 'selected':''}>관리자</option>
					<option value="1" ${searchVO.userType==1 ? 'selected':''}>사용자</option>
					<option value="2" ${searchVO.userType==2 ? 'selected':''}>조직원</option>
				</select>
				<button type="submit">검색</button>
			</form>
			
			<div class="board-toolbar">
		       <a class="btn-excel" href="<c:url value='/admin/member/userListExcel.do'>
			      <c:param name='condition' value='${param.condition}'/>
			      <c:param name='keyword'   value='${fn:escapeXml(param.keyword)}'/>
			      <c:param name='userType'  value='${param.userType}'/>
			    </c:url>">엑셀(xlsx)</a>
			
			  <a class="btn-excel" href="<c:url value='/admin/member/userListExcelXls.do'>
			      <c:param name='condition' value='${param.condition}'/>
			      <c:param name='keyword'   value='${fn:escapeXml(param.keyword)}'/>
			      <c:param name='userType'  value='${param.userType}'/>
			    </c:url>">엑셀(xls)</a>
			 </div>
		</div>

		 <!-- 회원 목록 테이블 -->
		 <!-- 1줄: 좌측 요약 -->
		<div class="board-summary">
		  총 <strong><fmt:formatNumber value="${totalCnt}" type="number"/></strong>건
		</div>
		 
        <div class="table-wrap">
			<table class="table" style="margin-top:5px;">
				<thead>
					<tr>
						<th style="width:50px">번호</th>
						<th>권한</th>
						<th>아이디</th>
						<th>이름</th>
						<th>휴대폰</th>
						<th>가입일</th>
						<th>최근로그인</th>
						<th>로그인상태</th>
						<th>접속IP</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty memberList}">
							<c:forEach var="m" items="${memberList}" varStatus="st">
								<tr>
									<td>${totalCnt - ((page - 1) * pageSize + st.index)}</td>									
									<td>
										<c:choose>
											<c:when test="${m.userType == 0}">관리자</c:when>
											<c:when test="${m.userType == 1}">사용자</c:when>
											<c:otherwise>조직원</c:otherwise>
										</c:choose>
									</td>
									<td>
										<a href="#" class="user-edit-link" data-uuid="${m.userUuid}" data-userno="${m.userNo}">
											${m.userId}
										</a>
									</td>
									<td>
										<c:choose>
										    <%-- 이름이 2글자일 때: 앞 1글자 + * --%>
										    <c:when test="${fn:length(m.name) == 2}">
										      ${fn:substring(m.name, 0, 1)}*
										    </c:when>
										
										    <%-- 이름이 3글자 이상일 때: 앞1글자 + 가운데*...* + 마지막1글자 --%>
										    <c:when test="${fn:length(m.name) > 2}">
										      ${fn:substring(m.name, 0, 1)}
										      <c:forEach var="i" begin="1" end="${fn:length(m.name)-2}">*</c:forEach>
										      ${fn:substring(m.name, fn:length(m.name)-1, fn:length(m.name))}
										    </c:when>
										    <%-- null/빈 문자열 --%>
										    <c:otherwise>-</c:otherwise>
									  	</c:choose>
									</td>
									<td>${m.mobile}</td>
									<td style="white-space: normal;">${m.signupDate}</td>
									<td style="white-space: normal;">${m.lastLogin}</td>
									<td>
									  <c:choose>
									    <c:when test="${m.loginStatus == 1}">로그인</c:when>
									    <c:when test="${m.loginStatus == 0}">로그아웃</c:when>
									    <c:otherwise>-</c:otherwise>
									  </c:choose>
									</td>
									<td>${m.loginIp}</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="9">등록된 회원이 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>

			<c:if test="${totalCnt > 0}">
				<div class="pagination">
					<ul>
						<c:if test="${page > 1}">
							<li><a
								href="?page=1&condition=${param.condition}&keyword=${param.keyword}&userType=${param.userType}">&laquo;</a></li>
							<li><a
								href="?page=${page-1}&condition=${param.condition}&keyword=${param.keyword}&userType=${param.userType}">&lsaquo;</a></li>
						</c:if>
						<c:forEach begin="1" end="${totalPages}" var="i">
							<li><a class="${i==page?'active':''}"
								href="?page=${i}&condition=${param.condition}&keyword=${param.keyword}&userType=${param.userType}">${i}</a></li>
						</c:forEach>
						<c:if test="${page < totalPages}">
							<li><a
								href="?page=${page+1}&condition=${param.condition}&keyword=${param.keyword}&userType=${param.userType}">&rsaquo;</a></li>
							<li><a
								href="?page=${totalPages}&condition=${param.condition}&keyword=${param.keyword}&userType=${param.userType}">&raquo;</a></li>
						</c:if>
					</ul>
				</div>
			</c:if>
		</div>
	</div>
	
	<!-- 회원정보 수정 패널 -->
	<div id="userDrawer" class="drawer">
	  <div class="drawer-header">
	    <h3>회원정보 수정</h3>
	    <button type="button" class="drawer-close" aria-label="닫기">×</button>
	  </div>
	
	  <form id="userEditForm" class="drawer-body">
	    <input type="hidden" name="userUuid" id="ue-userUuid">
	    <input type="hidden" name="userNo"   id="ue-userNo">
	    <!-- ✅ CSRF 토큰 추가 -->
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	
	    <div class="form-row">
	      <label>아이디</label>
	      <input type="text" id="ue-userId" readonly>
	    </div>
	
	    <div class="form-row">
	      <label>권한</label>
	      <select name="userType" id="ue-userType" required>
	        <option value="0">관리자</option>
	        <option value="1">사용자</option>
	        <option value="2">조직원</option>
	      </select>
	    </div>
	
	    <div class="form-row">
	      <label>새 비밀번호 (선택)</label>
	      <input type="password" name="newPassword" id="ue-newPassword" maxlength="100" placeholder="변경 시에만 입력">
	      <small class="hint">비워두면 비밀번호는 변경하지 않습니다.</small>
	    </div>
	
	    <div class="form-row">
	      <label>휴대전화</label>
	      <input type="tel" name="mobile" id="ue-mobile" maxlength="20" placeholder="010-1234-5678">
	    </div>
	
	    <div class="drawer-footer">
	      <button type="button" class="btn small" id="ue-cancel">취소</button>
	      <button type="submit" class="btn primary" id="ue-save">저장</button>
	    </div>
	  </form>
	</div>
	<div class="drawer-backdrop" id="userDrawerBackdrop"></div>
	
</body>
</html>

<script>
(function(){
  const drawer   = document.getElementById('userDrawer');
  const backdrop = document.getElementById('userDrawerBackdrop');
  const form     = document.getElementById('userEditForm');
  const closeBtn = drawer.querySelector('.drawer-close');
  const cancelBtn= document.getElementById('ue-cancel');

  // 필드
  const f = {
    userUuid: document.getElementById('ue-userUuid'),
    userNo:   document.getElementById('ue-userNo'),
    userId:   document.getElementById('ue-userId'),
    userType: document.getElementById('ue-userType'),
    newPw:    document.getElementById('ue-newPassword'),
    mobile:   document.getElementById('ue-mobile')
  };

  // 휴대전화 blur 보정 (기존 normalizePhone 로직 경량)
  f.mobile.addEventListener('blur', function(){
    this.value = this.value.replace(/[^\d\-]/g,'').replace(/\-+/g,'-');
  });

  function openDrawer(){ drawer.classList.add('open'); }
  function closeDrawer(){ drawer.classList.remove('open'); }

  closeBtn.addEventListener('click', closeDrawer);
  cancelBtn.addEventListener('click', closeDrawer);
  backdrop.addEventListener('click', closeDrawer);

  // 아이디 클릭 → 로드
  document.querySelectorAll('.user-edit-link').forEach(a=>{
    a.addEventListener('click', async (e)=>{
      e.preventDefault();
      const uuid = a.dataset.uuid, userNo = a.dataset.userno;
      if(!uuid) return;

      // 상세 로드
      const url = '<c:url value="/admin/member/userItem.do"/>' + '?userUuid=' + encodeURIComponent(uuid);
      const res = await fetch(url, {credentials:'same-origin'});
      if(!res.ok){ alert('사용자 정보를 불러오지 못했습니다.'); return; }
      const data = await res.json();

      // 채우기
      f.userUuid.value = data.userUuid || uuid;
      f.userNo.value   = data.userNo   || userNo || '';
      f.userId.value   = data.userId   || '';
      f.userType.value = (data.userType ?? 1);
      f.mobile.value   = data.mobile   || '';

      f.newPw.value = '';
      openDrawer();
    });
  });

  // 저장
  form.addEventListener('submit', async (e)=>{
    e.preventDefault();

    // 간단 검증
    const ut = Number(f.userType.value);
    if (![0,1,2].includes(ut)) { alert('권한 값이 올바르지 않습니다.'); return; }
    const pw = f.newPw.value.trim();
    if (pw && pw.length < 8) { alert('비밀번호는 8자 이상으로 입력하세요.'); return; }

    const fd = new FormData(form);
    const res = await fetch('<c:url value="/admin/member/userUpdate.do"/>', {
      method: 'POST',
      body: fd,
      credentials: 'same-origin'
    });

    if(!res.ok){
      const txt = await res.text();
      alert('저장 실패: ' + txt);
      return;
    }
    const json = await res.json();
    if(json && json.ok){
      alert('저장되었습니다.');
      closeDrawer();
      // 필요시 페이지 새로고침 / 특정 행만 업데이트
      location.reload();
    }else{
      alert('저장 실패');
    }
  });
})();
</script>
