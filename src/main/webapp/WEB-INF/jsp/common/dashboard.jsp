<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>대시보드</title>
    <link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/common.css">
    <link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/dashboard.css">
</head>
<body>
<%@ include file="/WEB-INF/jsp/include/topmenu.jsp" %>

<c:set var="requestURI" value="${pageContext.request.requestURI}" />

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
	        <li class="menu-item ${fn:contains(requestURI, '/posts.do') ? 'active' : ''}">
	                <a href="<c:url value=''/>">📚 내가 쓴 글</a>
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
	            <li class="menu-item ${fn:contains(requestURI, '/user/posts.do') ? 'active' : ''}">
	                <a href="<c:url value=''/>">💬 1:1 상담문의</a>
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

    <!-- 우측 콘텐츠 -->
    <main class="main-content">
        <!-- 페이지 타이틀/알림 -->
        <div class="page-header">
            <h1>대시보드</h1>
                <div class="page-sub">
		        <!-- 좌측: 알림/환영문구 -->
		        <div class="page-sub-left">
		            <span class="badge success">로그인 성공!</span>
		            <span class="welcome">
		                환영합니다,
		                <sec:authentication property="principal.username" />님 👋
		                <span class="role">
		                    (권한:
		                    <sec:authorize access="hasRole('ROLE_ADMIN')">관리자</sec:authorize>
		                    <sec:authorize access="hasRole('ROLE_USER')">사용자</sec:authorize>
		                    <sec:authorize access="hasRole('ROLE_ORG')">부서원</sec:authorize>
		                    )
		                </span>
		            </span>
		        </div>		
				<!-- 우측: 로그아웃 버튼 -->
		        <div class="page-sub-right">
					<form action="<c:url value='/logout.do'/>" method="post" class="logout-form">
					    <sec:csrfInput/>
					    <button type="submit" class="btn small">로그아웃</button>
					</form>
		        </div>
		    </div>
		</div>

        <!-- 요약 카드 -->
        <section class="cards">
            <div class="card">
                <div class="card-title">전체 게시글</div>
                <div class="card-value">
                    <c:out value="${stats.totalPosts}" default="0"/>
                </div>
                <div class="card-foot">누적</div>
            </div>

            <div class="card">
                <div class="card-title">오늘 등록</div>
                <div class="card-value">
                    <c:out value="${stats.todayPosts}" default="0"/>
                </div>
                <div class="card-foot">
                    <fmt:formatDate value="${today}" pattern="yyyy-MM-dd"/>
                </div>
            </div>

            <div class="card">
                <div class="card-title">미답변 문의</div>
                <div class="card-value">
                    <c:out value="${stats.pendingQna}" default="0"/>
                </div>
                <div class="card-foot">질문답변 게시판</div>
            </div>

            <div class="card">
                <div class="card-title">공지 진행중</div>
                <div class="card-value">
                    <c:out value="${stats.activeNotices}" default="0"/>
                </div>
                <div class="card-foot">노출 상태</div>
            </div>
        </section>
        
        <!-- 최근 게시글 (boardRecent: List<BoardVO>) -->
        <section class="panel">
            <div class="panel-head">
                <h2>최근 게시글</h2>
                <a class="link" href="<c:url value='/board.do'/>">전체 보기</a>
            </div>

            <div class="table-wrap">
                <table class="table">
                    <colgroup>
                        <col style="width: 80px">
                        <col>
                        <col style="width: 180px">
                        <col style="width: 140px">
                        <col style="width: 100px">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>번호</th>
                        <th>제목</th>
                        <th>작성자</th>
                        <th>게시일</th>
                        <th>조회수</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="post" items="${boardRecent}">
                        <tr>
                            <td><c:out value="${post.boardId}"/></td>
                            <td class="title" style="text-align:left;">
                                <a href="<c:url value='/detail.do'>
                                           <c:param name='boardId' value='${post.boardId}'/>
                                           <c:param name='boardCode' value='${post.boardCode}'/>
                                         </c:url>">
                                    <c:out value="${post.boardTitle}"/>
                                </a>
                                <c:if test="${post.fileCount > 0}">
                                    <span class="file-badge">파일</span>
                                </c:if>
                            </td>
                            <td><c:out value="${post.userUuid}"/></td>
                            <td>
	                            <fmt:parseDate value="${post.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" var="ps"/>
	                            <fmt:formatDate value="${ps}" pattern="yyyy-MM-dd"/>
                            </td>
                            <td><c:out value="${post.viewCnt}"/></td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty boardRecent}">
                        <tr>
                            <td colspan="5" class="empty">최근 게시글이 없습니다.</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </section>

        <!-- 공지/안내 등 2열 패널 샘플 -->
        <section class="grid">
            <div class="panel">
                <div class="panel-head"><h2>공지사항</h2></div>
                <ul class="list">
                    <c:forEach var="n" items="${noticeList}">
                        <li>
                            <a href="<c:url value='/detail.do'>
                                      <c:param name='boardId' value='${n.boardId}'/>
                                      <c:param name='boardCode' value='${n.boardCode}'/>
                                    </c:url>">
                                <span class="dot"></span>
                                <span class="text"><c:out value="${n.boardTitle}"/></span>
                                <span class="date">
	                                <fmt:parseDate value="${n.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" var="pd"/>
									<fmt:formatDate value="${pd}" pattern="MM-dd"/>
                                </span>
                            </a>
                        </li>
                    </c:forEach>
                    <c:if test="${empty noticeList}">
                        <li class="empty">등록된 공지가 없습니다.</li>
                    </c:if>
                </ul>
            </div>

            <div class="panel">
                <div class="panel-head"><h2>빠른 링크</h2></div>
                <ul class="list">
                	<li>
	                    <a href="<c:url value=''/>">
		                    <span class="dot"></span>
		                    <span class="text">🏳️ 방문 통계</span>	                    
	                    </a>
	                    <a href="<c:url value=''/>">
		                    <span class="dot"></span>
		                    <span class="text">🏳️ 환경 설정</span>	
	                    </a>
                	</li>
                </div>
            </div>
        </section>

    </main>
</div>

<script>
    // 모바일에서 사이드바 토글이 필요하면 여기에 JS 추가
</script>
</body>
</html>
