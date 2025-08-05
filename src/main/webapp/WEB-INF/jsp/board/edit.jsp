<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 수정</title>
     <link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/common.css">
     <link rel="stylesheet" type="text/css" href="/demo_cms/css/cms/edit.css">
</head>
<body>
<!-- layout-container  -->
<div class="layout-container">
	
	<%@ include file="/WEB-INF/jsp/include/leftmenu.jsp" %>    
	
	<!-- main-content -->
	<div class="main-content">
		<!-- board-detail-container -->
	    <div class="board-detail-container">
	    
	    	<!-- 로그인 사용자의 8자리 UUID -->
			<sec:authentication property="principal.userUuid" var="loginUuid"/>
			<!-- 관리자 여부 -->
			<sec:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin"/>
			
	    	<h1 class="board-title">게시글 수정</h1>
	    	
	    	 <!-- 수정 + 삭제 통합 폼 -->
		    <form action="update.do" method="post" enctype="multipart/form-data" onsubmit="return confirm('정말 수정하시겠습니까?');">
		        <sec:csrfInput/>
		        <input type="hidden" name="boardId" value="${board.boardId}" />
		
		        <table class="board-table">
		            <tr>
		                <th>카테고리</th>
		                <td>
		                    <select name="boardCode">
		                        <c:forEach var="item" items="${boardMasterList}">
		                            <option value="${item.boardCode}" 
		                                <c:if test="${item.boardCode == board.boardCode}">selected</c:if> >
		                                ${item.boardName}
		                            </option>
		                        </c:forEach>
		                    </select>
		                </td>
		            </tr>
		            <tr>
		                <th>제목</th>
		                <td>
		                    <input type="text" name="boardTitle" value="${board.boardTitle}" style="width:100%;" />
		                </td>
		            </tr>
		            <tr>
		                <th>내용</th>
		                <td>
		                    <textarea name="boardContent" id="boardContent" rows="10" style="width:100%;">
		                    	<c:out value="${board.boardContent}" escapeXml="false" />
		                    </textarea>
		                </td>
		            </tr>
		            <tr>
					    <th>첨부파일</th>
					    <td id="fileInputs">
							<c:if test="${not empty fileList}">
								<c:forEach var="file" items="${fileList}">
						    		<div class="file-row">
						    			<!-- 기존 파일명 -->
						    			<input type="text" readonly value="${file.originalName}">							    			
						    			<button type="button" class="btn-gray">첨부파일</button>
						    			<button type="button" class="btn-delete">삭제</button>
						    			<button type="button" class="btn-add">파일 추가</button>
						    			
						    			<!-- 실제 업로드용 -->
						    			<input type="file" name="uploadFiles" style="display: none;">
						    			
						    			<!-- 기존 파일 ID (삭제할 때 참조) -->
						    			<input type="hidden" name="existingFileIds" value="${file.fileId}">
    									<input type="hidden" name="deleteFileIds" value="" class="delete-file-id">
						    		</div>						       
					             </c:forEach>
					        </c:if>
					        
					         <c:if test="${empty fileList}">
						        <div class="file-row">
						            <input type="text" readonly>
						            <button type="button" class="btn-gray">첨부파일</button>
						            <button type="button" class="btn-delete">삭제</button>
						            <button type="button" class="btn-add">파일 추가</button>
						            <input type="file" name="uploadFiles" style="display: none;">
						        </div>
						    </c:if>
					    </td>
					</tr>
		        </table>
		
		        <!-- 버튼 영역 -->
		        <div class="btn-group">
					<c:if test="${isAdmin or loginUuid == board.userUuid}">
					<!-- ✅ 수정: CSRF 포함 -->
					<button type="submit" class="btn btn-blue">수정</button>
					
					<!-- ✅ 삭제: 숨김 폼 + JS 제출(아래 1-3 참고) -->
					<button type="button" class="btn btn-black"
					        onclick="deletePost(${board.boardId}, '${board.boardCode}')">삭제</button>
					</c:if>
					
					<!-- 항상 보이는 '목록' -->
					<button type="button" class="btn btn-gray"
					        onclick="location.href='<c:url value="/board.do"><c:param name="boardCode" value="${board.boardCode}"/></c:url>'">목록으로</button>
		        </div>
		    </form>
		    <!-- ✅ 삭제 전용 숨김 폼 -->
			<form id="deleteForm" method="post" action="<c:url value='/delete.do'/>" style="display:none;">
			  <sec:csrfInput/>
			  <input type="hidden" name="boardId"/>
			  <input type="hidden" name="boardCode"/>
			</form>		    
		</div>

<!-- 스마트에디터 -->  
<script type="text/javascript" src="/demo_cms/resources/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script>
	let oEditor = [];

	nhn.husky.EZCreator.createInIFrame({
	    oAppRef: oEditor,
	    elPlaceHolder: "boardContent", // textarea ID
	    sSkinURI: "/demo_cms/resources/smarteditor/SmartEditor2Skin.html",
	    fCreator: "createSEditor2"
	});
	
	//이벤트 리스너 방식
	document.querySelector("form").addEventListener("submit", function(e) {
	    oEditor[0].exec("UPDATE_CONTENTS_FIELD", []);
	    const content = document.getElementById("boardContent").value;

	    if (content.trim() === "") {
	        alert("내용을 입력해주세요.");
	        e.preventDefault();
	    }
	});
</script>		

<!-- 삭제용 POST 폼 -->  
<script>
    function deletePost(boardId, boardCode) {
    	if (!confirm('정말 삭제하시겠습니까?')) return;
        const f = document.getElementById('deleteForm');
        f.boardId.value = boardId;
        f.boardCode.value = boardCode;
        f.submit();
    }
</script>

<!-- 첨부파일영역 -->
<script>
const maxFileSize = 10 * 1024 * 1024; // 10MB

function addFileInput() {
    const fileInputsContainer = document.getElementById("fileInputs");

    const div = document.createElement("div");
    div.className = "file-row";

    const textInput = document.createElement("input");
    textInput.type = "text";
    textInput.readOnly = true;

    const fileInput = document.createElement("input");
    fileInput.type = "file";
    fileInput.name = "uploadFiles";
    fileInput.style.display = "none";

    const labelBtn = document.createElement("button");
    labelBtn.type = "button";
    labelBtn.className = "btn-gray";
    labelBtn.textContent = "첨부파일";

    const delBtn = document.createElement("button");
    delBtn.type = "button";
    delBtn.className = "btn-delete";
    delBtn.textContent = "삭제";

    const addBtn = document.createElement("button");
    addBtn.type = "button";
    addBtn.className = "btn-add";
    addBtn.textContent = "파일 추가";

    labelBtn.onclick = () => fileInput.click();

    fileInput.onchange = () => {
        const file = fileInput.files[0];
        if (!file) return;

        if (file.size > maxFileSize) {
            alert("10MB 이하의 파일로 업로드해 주세요.");
            fileInput.value = "";
            textInput.value = "";
            return;
        }
        textInput.value = file.name;
    };

    delBtn.onclick = () => {
        const rows = document.querySelectorAll(".file-row");
        if (rows.length === 1) {
            textInput.value = "";
            fileInput.value = "";
        } else {
            div.remove();
        }
    };

    addBtn.onclick = () => {
        addFileInput();
        bindEventsToExistingRows();
    };

    div.appendChild(textInput);
    div.appendChild(labelBtn);
    div.appendChild(delBtn);
    div.appendChild(addBtn);
    div.appendChild(fileInput);

    fileInputsContainer.appendChild(div);
}

// 기존 file-row에 이벤트 바인딩
function bindEventsToExistingRows() {
    const rows = document.querySelectorAll(".file-row");

    rows.forEach(row => {
        const textInput = row.querySelector("input[type='text']");
        const fileInput = row.querySelector("input[type='file']");
        const attachBtn = row.querySelector(".btn-gray");
        const delBtn = row.querySelector(".btn-delete");
        const addBtn = row.querySelector(".btn-add");

        const existingFileIdInput = row.querySelector("input[name='existingFileIds']");
        const deleteFileIdInput = row.querySelector(".delete-file-id");

        attachBtn.onclick = () => {
            fileInput.click();
        };

        fileInput.onchange = () => {
            const file = fileInput.files[0];
            if (!file) return;

            if (file.size > maxFileSize) {
                alert("10MB 이하의 파일로 업로드해 주세요.");
                fileInput.value = "";
                textInput.value = "";
                return;
            }

            textInput.value = file.name;
            
            // ✅ 기존 파일이면 삭제 대상으로 등록
            if (existingFileIdInput) {
                deleteFileIdInput.value = existingFileIdInput.value;
                existingFileIdInput.disabled = true; // 서버로 전달되지 않도록 처리
            }
        };

        delBtn.onclick = () => {
            const rows = document.querySelectorAll(".file-row");

            if (existingFileIdInput) {
                // 기존 파일일 경우 삭제 체크 처리
                deleteFileIdInput.value = existingFileIdInput.value;
                //row.remove(); // 아예 DOM에서 제거
                row.style.display = "none";
            } else {
                // 새로 추가한 행일 경우 내용만 비움
                if (rows.length === 1) {
                    textInput.value = "";
                    fileInput.value = "";
                } else {
                    row.remove();// 두 개 이상이면 DOM에서 제거
                }
            }
         	// ✅ 삭제 후 file-row가 하나도 없으면 새로 추가
            const remainingRows = document.querySelectorAll(".file-row:not([style*='display: none'])");
            if (remainingRows.length === 0) {
                addFileInput();
                bindEventsToExistingRows(); // 새로 추가된 행에 이벤트 바인딩
            }
        };

        addBtn.onclick = () => {
            addFileInput();
            bindEventsToExistingRows();
        };
    });
}

// DOM 로딩 완료 후 초기 이벤트 바인딩
document.addEventListener("DOMContentLoaded", () => {
	 if (document.querySelectorAll(".file-row").length === 0) {
	        addFileInput();
	    }
    bindEventsToExistingRows();
});
</script>

	    
		</div><!-- board-detail-container -->
    </div><!-- main-content -->
</div><!-- layout-container  -->
</body>
</html>
