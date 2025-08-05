<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 상세</title>
    <link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/common.css">
    <link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/detail.css">
</head>
<body>
<!-- layout-container  -->
<div class="layout-container">

    <%@ include file="/WEB-INF/jsp/include/leftmenu.jsp" %>    
    
    <div class="main-content">
        <div class="board-detail-container">
        
        	<sec:authentication property="principal.userUuid" var="loginUuid"/>        	
        
            <!-- 제목 -->
            <h3 class="board-title">${board.boardTitle}</h3>
            
            <!-- 작성정보 -->
            <div class="board-info">
                <span>작성자: ${board.userUuid}</span>
                <span>작성일: ${board.createdAt}</span>
                <span>조회수: ${board.viewCnt}</span>
            </div>

            <!-- 본문 내용 (이미지/텍스트) -->
            <div class="board-content">
                <!-- 임시 이미지 영역 (이미지 있을 경우 출력) -->
                <div class="content-image">
                    이미지입니다.
                </div>

                <!-- 본문 텍스트 -->
                <div class="content-text">
                   <c:out value="${board.boardContent}" escapeXml="false" />
                </div>
            </div>

            <!-- 첨부파일 영역 -->
            <c:if test="${not empty fileList}">
			    <div class="board-attachments">
			        <ul class="file_down">
			            <c:forEach var="file" items="${fileList}">
			                <li>
			                    <a href="/demo_cms/file/download.do?fileId=${file.fileId}">
			                       💾  ${file.originalName} 
									(<c:choose>
								        <c:when test="${file.fileSize != null}">
								            <fmt:formatNumber value="${file.fileSize / 1024}" pattern="#,##0" /> KB
								        </c:when>
								        <c:otherwise>0 KB</c:otherwise>
								    </c:choose>)
			                    </a>
			                    <div class="inner_btn"><a href="/demo_cms/file/download.do?fileId=${file.fileId}">다운로드</a></div>
			                </li>
			            </c:forEach>
			        </ul>
			    </div>
			</c:if>

            <!-- 이전/다음글 -->
            <table class="nav-table">
                <tr>
                    <th>▴다음글</th>
                    <td>
                        <c:choose>
                            <c:when test="${not empty nextPost}">
                                <a href="detail.do?boardId=${nextPost.boardId}&boardCode=${boardCode}">
                                    ${nextPost.boardTitle}
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a>다음글이 없습니다.</a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th>▾이전글</th>
                    <td>
                        <c:choose>
                            <c:when test="${not empty prevPost}">
                                <a href="detail.do?boardId=${prevPost.boardId}&boardCode=${boardCode}">
                                    ${prevPost.boardTitle}
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a>이전글이 없습니다.</a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>

            <!-- 버튼 영역 -->
            <div class="btn-group">
				  <!-- 1) 관리자: 항상 표시 -->
				  <sec:authorize access="hasRole('ROLE_ADMIN')">
				    <a href="<c:url value='/edit.do'>
				               <c:param name='boardId' value='${board.boardId}'/>
				               <c:param name='boardCode' value='${boardCode}'/>
				             </c:url>" class="btn btn-blue">수정</a>
				
				    <form action="<c:url value='/delete.do'/>" method="post" style="display:inline;">
				      <sec:csrfInput/>
				      <input type="hidden" name="boardId" value="${board.boardId}"/>
				      <input type="hidden" name="boardCode" value="${boardCode}"/>
				      <button type="submit" class="btn btn-black"
				              onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
				    </form>
				  </sec:authorize>
				
				  <!-- 2) 일반 로그인 사용자: 본인 글일 때만 표시 -->
				  <sec:authorize access="isAuthenticated() and !hasRole('ROLE_ADMIN')">
				    <c:if test="${loginUuid == board.userUuid}">
				      <a href="<c:url value='/edit.do'>
				                 <c:param name='boardId' value='${board.boardId}'/>
				                 <c:param name='boardCode' value='${boardCode}'/>
				               </c:url>" class="btn btn-blue">수정</a>
				
				      <form action="<c:url value='/delete.do'/>" method="post" style="display:inline;">
				        <sec:csrfInput/>
				        <input type="hidden" name="boardId" value="${board.boardId}"/>
				        <input type="hidden" name="boardCode" value="${boardCode}"/>
				        <button type="submit" class="btn btn-black"
				                onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
				      </form>
				    </c:if>
				  </sec:authorize>
				
				  <!-- 3) 목록으로 (항상 표시) -->
				  <a href="<c:url value='/board.do'><c:param name='boardCode' value='${boardCode}'/></c:url>"
				     class="btn btn-gray">목록으로</a>
            </div>

        </div><!-- board-detail-container -->
    </div><!-- main-content -->
</div><!-- layout-container  -->
</body>
</html>
