<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시판 생성</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f9f9f9;
        }

        h2 {
            color: #333;
            margin-bottom: 20px;
        }

        form {
            background-color: #fff;
            padding: 20px;
            width: 500px;
            border: 1px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
            color: #555;
        }

        input[type="text"],
        textarea {
            width: 100%;
            padding: 8px 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
            box-sizing: border-box;
        }

        textarea {
            height: 100px;
            resize: vertical;
        }

        button {
            margin-top: 20px;
            padding: 10px 20px;
            font-size: 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }

        button:hover {
            background-color: #45a049;
        }

        .back-link {
            margin-top: 20px;
            display: block;
        }
    </style>
</head>
<body>

<h2>➕ 게시판 생성</h2>

<form action="/demo_cms/boardMaster/create.do" method="post">
    <label for="boardCode">게시판 코드</label>
    <input type="text" id="boardCode" name="boardCode" required />

    <label for="boardName">게시판 이름</label>
    <input type="text" id="boardName" name="boardName" required />

    <label for="description">설명</label>
    <textarea id="description" name="description"></textarea>

    <input type="hidden" name="createdBy" value="admin123" />

    <button type="submit">게시판 생성</button>
</form>

<a href="/demo_cms/boardMaster/list.do" class="back-link">← 게시판 목록보기</a>
<a href="/demo_cms/main.do" class="back-link">← 메인으로 돌아가기</a>
</body>
</html>
