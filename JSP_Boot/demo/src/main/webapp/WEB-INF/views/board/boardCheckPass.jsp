<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/shopping.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/script/board.js"></script>
</head>
<body>
	<h1>비밀번호 확인</h1>
    <form name="frm" method="post" action="${pageContext.request.contextPath}/board_check_pass">
        <input type="hidden" name="num" value="${param.num != null ? param.num : num}">
        <input type="hidden" name="action" value="${param.action}">
        <table>
            <tr>
                <td>비밀번호</td>
                <td><input type="password" name="pass" size="20"></td>
            </tr>
        </table>
            <br>
            <input type="submit" value="확인" onclick="return passCheck()">
            <br><br>${message}
        
    </form>
	
</body>
</html>