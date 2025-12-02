<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <script type="text/javascript">
        var num = '${num != null ? num : param.num}'; // request attribute 또는 URL 파라미터에서 num 가져오기
        var action = '${action != null ? action : param.action}';
        
        if(window.name == 'update' || action == 'update') {
            // 수정 페이지로 이동 (부모 창)
            window.opener.location.href = '${pageContext.request.contextPath}/board_update_form?num=' + num;
        } else if(window.name == 'delete' || action == 'delete') {
            // 삭제 처리 (부모 창에서 실행)
            window.opener.location.href = '${pageContext.request.contextPath}/board_delete?num=' + num;
        }
        window.close(); // 팝업 창 닫기
    </script>
</body>
</html>