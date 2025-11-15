<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 
    로그인 체크: 세션에 loginUser가 없으면 로그인 페이지로 자동 이동
    - 보안 기능: 로그인하지 않은 사용자는 메인 페이지에 접근할 수 없음
    - JSTL의 <c:if>와 <jsp:forward>를 사용하여 구현
-->
<c:if test = "${empty loginUser }">
	<jsp:forward page="/login.do" />
</c:if>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 페이지</title>
</head>
<body>
	<!-- 환영 메시지 -->
	<h1>반갑습니다!</h1>
	<p>
		<!-- 
			EL 표현식을 사용하여 세션에 저장된 회원 이름 표시
			- ${loginUser.name}: 세션의 loginUser 객체에서 name 속성 가져오기
			- LoginServlet에서 세션에 저장한 MemberVO 객체의 getName() 메서드 호출
		-->
		<span>${loginUser.name}</span>님<br>
		오늘도 멋진 하루 보내세요.
	</p>

	<!-- 회원 정보 표시 영역 -->
	<div>
		<!-- 아이디 표시 -->
		<p>ID : <strong>${loginUser.userid}</strong></p>
		
		<!-- 이메일 표시 -->
		<p>Email : <strong>${loginUser.email}</strong></p>
		
		<!-- 전화번호 표시 -->
		<p>Phone : <strong>${loginUser.phone}</strong></p>
		
		<!-- 
			등급 표시 (삼항 연산자 사용)
			- ${loginUser.admin == 1 ? '관리자' : '일반회원'}
			- admin 값이 1이면 "관리자", 아니면 "일반회원" 표시
		-->
		<p>Admin : <strong>${loginUser.admin == 1 ? '관리자' : '일반회원'}</strong></p>
	</div>

	<!-- 기능 버튼 영역 -->
	<form method="post" action="<%=request.getContextPath()%>/logout.do">
		<!-- 
			로그아웃 버튼
			- type="submit": 폼 제출 버튼
			- action: LogoutServlet의 /logout.do로 POST 요청 전송
		-->
		<button type="submit">로그아웃</button>
		
		<!-- 
			회원정보변경 버튼
			- type="button": 폼 제출하지 않는 일반 버튼
			- onclick: UpdateServlet의 /memberUpdate.do로 이동 (GET 요청)
			- userid 파라미터를 URL에 포함하여 전달
		-->
		<button type="button" onclick="location.href='<%=request.getContextPath()%>/memberUpdate.do?userid=${loginUser.userid}'">
			회원정보변경
		</button>
	</form>
</body>
</html>
