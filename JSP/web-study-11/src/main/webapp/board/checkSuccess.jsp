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
        
        if(window.name == 'update') {
            // 수정 페이지로 이동 (부모 창)
            // TODO: BoardUpdateFormAction이 구현되면 아래 주석 해제
            // window.opener.location.href = 'BoardServlet?command=board_update_form&num=' + num;
            
            // 임시: 수정 기능이 구현되기 전까지 목록으로 이동
            window.opener.location.href = 'BoardServlet?command=board_update_form&num=' + num;
        } else if(window.name == 'delete') {
            // 삭제 처리 (부모 창에서 실행)
            window.opener.location.href = 'BoardServlet?command=board_delete&num=' + num;
        }
        window.close(); // 팝업 창 닫기
    </script>
</body>
</html>