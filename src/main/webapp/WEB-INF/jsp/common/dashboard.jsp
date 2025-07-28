<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>공통 대시보드</h1>

<c:choose>
    <c:when test="${userType == 0}">
        <p>👑 관리자님 환영합니다.</p>
        <a href="/demo_cms/admin/board.do">관리자 게시판</a>
    </c:when>
    <c:when test="${userType == 1}">
        <p>🙋‍♂️ 사용자님 환영합니다.</p>
        <a href="/demo_cms/user/board.do">사용자 게시판</a>
    </c:when>
    <c:when test="${userType == 2}">
        <p>👨‍💼 부서원님 환영합니다.</p>
        <a href="/demo_cms/org/board.do">부서 게시판</a>
    </c:when>
</c:choose>
</body>
</html>