<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
					<a href="<c:url value=''/>">📚 내가 쓴 글</a>
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
						<a href="<c:url value=''/>">🧑‍🤝‍🧑 사용자 관리</a>
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

			<form method="get"
				action="<c:url value='/admin/member/userList.do'/>" class="toolbar">
				<select name="condition">
					<option value="all" ${searchVO.condition=='all' ? 'selected':''}>전체</option>
					<option value="id" ${searchVO.condition=='id' ? 'selected':''}>아이디</option>
					<option value="name" ${searchVO.condition=='name' ? 'selected':''}>이름</option>
					<option value="mobile"
						${searchVO.condition=='mobile' ? 'selected':''}>휴대폰</option>
				</select> <input type="text" name="keyword" value="${searchVO.keyword}"
					placeholder="검색어" /> <select name="userType">
					<option value="">권한 전체</option>
					<option value="0" ${searchVO.userType==0 ? 'selected':''}>관리자</option>
					<option value="1" ${searchVO.userType==1 ? 'selected':''}>사용자</option>
					<option value="2" ${searchVO.userType==2 ? 'selected':''}>조직원</option>
				</select>
				<button type="submit">검색</button>
			</form>

			<table class="table">
				<thead>
					<tr>
						<th>#</th>
						<th>아이디</th>
						<th>이름</th>
						<th>권한</th>
						<th>휴대폰</th>
						<th>가입일</th>
						<th>최근로그인</th>
						<th>로그인상태</th>
						<th>IP</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty memberList}">
							<c:forEach var="m" items="${memberList}" varStatus="st">
								<tr>
									<td>${totalCnt - ((page - 1) * pageSize + st.index)}</td>
									<td>${m.userId}</td>
									<td>${m.name}</td>
									<td><c:choose>
											<c:when test="${m.userType == 0}">관리자</c:when>
											<c:when test="${m.userType == 1}">사용자</c:when>
											<c:otherwise>조직원</c:otherwise>
										</c:choose></td>
									<td>${m.mobile}</td>
									<td>${m.signupDate}</td>
									<td>${m.lastLogin}</td>
									<td>${m.loginStatus}</td>
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
</body>
</html>
