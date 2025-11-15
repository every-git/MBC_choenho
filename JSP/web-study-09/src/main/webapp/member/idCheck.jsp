<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>아이디 중복확인</title>
<!-- 외부 JavaScript 파일 로드 (idok 함수 포함) -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/member.js"></script>
</head>
<body>
   <h2>아이디 중복확인</h2>
   
   <!-- 
       아이디 중복 체크 폼
       - action: IdCheckServlet의 /idCheck.do로 GET 요청 전송
       - method: GET 방식 사용 (URL에 userid 파라미터 포함)
   -->
   <form action="<%=request.getContextPath()%>/idCheck.do" method="get" name="frm">
      <!-- 
          아이디 입력 필드
          - value="${userid}": IdCheckServlet에서 전달한 userid 값 표시
          - placeholder: 입력 안내 문구
      -->
      아이디 <input type="text" name="userid" value="${userid}" placeholder="아이디를 입력하세요"> 
      
      <!-- 중복 체크 버튼: 폼 제출하여 중복 확인 -->
      <input type="submit" value="중복 체크"> 
   </form>
   
   <!-- 
       에러 메시지 표시
       - IdCheckServlet에서 전달한 message가 있으면 표시
       - 예: "아이디를 입력해주세요."
   -->
   <c:if test="${not empty message}">
      <div>${message}</div>
   </c:if>
   
   <!-- 
       중복된 아이디인 경우 (result == 1)
       - IdCheckServlet에서 confirmID() 메서드가 1을 반환한 경우
   -->
   <c:if test="${result == 1}">
      <div>
         <!-- 
             부모 창(회원가입 페이지)의 아이디 필드 초기화
             - opener: 팝업을 연 부모 창을 가리킴
             - 중복된 아이디이므로 부모 창의 입력 필드를 비워줌
         -->
         <script type="text/javascript">
            if(opener && opener.document && opener.document.frm) {
               opener.document.frm.userid.value = "";
            }
         </script>
         <!-- 중복 메시지 표시 -->
         <strong>${userid}</strong>는 이미 사용 중인 아이디입니다.
      </div>
   </c:if>
   
   <!-- 
       사용 가능한 아이디인 경우 (result == 0)
       - IdCheckServlet에서 confirmID() 메서드가 0을 반환한 경우
   -->
   <c:if test="${result == 0}">
      <div>
         <!-- 사용 가능 메시지 표시 -->
         <strong>${userid}</strong>는 사용 가능한 아이디입니다.
         <br><br>
         <!-- 
             사용 버튼
             - onclick="idok('${userid}')": 클릭 시 idok 함수 호출
             - idok 함수는 부모 창의 폼에 아이디를 설정하고 팝업을 닫음
         -->
         <input type="button" value="사용" onclick="idok('${userid}')">
      </div>
   </c:if>
   
   <!-- 
       데이터베이스 오류인 경우 (result == -1)
       - IdCheckServlet에서 confirmID() 메서드가 -1을 반환한 경우
   -->
   <c:if test="${result == -1}">
      <div>
         데이터베이스 오류가 발생했습니다. 다시 시도해주세요.
      </div>
   </c:if>
</body>
</html>
