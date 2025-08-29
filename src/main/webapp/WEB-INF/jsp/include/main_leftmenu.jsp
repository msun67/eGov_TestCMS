<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/common.css">
<link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/dashboard.css">
</head>

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
	           <a href="<c:url value='/posts.do'/>">📚 내가 쓴 글</a>
	        </li>
	    </ul>
	
	    <!-- 관리자 전용 메뉴 -->
	    <sec:authorize access="hasRole('ROLE_ADMIN')">
	        <div class="menu-section">관리자 전용 메뉴</div>
	        <ul class="menu">
	            <li class="menu-item ${fn:contains(requestURI, '/admin/boardMaster/create.do') ? 'active' : ''}">
	                <a href="<c:url value='/admin/boardMaster/create.do'/>">👑 게시판 생성</a>
	            </li>
	            <li class="menu-item ${fn:contains(requestURI, '/admin/member/userList.do') ? 'active' : ''}">
	                <a href="<c:url value='/admin/member/userList.do'/>">🧑‍🤝‍🧑 사용자 관리</a>
	            </li>
	            <li class="menu-item ${fn:contains(requestURI, '/admin/print/qrprint.do') ? 'active' : ''}">
	                <a href="<c:url value='/admin/print/qrprint.do'/>">🔍 발권정보</a>
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


</html>