<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>

<!-- ============================================ -->
<!-- 외부 CSS 파일 링크 -->
<!-- ============================================ -->
<!-- login.css: 로그인 페이지의 모든 스타일 정의 -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/login.css">

<!-- ============================================ -->
<!-- JavaScript 함수 정의 시작 -->
<!-- ============================================ -->
<script>
/**
 * 로그인 폼 유효성 검증 함수
 * 아이디와 비밀번호가 입력되었는지 확인
 * @returns {boolean} 검증 통과 시 true, 실패 시 false
 */
function loginCheck() {
    // 아이디 입력 체크 - 아이디가 비어있으면 경고 메시지 표시
    if(document.frm.userid.value == "") {
        alert("아이디를 입력해주세요.");
        document.frm.userid.focus(); // 아이디 입력 필드로 포커스 이동
        return false; // 폼 제출 중단
    }
    // 비밀번호 입력 체크 - 비밀번호가 비어있으면 경고 메시지 표시
    if(document.frm.pwd.value == "") {
        alert("비밀번호를 입력해주세요.");
        document.frm.pwd.focus(); // 비밀번호 입력 필드로 포커스 이동
        return false; // 폼 제출 중단
    }
    return true; // 모든 검증 통과 시 폼 제출 허용
}
</script>
<!-- ============================================ -->
<!-- JavaScript 함수 정의 끝 -->
<!-- ============================================ -->
</head>
<body>
    <!-- ============================================ -->
    <!-- 로그인 컨테이너 시작 -->
    <!-- ============================================ -->
    <div class="login-container">
        <!-- 로그인 제목 -->
        <h2>로그인</h2>
        
        <!-- 로그인 폼 시작 -->
        <!-- action: 폼 제출 시 이동할 URL (LoginServlet) -->
        <!-- method: POST 방식 사용 (비밀번호 같은 중요한 정보 전송) -->
        <!-- name: 폼에 이름 부여 (JavaScript에서 접근하기 위해) -->
        <form name="frm" action="<%=request.getContextPath()%>/login.do" method="post">
            <table>
                <!-- 아이디 입력 행 -->
                <tr>
                    <td>아이디</td>
                    <td>
                        <!-- 아이디 입력 필드 -->
                        <input type="text" name="userid" placeholder="아이디를 입력하세요">
                    </td>
                </tr>
                
                <!-- 비밀번호 입력 행 -->
                <tr>
                    <td>비밀번호</td>
                    <td>
                        <!-- 비밀번호 입력 필드 (type="password"로 입력값이 가려짐) -->
                        <input type="password" name="pwd" placeholder="비밀번호를 입력하세요">
                    </td>
                </tr>
                
                <!-- 버튼 행 -->
                <tr>
                    <td colspan="2" align="center">
                        <div class="button-container">
                            <!-- 로그인 버튼 - 클릭 시 loginCheck() 함수 실행 후 폼 제출 -->
                            <input type="submit" value="로그인" onclick="return loginCheck();">
                            
                            <!-- 취소 버튼 - 클릭 시 폼의 모든 입력값 초기화 -->
                            <input type="reset" value="취소">
                            
                            <!-- 회원가입 버튼 - 클릭 시 회원가입 페이지로 이동 -->
                            <input type="button" value="회원가입" onclick="location.href='join.do'">
                        </div>
                    </td>
                </tr>
                
                <!-- 로그인 실패 시 메시지 출력 행 -->
                <!-- ${message}: LoginServlet에서 전달한 에러 메시지 -->
                <tr>
                    <td colspan="2">
                        <%-- 에러 메시지가 있을 때만 표시 --%>
                        <% if(request.getAttribute("message") != null) { %>
                            <div class="error-message"><%=request.getAttribute("message")%></div>
                        <% } %>
                    </td>
                </tr>
            </table>
        </form>
        <!-- 로그인 폼 끝 -->
    </div>
    <!-- ============================================ -->
    <!-- 로그인 컨테이너 끝 -->
    <!-- ============================================ -->
</body>
</html>