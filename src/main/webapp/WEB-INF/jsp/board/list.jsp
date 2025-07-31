<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시판 목록</title>
    <!-- 공통스타일 (네비게이션 + 우측 영역) -->
    <link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/common.css">
    <!-- 게시판 리스트 전용 -->
    <link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/list.css">
</head>
<body>

<!-- layout-container  -->
<div class="layout-container">
	<!-- 네비게이션 바 include--!>
	<%@ include file="/WEB-INF/jsp/include/leftmenu.jsp" %>

    <!-- main-content -->
    <div class="main-content">
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

        
        <h1>게시판 목록</h1>
        <a href="/demo_cms/write.do?boardCode=${boardCode}">글쓰기</a>
        <a href="<c:url value='/logout.do'/>">로그아웃</a>
        <!--<a class="link" style="color:#898989;" href="/demo_cms/board.do">게시판 목록 보기</a>
         <a href="/demo_cms/main.do">메인으로</a> -->
              
        <!-- 검색 폼 -->
        <form method="get" action="/demo_cms/board.do">
            <input type="hidden" name="boardCode" value="${boardCode}" />
            <select name="condition">
                <option value="all" <c:if test="${searchVO.condition == 'all'}">selected</c:if>>전체 검색</option>
                <option value="title" <c:if test="${searchVO.condition == 'title'}">selected</c:if>>제목</option>
                <option value="content" <c:if test="${searchVO.condition == 'content'}">selected</c:if>>내용</option>
            </select>
            <input type="text" name="keyword" value="${searchVO.keyword}" placeholder="키워드를 입력해 주세요." />
            <button type="submit">검색</button>
        </form>

        <!-- 게시글 목록 테이블 -->
        <table>
	        <colgroup>
				<col width="8%;">
				<col width="50%;">
				<col class="15%;">
				<col class="15%;">
				<col class="12%;">
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
