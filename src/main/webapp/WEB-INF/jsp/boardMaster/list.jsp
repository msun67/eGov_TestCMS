<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>게시판 목록</title>
    <style>
        table {
            border-collapse: collapse;
            text-aling : center;
            width: 80%;
        }
        th, td {
            border: 1px solid #aaa;
            padding: 10px;
        }
        th {
            background-color: #f5f5f5;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/common.css">
</head>
<body>

<!-- 수정 완료 메시지 출력 -->
<c:if test="${not empty message}">
    <script>
        alert("${message}");
    </script>
    <c:remove var="message" scope="session" />
</c:if>


<h2>📋 게시판 목록</h2>

<table>
    <thead>
        <tr>
            <th>게시판 코드</th>
            <th>게시판 이름</th>
            <th>설명</th>
            <th>생성일</th>
            <th>관리</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="board" items="${boardList}">
            <tr>
                <td>${board.boardCode}</td>
                <td>${board.boardName}</td>
                <td>${board.description}</td>
                <td>${board.createdAt}</td>
                <td>
                    <a href="/demo_cms/board.do?boardCode=${board.boardCode}">게시글 보기</a> |
                    <a href="/demo_cms/boardMaster/edit.do?boardCode=${board.boardCode}">수정</a>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty boardList}">
            <tr>
                <td colspan="5" style="text-align:center;">등록된 게시판이 없습니다.</td>
            </tr>
        </c:if>
    </tbody>
</table>

<br>
<a href="/demo_cms/boardMaster/create.do">← 게시판 생성하기</a><br>
<a href="/demo_cms/main.do" class="back-link">← 메인으로 돌아가기</a>

</body>
</html>
