<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<head>

</head>

<div class="topmenu-container">
    <div class="topmenu-left">
        <a href="/demo_cms/dashboard.do" class="topmenu-logo">🏠︎</a>
    </div>
    <div class="topmenu-right">  
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
        <form action="<c:url value='/logout.do'/>" method="post" class="logout-form">
        	<sec:csrfInput/>
        	<button type="submit" class="topmenu-logout">로그아웃</button>
        </form>
    </div>
</div>