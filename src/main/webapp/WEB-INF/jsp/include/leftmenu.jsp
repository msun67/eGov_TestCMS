<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<aside class="side-menu">
    <div class="side-menu-header">게시판 목록</div>
    <ul>
        <c:forEach var="bm" items="${boardMasterList}">
            <li class="${bm.boardCode == boardCode ? 'active' : ''}">
                <a href="/demo_cms/board.do?boardCode=${bm.boardCode}">
                    ${bm.boardName}
                </a>
            </li>
        </c:forEach>
    </ul>
</aside>