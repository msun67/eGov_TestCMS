<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 등록</title>
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
            <h1 class="board-title">게시글 등록</h1>
            
            <form action="<c:url value='/write.do'/>" method="post" enctype="multipart/form-data" onsubmit="return submitForm();">
            <!-- ✅ CSRF 토큰 (Spring Security 태그) -->
    		<sec:csrfInput/>

                <table class="board-table">
                    <tr>
                        <th>카테고리</th>
                        <td>
                            <select name="boardCode">
                                <c:forEach var="item" items="${boardMasterList}">
                                    <option value="${item.boardCode}"
                                        <c:if test="${item.boardCode == boardCode}">selected</c:if>>
                                        ${item.boardName}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>제목</th>
                        <td>
                            <input type="text" name="boardTitle" style="width:100%;" required />
                        </td>
                    </tr>
                    <tr>
                        <th>작성자</th>
                        <td>
                            <input type="text" name="userUuid" style="width:100%;" required />
                        </td>
                    </tr>
                    <tr>
                        <th>내용</th>
                        <td>
                            <textarea name="boardContent" id="boardContent" rows="10" style="width:100%;"></textarea>
                        </td>
                    </tr>
                    <tr>
					    <th>첨부파일</th>
					    <td>
					        <div id="fileInputs">
							    <!-- 동적으로 라인이 추가될 영역 -->
							</div>
					    </td>
					</tr>
                </table>

                <!-- 버튼 영역 -->
                <div class="btn-group">
                    <button type="submit" class="btn btn-blue">등록</button>
                    <button type="button" class="btn btn-gray" onclick="location.href='board.do?boardCode=${boardCode}'">목록으로</button>
                </div>
            </form>

<!-- 스마트에디터 -->            
<script src="<c:url value='/resources/smarteditor/js/HuskyEZCreator.js'/>" charset="utf-8"></script>
<script>
	let oEditor = [];
	
	nhn.husky.EZCreator.createInIFrame({
	    oAppRef: oEditor,
	    elPlaceHolder: "boardContent", // textarea ID
	    sSkinURI: "<c:url value='/resources/smarteditor/SmartEditor2Skin.html'/>",
	    fCreator: "createSEditor2"
	});

//submitForm()을 명시적 호출
function submitForm() {
	// 스마트에디터 내용 textarea에 적용
    oEditor[0].exec("UPDATE_CONTENTS_FIELD", []);
	
    const content = document.getElementById("boardContent").value;
    //alert("저장될 HTML:\n" + content);
    if (content.trim() === "") {
        alert("내용을 입력해주세요.");
        return false;
    }
    if (totalSize > maxTotalSize) {
        alert("전체 첨부파일 용량은 40MB를 초과할 수 없습니다.");
        return false;
    }
    console.log("전송할 HTML:", content);
    return true;
}
</script>

<!-- 첨부파일영역 -->
<script>
const maxFileSize = 10 * 1024 * 1024;  // 10MB
const maxTotalSize = 40 * 1024 * 1024; // 40MB

let totalSize = 0;

function addFileInput() {
    const div = document.createElement("div");
    div.className = "file-row";

    const textInput = document.createElement("input");
    textInput.type = "text";
    textInput.readOnly = true;

    const fileInput = document.createElement("input");
    fileInput.type = "file";
    fileInput.name = "uploadFiles";
    fileInput.style.display = "none"; // 숨겨진 실제 input
    fileInput.addEventListener("change", function () {
        if (fileInput.files.length > 0) {
            const file = fileInput.files[0];

            // 파일당 크기 제한
            if (file.size > maxFileSize) {
                alert("10MB 이하의 파일로 업로드해 주세요.");
                fileInput.value = "";
                textInput.value = "";
                return;
            }

            // 전체 크기 제한
            const inputs = document.querySelectorAll("input[type='file'][name='uploadFiles']");
            let tempTotal = 0;
            for (let i of inputs) {
                if (i.files.length > 0) {
                    tempTotal += i.files[0].size;
                }
            }
            if (tempTotal > maxTotalSize) {
                alert("전체 첨부파일 용량은 40MB를 초과할 수 없습니다.");
                fileInput.value = "";
                textInput.value = "";
                return;
            }

            totalSize = tempTotal;
            textInput.value = file.name;
        }
    });

    const labelBtn = document.createElement("button");
    labelBtn.type = "button";
    labelBtn.className = "btn-gray";
    labelBtn.textContent = "첨부파일";
    labelBtn.onclick = () => fileInput.click();

    const delBtn = document.createElement("button");
    delBtn.type = "button";
    delBtn.className = "btn-delete";
    delBtn.textContent = "삭제";
    delBtn.onclick = function () {
    	const fileInputs = document.querySelectorAll("#fileInputs .file-row");
        if (fileInputs.length === 1) {
            // 마지막 한 줄일 경우, 삭제 대신 초기화
            fileInput.value = "";
            textInput.value = "";
            totalSize = 0;
        } else {
            if (fileInput.files.length > 0) {
                totalSize -= fileInput.files[0].size;
            }
            div.remove();
        }
    };

    const addBtn = document.createElement("button");
    addBtn.type = "button";
    addBtn.className = "btn-add";
    addBtn.textContent = "파일 추가";
    addBtn.onclick = addFileInput;

    div.appendChild(textInput);
    div.appendChild(labelBtn);
    div.appendChild(delBtn);
    div.appendChild(addBtn);
    div.appendChild(fileInput);

    document.getElementById("fileInputs").appendChild(div);
}

// 페이지 로딩 시 1개 기본 파일 input 추가
window.onload = function () {
    addFileInput();
};
</script>

        </div><!-- board-detail-container -->
    </div><!-- main-content -->
</div><!-- layout-container -->
</body>
</html>
