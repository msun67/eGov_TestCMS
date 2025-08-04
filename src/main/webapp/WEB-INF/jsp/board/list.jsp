<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시판 목록</title>
    <!-- 공통스타일 (네비게이션 + 우측 영역) -->
    <link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/common.css">
    <!-- 게시판 리스트 전용 -->
    <link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/list.css">
    <!-- 대시보드용 상단문구 디자인 -->
    <link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/dashboard.css">
</head>
<body>

<!-- layout-container  -->
<div class="layout-container">	
	<%@ include file="/WEB-INF/jsp/include/leftmenu.jsp" %>
	
    <!-- main-content -->
    <div class="main-content">
    
		<%-- ✅ 헤더에서 사용할 보드명 계산 (먼저 정의) --%>
	    <c:set var="boardName" value="전체 글 목록"/>
	    <c:forEach var="bm" items="${boardMasterList}">
	      <c:if test="${bm.boardCode == boardCode}">
	        <c:set var="boardName" value="${bm.boardName}"/>
	      </c:if>
	    </c:forEach>
	    
    	<div class="page-header">
			<h1><c:out value="${boardName}"/></h1>
		      <div class="page-sub">
		        <div class="page-sub-left">
		          <span class="badge success">
		              <sec:authorize access="hasRole('ROLE_ADMIN')"> 관리자 </sec:authorize>
		              <sec:authorize access="hasRole('ROLE_USER')"> 사용자 </sec:authorize>
		              <sec:authorize access="hasRole('ROLE_ORG')"> 부서원 </sec:authorize>
		              </span>
		          <span class="welcome">
		            환영합니다,
		            <c:choose>
		              <c:when test="${not empty displayName}">${displayName}</c:when>
		              <c:otherwise><sec:authentication property="principal.username"/></c:otherwise>
		            </c:choose>님👋		           
		          </span>
		        </div>
		        <div class="page-sub-right">
		          <form action="<c:url value='/logout.do'/>" method="post" class="logout-form">
		            <sec:csrfInput/>
		            <button type="submit" class="btn small">로그아웃</button>
		          </form>
		        </div>
		      </div>
		    </div>
	    
    	<!-- ✅ 게시판 수정/삭제 후 메시지 표시 영역 -->
	        <c:if test="${not empty okMessage}">
			    <div class="alert alert-success">${okMessage}</div>
			</c:if>			
			<c:if test="${not empty errorMessage}">
			    <div class="alert alert-error">${errorMessage}</div>
			</c:if>			
			<c:if test="${not empty warningMessage}">
			    <div class="alert alert-warning">${warningMessage}</div>
			</c:if>

      
        <!-- 글쓰기, 검색 폼 -->
        <div class="board-actions">
        
	        <div class="board-toolbar">
		        <c:if test="${not empty boardCode}">
					<c:choose>
				      <%-- 공지사항: 관리자만 글쓰기 노출 --%>
				      <c:when test="${boardCode eq 'notice'}">
				        <sec:authorize access="hasRole('ROLE_ADMIN')">
				          <a href="<c:url value='/write.do'><c:param name='boardCode' value='${boardCode}'/></c:url>"
				             class="btn primary">글쓰기</a>
				        </sec:authorize>
				      </c:when>
				
				      <%-- 그 외 게시판: 로그인 사용자에게 노출 --%>
				      <c:otherwise>
				        <sec:authorize access="isAuthenticated()">
				          <a href="<c:url value='/write.do'><c:param name='boardCode' value='${boardCode}'/></c:url>"
				             class="btn primary">글쓰기</a>
				        </sec:authorize>
				      </c:otherwise>
				    </c:choose>
				 </c:if>
			 </div>
		       	
	        <form method="get" action="<c:url value='/board.do'/>" class="board-actions-form">
	            <input type="hidden" name="boardCode" value="${boardCode}" />
	            <select name="condition">
	                <option value="all" <c:if test="${searchVO.condition == 'all'}">selected</c:if>>전체 검색</option>
	                <option value="title" <c:if test="${searchVO.condition == 'title'}">selected</c:if>>제목</option>
	                <option value="content" <c:if test="${searchVO.condition == 'content'}">selected</c:if>>내용</option>
	            </select>
	            <input type="text" name="keyword" value="${searchVO.keyword}" placeholder="키워드를 입력해 주세요." />
	            <button type="submit">검색</button>        
	        </form>
        </div>

        <!-- 게시글 목록 테이블 -->
        <div class="table-wrap">
	        <table class="table">
		        <colgroup>
					<col style="width: 80px">    <!-- 번호 -->
					<col>                        <!-- 제목: 자동 확장 -->
					<col style="width: 180px">   <!-- 작성자 -->
					<col style="width: 140px">   <!-- 게시일 -->
					<col style="width: 100px">   <!-- 조회수 -->
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
	                <c:choose>
	                    <c:when test="${not empty boardList}">
	                        <c:forEach var="board" items="${boardList}" varStatus="status">
	                            <tr>
	                                <td>${totalCnt - ((searchVO.page - 1) * searchVO.size + status.index)}</td>
	                                <td style="text-align:left;">
	                                    <a href="/demo_cms/detail.do?boardId=${board.boardId}&boardCode=${searchVO.boardCode}">
	                                        ${board.boardTitle}
	                                    </a>
	                                </td>
	                                <td>${board.userUuid}</td>
	                                <td>${fn:substring(board.createdAt, 0, 10)}</td>
	                                <td>${board.viewCnt}</td>
	                            </tr>
	                        </c:forEach>
	                    </c:when>
	                    <c:otherwise>
	                        <tr>
	                            <td colspan="5" class="no-post-message">등록된 게시글이 없습니다.</td>
	                        </tr>
	                    </c:otherwise>
	                </c:choose>
	            </tbody>
	        </table>
        </div>

        <!-- 페이징 -->
        <c:if test="${totalCnt > 0}">
		    <div class="pagination">
		        <ul>
		            <!-- 처음 / 이전 -->
		            <c:if test="${page > 1}">
		                <li><a href="?page=1&boardCode=${boardCode}&condition=${param.condition}&keyword=${param.keyword}">&laquo;</a></li>
		                <li><a href="?page=${page - 1}&boardCode=${boardCode}&condition=${param.condition}&keyword=${param.keyword}">&lsaquo;</a></li>
		            </c:if>
		
		            <!-- 페이지 번호 -->
		            <c:forEach begin="1" end="${totalPages}" var="i">
		                <li>
		                    <a href="?page=${i}&boardCode=${boardCode}&condition=${param.condition}&keyword=${param.keyword}"
		                       class="${i == page ? 'active' : ''}">${i}</a>
		                </li>
		            </c:forEach>
		
		            <!-- 다음 / 마지막 -->
		            <c:if test="${page < totalPages}">
		                <li><a href="?page=${page + 1}&boardCode=${boardCode}&condition=${param.condition}&keyword=${param.keyword}">&rsaquo;</a></li>
		                <li><a href="?page=${totalPages}&boardCode=${boardCode}&condition=${param.condition}&keyword=${param.keyword}">&raquo;</a></li>
		            </c:if>
		        </ul>
		    </div>
		</c:if>

    </div><!-- main-content -->
</div><!-- layout-container  -->
</body>
</html>
