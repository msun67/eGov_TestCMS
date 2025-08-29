<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8" />
<title>QR 프린트 테스트</title>
    
<!-- ✅ Spring Security CSRF -->
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/common.css">
<link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/dashboard.css">
</head>

<body>
<%@ include file="/WEB-INF/jsp/include/topmenu.jsp" %>

<!-- layout-container  -->
<div class="layout-container">	
<%@ include file="/WEB-INF/jsp/include/main_leftmenu.jsp" %>
	<!-- main-content -->
	<div class="main-content">
		<sec:authorize access="hasRole('ROLE_ADMIN')">
		  <h1>QR 코드 출력 테스트</h1>
		  
		  <button type="button" id="printBtn">QR 코드 출력</button>  
		</sec:authorize>
	<!-- main-content -->
	</div>
<!-- layout-container  -->
</div>

<script>
    const btn = document.getElementById('printBtn');
    const csrfToken  = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    btn.addEventListener('click', async () => {
      btn.disabled = true;
      try {
        const resp = await fetch(
          '${pageContext.request.contextPath}/admin/print/qr.do',
          {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              [csrfHeader]: csrfToken   // ✅ CSRF 헤더에 토큰 실어 전송
            }
          }
        );

        if (!resp.ok) {
          alert('인쇄 요청 실패 (HTTP ' + resp.status + ')');
          return;
        }

        const data = await resp.json();
        alert(data.message || (data.ok ? '완료' : '실패'));
      } catch (err) {
        alert('인쇄 중 오류: ' + err);
      } finally {
        btn.disabled = false;
      }
    });
</script>
</body>
</html>
