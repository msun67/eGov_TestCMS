<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 생성</title>
<link rel="stylesheet" type="text/css"
	href="/demo_cms/css/cms/common.css">

<!-- ===== Page-local styles (bm- 접두사로만 정의: common.css와 충돌 회피) ===== -->
<style>
.bm-wrap {
	max-width: 1200px;
	margin: 24px auto;
	padding: 0 16px;
	box-sizing: border-box;
}

.bm-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin: 8px 0 16px;
}

.bm-title {
	font-size: 24px;
	font-weight: 700;
	display: flex;
	align-items: center;
	gap: 8px;
}

.bm-sub {
	color: #6b7280;
	font-size: 14px;
}

/* 2-column grid (좌측 목록, 우측 폼) */
.bm-grid {
	display: grid;
	grid-template-columns: 1.2fr 1fr;
	gap: 20px;
}

@media ( max-width : 980px) {
	.bm-grid {
		grid-template-columns: 1fr;
	}
}

/* 카드 */
.bm-card {
	background: #fff;
	border: 1px solid #e5e7eb;
	border-radius: 14px;
	box-shadow: 0 1px 2px rgba(0, 0, 0, .04);
}

.bm-card-hd {
	padding: 14px 16px;
	border-bottom: 1px solid #f1f5f9;
	font-weight: 700;
}

.bm-card-bd {
	padding: 16px;
}

/* 목록 */
.bm-list {
	display: flex;
	flex-direction: column;
	gap: 10px;
}

.bm-item {
	display: flex;
	justify-content: space-between;
	align-items: flex-start;
	gap: 10px;
	padding: 12px;
	border: 1px solid #eef2f7;
	border-radius: 12px;
}

.bm-item-main {
	display: flex;
	flex-direction: column;
	gap: 4px;
}

.bm-code {
	font-weight: 700;
	color: #111827;
}

.bm-desc {
	font-size: 12px;
	color: #6b7280;
}

.bm-badges {
	display: flex;
	flex-wrap: wrap;
	gap: 6px;
	margin-top: 6px;
}

.bm-badge {
	font-size: 11px;
	padding: 3px 8px;
	border-radius: 999px;
	border: 1px solid #e5e7eb;
	background: #f9fafb;
	color: #374151;
}

.bm-badge--on {
	color: #065f46;
	background: #ecfdf5;
	border-color: #a7f3d0;
}

.bm-badge--off {
	color: #991b1b;
	background: #fef2f2;
	border-color: #fecaca;
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

.bm-btn--sm {
	padding: 6px 10px;
	font-size: 12px;
	border-radius: 999px;
}

.bm-btn--primary {
	background: #2563eb;
	color: #fff;
	border-color: #2563eb;
}

.bm-btn--primary:hover {
	background: #1d4ed8;
}

.bm-btn--ghost {
	background: #fff;
	color: #111827;
	text-decoration:none;
}

/* 폼 */
.bm-form {
	display: flex;
	flex-direction: column;
	gap: 14px;
}

.bm-field label {
	display: block;
	font-weight: 600;
	margin-bottom: 6px;
}

.bm-input, .bm-textarea {
	width: 100%;
	border: 1px solid #e5e7eb;
	border-radius: 10px;
	padding: 10px 12px;
	box-sizing: border-box;
	font-family: 'Segoe UI', sans-serif;
}

.bm-textarea {
	min-height: 110px;
	resize: vertical;
}

.bm-help {
	font-size: 12px;
	color: #6b7280;
	margin-top: 4px;
}

.bm-actions {
	display: flex;
	justify-content: flex-end;
	gap: 8px;
}

/* input과 textarea 공통 placeholder 스타일 */
.bm-input::placeholder,
.bm-textarea::placeholder {
  font-family: inherit;
  color: #9ca3af;
  font-size: 14px;
}

/* Webkit (Chrome, Safari, Edge) */
.bm-input::-webkit-input-placeholder,
.bm-textarea::-webkit-input-placeholder {
  font-family: inherit;
  color: #9ca3af;
  font-size: 14px;
}

/* Firefox */
.bm-input::-moz-placeholder,
.bm-textarea::-moz-placeholder {
  font-family: inherit;
  color: #9ca3af;
  font-size: 14px;
}

/* IE/Edge 구버전 */
.bm-input:-ms-input-placeholder,
.bm-textarea:-ms-input-placeholder {
  font-family: inherit;
  color: #9ca3af;
  font-size: 14px;
}

/* common.css의 .alert는 absolute로 main-content 안쪽 최상단에 표시됨 */
.main-content {
	position: relative;
	padding: 24px 0;
	background: transparent;
}
</style>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/include/topmenu.jsp"%>

	<sec:authorize access="hasRole('ROLE_ADMIN')">

		<div class="main-content">
			<!-- ✅ 성공/안내 메시지(Flash) -->
			<c:if test="${not empty message}">
				<div class="alert alert-success">${message}</div>
			</c:if>


			<div class="bm-wrap">
				<div class="bm-header">
					<div class="bm-title">
						👑 게시판 생성 <span class="bm-sub">관리자 페이지</span>
					</div>
					<div>
						<a class="bm-btn bm-btn--ghost"
							href="<c:url value='/dashboard.do'/>">대시보드</a> <a
							class="bm-btn bm-btn--ghost"
							href="<c:url value='/admin/boardMaster/list.do'/>">전체 목록</a>
					</div>
				</div>

				<div class="bm-grid">
					<!-- 좌: 이미 생성된 게시판 -->
					<div class="bm-card">
						<div class="bm-card-hd">이미 생성된 게시판</div>
						<div class="bm-card-bd">
							<c:choose>
								<c:when test="${empty boardList}">
									<div style="color: #6b7280">아직 생성된 게시판이 없습니다.</div>
								</c:when>
								<c:otherwise>
									<div class="bm-list">
										<c:forEach var="b" items="${boardList}">
											<div class="bm-item">
												<div class="bm-item-main">
													<div class="bm-code">${b.boardCode}· ${b.boardName}</div>
													<div class="bm-badges">
														<span
															class="bm-badge ${b.useyn == 1 ? 'bm-badge--on' : 'bm-badge--off'}">
															${b.useyn == 1 ? '사용' : '사용 안함'} </span> <span class="bm-badge">작성자
															${b.createdBy}</span> <span class="bm-badge"> <fmt:formatDate
																value="${b.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" />
														</span>
													</div>
													<div class="bm-desc">${fn:escapeXml(b.description)}</div>
												</div>

												<!-- 사용 여부 토글 -->
												<form
													action="${pageContext.request.contextPath}/admin/boardMaster/useYn.do"
													method="post">
													<input type="hidden" name="${_csrf.parameterName}"
														value="${_csrf.token}" /> <input type="hidden"
														name="boardCode" value="${b.boardCode}" /> <input
														type="hidden" name="useyn" value="${b.useyn == 1 ? 0 : 1}" />
													<button class="bm-btn bm-btn--sm" type="submit">
														<c:choose>
															<c:when test="${b.useyn == 1}">사용 안함으로 전환</c:when>
															<c:otherwise>사용으로 전환</c:otherwise>
														</c:choose>
													</button>
												</form>
											</div>
										</c:forEach>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</div>

					<!-- 우: 생성 폼 -->
					<div class="bm-card">
						<div class="bm-card-hd">새 게시판 만들기</div>
						<div class="bm-card-bd">
							<form class="bm-form"
								action="<c:url value='/admin/boardMaster/create.do'/>"
								method="post">
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />

								<div class="bm-field">
									<label>예상 코드</label> 
									<div style="font-weight:700; font-size:16px; color:#2563eb; margin-bottom:4px;">
									${nextBoardCode}
									</div>
									<div class="bm-help">실제 생성 시점에 자동 부여됩니다(동시 생성 시 달라질 수 있음).</div>
								</div>

								<div class="bm-field">
									<label for="boardName">게시판 이름</label> <input class="bm-input"
										id="boardName" name="boardName" type="text" required
										placeholder="예: 공지사항" />
								</div>

								<div class="bm-field">
									<label for="description">설명</label>
									<textarea class="bm-textarea" id="description"
										name="description" placeholder="게시판 용도를 간단히 적어주세요."></textarea>
								</div>

								<div class="bm-field">
									<label>사용 여부</label>
									<!-- 기본 1로 전송되도록 hidden + checkbox 조합 -->
									<input type="hidden" name="useyn" id="useynHidden" value="1" />
									<label style="display: flex; align-items: center; gap: 8px;">
										<input type="checkbox" id="useynChk" checked
										onchange="document.getElementById('useynHidden').value = this.checked ? 1 : 0;">
										사용(노출)
									</label>
								</div>

								<div class="bm-actions">
									<a class="bm-btn" href="<c:url value='/dashboard.do'/>">취소</a>
									<button class="bm-btn bm-btn--primary" type="submit">게시판
										생성</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>

	</sec:authorize>

	<sec:authorize access="!hasRole('ROLE_ADMIN')">
		<div class="main-content">
			<div class="bm-wrap">
				<div class="alert alert-warning">관리자 권한이 필요합니다.</div>
			</div>
		</div>
	</sec:authorize>
</body>
</html>
