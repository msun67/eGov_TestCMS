<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 수정</title>
<link rel="stylesheet" href="/demo_cms/css/cms/common.css">
<style>
.bm-container {
	max-width: 600px;
	margin: 40px auto;
}
/* 폼 */
.bm-form {
	background: #fff;
	border: 1px solid #ccc;
	border-radius: 10px;
	padding: 30px;
}

.bm-form h2 {
	margin-bottom: 24px;
}

.bm-field {
	margin-bottom: 20px;
}

.bm-field label {
	font-weight: bold;
	display: block;
	margin-bottom: 6px;
}

.bm-field input[type="text"], .bm-field textarea {
	width: 95%;
	padding: 10px;
	border: 1px solid #ddd;
	border-radius: 5px;
}
.bm-textarea {
	min-height: 110px;
	resize: vertical;
}
 .bm-field select{
 	width: 100%;
	padding: 10px;
	border: 1px solid #ddd;
	border-radius: 5px;
 }

.bm-checkbox-group label {
	margin-right: 12px;
	font-weight: 500;
}

.bm-actions {
	display: flex;
	justify-content: flex-end;
	gap: 8px;
}
.bm-help {
	font-size: 12px;
	color: #6b7280;
	margin-top: 4px;
}
/* 버튼 */
.bm-btn {
	border: 1px solid #e5e7eb;
	background: #fff;
	padding: 8px 12px;
	border-radius: 10px;
	cursor: pointer;
	text-decoration:none;
	font-size:14px;
}
.bm-btn:hover {
	background: #f9fafb;
}
.bm-btn--delete {
	background: #2563eb;
	color: #fff;
	border-color: #2563eb;
}

.bm-btn--delete:hover {
	background: #1d4ed8;
}
</style>
<script>
        function confirmDelete() {
            if (confirm("정말 삭제하시겠습니까? 삭제된 게시판은 복구할 수 없습니다.")) {
                document.getElementById("deleteForm").submit();
            }
        }
    </script>
</head>
<body>
	<sec:authorize access="hasRole('ROLE_ADMIN')">
		<%@ include file="/WEB-INF/jsp/include/topmenu.jsp"%>
		<!-- layout-container  -->
		<div class="layout-container">
			<%@ include file="/WEB-INF/jsp/include/leftmenu.jsp"%>
			<div class="main-content">

				<div class="bm-container">
					<form class="bm-form" action="<c:url value='/admin/boardMaster/update.do' />" method="post">
						<input type="hidden" name="boardCode" value="${boardMasterVO.boardCode}" />
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						<h2>✏️ 게시판 수정</h2>

						<div class="bm-field">
							<label>게시판 코드</label>
							<div style="font-weight:700; font-size:16px; color:#2563eb; margin-bottom:4px;">							
								${boardMasterVO.boardCode}
							</div>
						</div>

						<div class="bm-field">
							<label for="boardName">게시판 이름</label> <input type="text" id="boardName" name="boardName" value="${boardMasterVO.boardName}" required />
						</div>

						<div class="bm-field">
							<label for="description">설명</label>
							<textarea class="bm-textarea" id="description" name="description">${boardMasterVO.description}</textarea>
						</div>

						<div class="bm-field">
							<label for="useyn">사용 여부</label> <select name="useyn" id="useyn">
								<option value="1" ${boardMasterVO.useyn == 1 ? 'selected' : ''}>사용</option>
								<option value="0" ${boardMasterVO.useyn == 0 ? 'selected' : ''}>미사용</option>
							</select>
						</div>

						<div class="bm-field">
							<label for="writePermitType">글쓰기 권한 부여</label>
							<div class="bm-checkbox-group" >
								<label><input type="checkbox" name="writePermitTypesArray" value="1"
									<c:if test="${fn:contains(boardMasterVO.writePermitType, '1')}">checked</c:if> />
									사용자</label> <label><input type="checkbox"
									name="writePermitTypesArray" value="2"
									<c:if test="${fn:contains(boardMasterVO.writePermitType, '2')}">checked</c:if> />
									조직원</label>
								<div class="bm-help">※ 관리자는 항상 글쓰기 권한이 부여됩니다.</div>
							</div>
						</div>

						<input type="hidden" name="createdBy"
							value="${boardMasterVO.createdBy}" />

						<div class="bm-actions">
							<button type="submit" class="bm-btn">수정</button>
							<button type="button" class="bm-btn bm-btn--delete" onclick="confirmDelete()">삭제</button>
						</div>
					</form>

					<form id="deleteForm" action="<c:url value='/admin/boardMaster/delete.do' />" method="post" style="display: none;">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						<input type="hidden" name="boardCode" value="${boardMasterVO.boardCode}" />
					</form>
				</div>
			</div>
		</div>
	</sec:authorize>
</body>
</html>
