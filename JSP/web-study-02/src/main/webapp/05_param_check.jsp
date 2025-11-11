<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Parameter Check</title>
<script src="<%=request.getContextPath()%>/js/param.js"></script>
</head>
<body>
    <form method="post" action="<%=request.getContextPath()%>/ParamServlet" name="frm">
        아이디: <input type="text" name="id"><br>
        나이: <input type="text" name="age"><br>
        <input type="submit" value="전송" onclick="return check()">
        <input type="reset" value="취소">
    </form>
</body>
</html>

