<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>회원가입</title>
    <script type="text/javascript">
      /**
       * 전역 변수 설정
       * - contextPath를 JavaScript에서 사용하기 위해 전역 변수로 설정
       * - join.jsp와 member.js에서 모두 사용 가능하도록 함
       */
      window.contextPath = '<%=request.getContextPath()%>';
      var contextPath = window.contextPath;
      
      /**
       * 취소 버튼 클릭 시 실행되는 함수
       * 
       * 역할: 회원가입을 취소하고 로그인 페이지로 이동
       */
      function cancelJoin() {
        var loginUrl = contextPath + "/login.do";
        location.href = loginUrl;  // 로그인 페이지로 이동
      }
    </script>
    <!-- 외부 JavaScript 파일 로드 (아이디 중복 체크 등 함수 포함) -->
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/member.js"></script>
  </head>
  <body>
    <h2>회원 가입</h2>
    '*' 표시 항목은 필수 입력 항목입니다.
    
    <!-- 
        회원가입 폼
        - action: 폼 제출 시 이동할 URL (JoinServlet의 /join.do)
        - method: POST 방식 사용
        - name: JavaScript에서 document.frm으로 접근하기 위해
    -->
    <form action="<%=request.getContextPath()%>/join.do" method="post" name="frm">
      <table>
        <!-- 이름 입력 행 -->
        <tr>
          <td>이름</td>
          <td>
            <!-- 
                이름 입력 필드
                - maxlength="10": 최대 10자까지 입력 가능 (DB 컬럼 크기 제한)
            -->
            <input type="text" name="name" size="20" maxlength="10" />
          </td>
        </tr>
        
        <!-- 아이디 입력 및 중복 체크 행 -->
        <tr>
          <td>아이디</td>
          <td>
            <!-- 아이디 입력 필드 -->
            <input type="text" name="userid" size="20" id="userid" />
            
            <!-- 
                중복 체크 확인용 hidden 필드
                - 중복 체크 팝업에서 "사용" 버튼을 클릭하면 이 값이 설정됨
                - JoinServlet에서 이 값이 userid와 일치하는지 확인하여 중복 체크 여부 검증
            -->
            <input type="hidden" name="reid" size="20" />
            
            <!-- 중복 체크 버튼: 팝업으로 아이디 중복 확인 -->
            <input type="button" value="중복 체크" onclick="idCheck()" />
          </td>
        </tr>
        
        <!-- 비밀번호 입력 행 -->
        <tr>
          <td>암 호</td>
          <td>
            <!-- 비밀번호 입력 필드 (입력값이 가려짐) -->
            <input type="password" name="pwd" size="20" />
          </td>
        </tr>
        
        <!-- 비밀번호 확인 입력 행 -->
        <tr height="30">
          <td width="80">암호 확인</td>
          <td>
            <!-- 비밀번호 확인 입력 필드 -->
            <input type="password" name="pwd_check" size="20" />
          </td>
        </tr>
        
        <!-- 이메일 입력 행 -->
        <tr>
          <td>이메일</td>
          <td>
            <input type="text" name="email" size="20" />
          </td>
        </tr>
        
        <!-- 전화번호 입력 행 -->
        <tr>
          <td>전화번호</td>
          <td>
            <input type="text" name="phone" size="20" />
          </td>
        </tr>
        
        <!-- 등급 선택 행 -->
        <tr>
          <td>등급</td>
          <td>
            <!-- 
                라디오 버튼으로 등급 선택
                - value="0": 일반회원 (기본 선택)
                - value="1": 관리자
            -->
            <input type="radio" name="admin" value="0" checked="checked" />
            일반회원 
            <input type="radio" name="admin" value="1" /> 관리자
          </td>
        </tr>
        
        <!-- 버튼 행 -->
        <tr>
          <td colspan="2" align="center">
            <!-- 
                확인 버튼: 폼 제출
                - onclick="return joinCheck()": 클릭 시 joinCheck() 함수 실행하여 유효성 검증
                  - 검증 통과 시 폼 제출 진행
                  - 검증 실패 시 폼 제출 중단
            -->
            <input type="submit" value="확인" onclick="return joinCheck()" />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
            
            <!-- 취소 버튼: 로그인 페이지로 이동 -->
            <input type="button" value="취소" onclick="cancelJoin()" />
          </td>
        </tr>
        
        <!-- 에러 메시지 표시 행 -->
        <tr>
          <td colspan="2" align="center">
            <!-- 
                JSTL을 사용하여 에러 메시지 표시
                - JoinServlet에서 전달한 message가 있으면 표시
                - 예: "아이디 중복 체크를 해주세요.", "회원 가입이 완료되었습니다." 등
            -->
            <c:if test="${not empty message}">
              <div>${message}</div>
            </c:if>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>
