<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시판 수정</title>
    <style>
        form {
            width: 400px;
            padding: 20px;
            border: 1px solid #ddd;
            background: #fafafa;
            border-radius: 8px;
        }
        label {
            display: block;
            margin-top: 12px;
            font-weight: bold;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 8px;
            margin-top: 4px;
        }
        button {
            margin-top: 16px;
            padding: 8px 16px;
            cursor: pointer;
        }
        .actions {
            margin-top: 20px;
            display: flex;
            gap: 10px;
        }
        .delete-btn {
            background-color: #fff;
            border: 1px solid red;
            color: red;
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

<h2>✏️ 게시판 수정</h2>

<form action="/demo_cms/boardMaster/update.do" method="post">
    <label>게시판 코드:</label>
    <input type="text" name="boardCode" value="${boardMasterVO.boardCode}" readonly />

    <label>게시판 이름:</label>
    <input type="text" name="boardName" value="${boardMasterVO.boardName}" required />

    <label>설명:</label>
    <textarea name="description">${boardMasterVO.description}</textarea>
    
    <input type="hidden" name="createdBy" value="${boardMasterVO.createdBy}" />
    
    <div class="actions">
        <button type="submit">💾 수정</button>
        <button type="button" class="delete-btn" onclick="confirmDelete()">🗑️ 삭제</button>
    </div>
</form>

<!-- 삭제용 폼 (공백 방지를 위해 display: none 적용) -->
<form id="deleteForm" action="/demo_cms/boardMaster/delete.do" method="post" style="display: none;">
    <input type="hidden" name="boardCode" value="${boardMasterVO.boardCode}" />
</form>

</body>
</html>
