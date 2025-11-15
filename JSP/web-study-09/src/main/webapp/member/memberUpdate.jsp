<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ page import="com.saeyan.dto.MemberVO" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 정보 수정</title>
<!-- 외부 JavaScript 파일 로드 (joinCheck 함수 등 포함) -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/member.js"></script>
<script type="text/javascript">
   /**
    * 취소 버튼 클릭 시 실행되는 함수
    * 
    * 역할: 회원정보 수정을 취소하고 메인 페이지로 이동
    */
   function cancelUpdate() {
      var contextPath = '<%=request.getContextPath()%>';
      location.href = contextPath + '/member/main.jsp';  // 메인 페이지로 이동
   }
</script>
</head>
<body>
   <h2>회원 수정</h2>
   <%
      /**
       * UpdateServlet에서 전달받은 회원 정보 가져오기
       * - doGet 메서드에서 request.setAttribute("mvo", mvo)로 설정한 값
       * - 데이터베이스에서 조회한 최신 회원 정보
       */
      MemberVO mvo = (MemberVO) request.getAttribute("mvo");
      
      /**
       * 각 필드의 값을 저장할 변수 초기화
       * - null 체크를 통해 안전하게 값을 가져오기 위함
       */
      String nameValue = "";
      String useridValue = "";
      String emailValue = "";
      String phoneValue = "";
      int adminValue = 0;
      
      /**
       * mvo가 null이 아닌 경우에만 값 추출
       * - 각 필드가 null일 수 있으므로 삼항 연산자로 안전하게 처리
       */
      if(mvo != null) {
         nameValue = mvo.getName() != null ? mvo.getName() : "";
         useridValue = mvo.getUserid() != null ? mvo.getUserid() : "";
         emailValue = mvo.getEmail() != null ? mvo.getEmail() : "";
         phoneValue = mvo.getPhone() != null ? mvo.getPhone() : "";
         adminValue = mvo.getAdmin();
      }
   %>
   
   <!-- 회원 정보를 불러오지 못한 경우 에러 메시지 표시 -->
   <% if(mvo == null) { %>
      <p>회원 정보를 불러올 수 없습니다. 다시 시도해주세요.</p>
   <% } %>
   
   <!-- 
       회원정보 수정 폼
       - action: 폼 제출 시 이동할 URL (UpdateServlet의 /memberUpdate.do)
       - method: POST 방식 사용
       - name: JavaScript에서 document.frm으로 접근하기 위해
   -->
   <form action="<%=request.getContextPath()%>/memberUpdate.do" method="post" name="frm">
      <table>
         <!-- 이름 입력 행 -->
         <tr>
            <td>이름</td>
            <td>
               <!-- 
                   이름 입력 필드 (수정 불가)
                   - readonly: 읽기 전용 (수정할 수 없음)
                   - value: 기존 이름 값 표시
               -->
               <input type="text" name="name" size="20" value="<%= nameValue %>" readonly>
            </td>
         </tr>
         
         <!-- 아이디 입력 행 -->
         <tr>
            <td>아이디</td>
            <td>
               <!-- 
                   hidden 필드: 서버로 전송하기 위한 아이디 값
                   - 화면에는 보이지 않지만 폼 제출 시 함께 전송됨
                   - UpdateServlet의 doPost에서 WHERE 조건에 사용
               -->
               <input type="hidden" name="userid" value="<%= useridValue %>">
               
               <!-- 
                   아이디 표시 필드 (수정 불가)
                   - readonly disabled: 읽기 전용이면서 비활성화
                   - value: 기존 아이디 값 표시
                   - 사용자가 수정할 수 없도록 함
               -->
               <input type="text" size="20" value="<%= useridValue %>" readonly disabled>
            </td>
         </tr>
         
         <!-- 비밀번호 입력 행 -->
         <tr>
            <td>암 &nbsp; 호</td>
            <td>
               <!-- 
                   비밀번호 입력 필드
                   - value="": 빈 값 (보안상 기존 비밀번호를 표시하지 않음)
                   - 사용자가 새 비밀번호를 입력하면 그 값으로 변경됨
               -->
               <input type="password" name="pwd" size="20" value="">*
            </td>
         </tr>
         
         <!-- 비밀번호 확인 입력 행 -->
         <tr height="30">
            <td width="80">암호 확인</td>
            <td>
               <!-- 비밀번호 확인 입력 필드 -->
               <input type="password" name="pwd_check" size="20">*
            </td>
         </tr>
         
         <!-- 이메일 입력 행 -->
         <tr>
            <td>이메일</td>
            <td>
               <!-- 이메일 입력 필드 (기존 값 표시) -->
               <input type="text" name="email" size="20" value="<%= emailValue %>">
            </td>
         </tr>
         
         <!-- 전화번호 입력 행 -->
         <tr>
            <td>전화번호</td>
            <td>
               <!-- 전화번호 입력 필드 (기존 값 표시) -->
               <input type="text" name="phone" size="20" value="<%= phoneValue %>">
            </td>
         </tr>
         
         <!-- 등급 선택 행 -->
         <tr>
            <td>등급</td>
            <td>
               <!-- 
                   라디오 버튼으로 등급 선택
                   - 기존 등급에 따라 checked 속성 설정
                   - adminValue == 0이면 일반회원 선택, 1이면 관리자 선택
               -->
               <input type="radio" name="admin" value="0" <%= adminValue == 0 ? "checked=\"checked\"" : "" %>> 일반회원
               <input type="radio" name="admin" value="1" <%= adminValue == 1 ? "checked=\"checked\"" : "" %>> 관리자
            </td>
         </tr>
         
         <!-- 버튼 행 -->
         <tr>
            <td colspan="2" align="center">
               <!-- 
                   확인 버튼: 폼 제출
                   - onclick="return joinCheck()": 클릭 시 joinCheck() 함수 실행하여 유효성 검증
               -->
               <input type="submit" value="확인" onclick="return joinCheck()">
               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
               
               <!-- 취소 버튼: 메인 페이지로 이동 -->
               <input type="button" value="취소" onclick="cancelUpdate()">
            </td>
         </tr>
         
         <!-- 메시지 표시 행 -->
         <tr>
            <td colspan="2" align="center">
               <%
                  /**
                   * UpdateServlet에서 전달한 메시지 표시
                   * - 성공 시: "회원정보가 수정되었습니다."
                   * - 실패 시: "회원정보 수정에 실패했습니다."
                   */
                  String message = (String) request.getAttribute("message");
                  if(message != null && !message.trim().equals("")) {
               %>
                  <div><%= message %></div>
               <%
                  }
               %>
            </td>
         </tr>
      </table>
   </form>
</body>
</html>
