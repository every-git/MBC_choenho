<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<script>
/**
 * 로그인 폼 유효성 검증 함수
 * 
 * 역할: 폼 제출 전에 아이디와 비밀번호가 입력되었는지 확인
 * 
 * @returns {boolean} 검증 통과 시 true, 실패 시 false
 * 
 * 처리 과정:
 * 1. 아이디 입력 여부 확인
 * 2. 비밀번호 입력 여부 확인
 * 3. 모두 입력되었으면 true 반환 (폼 제출 허용)
 * 4. 하나라도 비어있으면 false 반환 (폼 제출 차단)
 */
function loginCheck() {
    // 아이디 입력 체크
    if(document.frm.userid.value == "") {
        alert("아이디를 입력해주세요.");
        document.frm.userid.focus();  // 아이디 입력 필드로 포커스 이동
        return false;  // 폼 제출 중단
    }
    
    // 비밀번호 입력 체크
    if(document.frm.pwd.value == "") {
        alert("비밀번호를 입력해주세요.");
        document.frm.pwd.focus();  // 비밀번호 입력 필드로 포커스 이동
        return false;  // 폼 제출 중단
    }
    
    return true;  // 모든 검증 통과 시 폼 제출 허용
}
</script>
</head>
<body>
    <h2>로그인</h2>
    
    <!-- 
        로그인 폼
        - action: 폼 제출 시 이동할 URL (LoginServlet의 /login.do)
        - method: POST 방식 사용 (비밀번호 같은 중요한 정보 전송)
        - name: 폼에 이름 부여 (JavaScript에서 document.frm으로 접근하기 위해)
    -->
    <form name="frm" action="<%=request.getContextPath()%>/login.do" method="post">
        <table>
            <!-- 아이디 입력 행 -->
            <tr>
                <td>아이디</td>
                <td>
                    <%
                       // LoginServlet에서 전달한 userid 값 가져오기
                       // 로그인 실패 시 입력했던 아이디를 다시 표시하기 위함
                       String userid = (String) request.getAttribute("userid");
                       if(userid == null) userid = "";  // 값이 없으면 빈 문자열
                    %>
                    <!-- 
                        아이디 입력 필드
                        - name: 서버로 전송될 파라미터 이름 (LoginServlet에서 request.getParameter("userid")로 받음)
                        - value: 이전에 입력했던 아이디 값 (로그인 실패 시 유지)
                        - autocomplete="off": 브라우저 자동완성 기능 비활성화
                    -->
                    <input type="text" name="userid" placeholder="아이디를 입력하세요" value="<%= userid %>" autocomplete="off">
                </td>
            </tr>
            
            <!-- 비밀번호 입력 행 -->
            <tr>
                <td>비밀번호</td>
                <td>
                    <!-- 
                        비밀번호 입력 필드
                        - type="password": 입력값이 가려짐 (보안)
                        - name: 서버로 전송될 파라미터 이름 (LoginServlet에서 request.getParameter("pwd")로 받음)
                    -->
                    <input type="password" name="pwd" placeholder="비밀번호를 입력하세요">
                </td>
            </tr>
            
            <!-- 버튼 행 -->
            <tr>
                <td colspan="2" align="center">
                    <!-- 
                        로그인 버튼
                        - type="submit": 폼 제출 버튼
                        - onclick="return loginCheck()": 클릭 시 loginCheck() 함수 실행
                          - 함수가 true를 반환하면 폼 제출 진행
                          - 함수가 false를 반환하면 폼 제출 중단
                    -->
                    <input type="submit" value="로그인" onclick="return loginCheck();">
                    
                    <!-- 취소 버튼: 폼의 모든 입력값 초기화 -->
                    <input type="reset" value="취소">
                    
                    <!-- 회원가입 버튼: 회원가입 페이지로 이동 -->
                    <input type="button" value="회원가입" onclick="location.href='<%=request.getContextPath()%>/join.do'">
                </td>
            </tr>
            
            <!-- 에러 메시지 표시 행 -->
            <tr>
                <td colspan="2">
                    <% 
                       // LoginServlet에서 전달한 에러 메시지가 있으면 표시
                       // 로그인 실패 시 "아이디 또는 비밀번호가 맞지 않습니다." 등의 메시지 표시
                       if(request.getAttribute("message") != null) { 
                    %>
                        <div><%=request.getAttribute("message")%></div>
                    <% } %>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>
