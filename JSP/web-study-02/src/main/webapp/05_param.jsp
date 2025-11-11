<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <form action="<%=request.getContextPath()%>/ParamServlet" method="get">
        아이디: <input type="text" name="id"><br>
        나 &nbsp;&nbsp; 이 : <input type="text" name="age"><br>
        <input type="submit" value="전송"> &nbsp;&nbsp;
        <input type="reset" value="취소">
    </form>
</body>
</html>