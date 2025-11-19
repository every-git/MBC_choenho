<%--
	게시글 목록을 표시하는 JSP 페이지
	
	역할: BoardListAction에서 전달받은 게시글 목록을 테이블 형태로 화면에 출력
	
	데이터 흐름:
	1. BoardListAction에서 request.setAttribute("boardList", list)로 데이터 저장
	2. forward를 통해 이 JSP 페이지로 이동
	3. EL 표현식 ${boardList}로 저장된 데이터 접근
	4. JSTL의 <c:forEach>로 리스트를 반복하여 각 게시글 출력
	
	사용 기술:
	- JSTL (JSP Standard Tag Library): <c:forEach> 태그 사용
	- EL (Expression Language): ${boardList}, ${board.num} 등
	- fmt 태그: 날짜 포맷팅
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%-- JSTL 태그 라이브러리 선언 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게시글 리스트</title>
<%-- CSS 파일 링크 (contextPath를 사용하여 절대 경로로 지정) --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/shopping.css">
</head>
<body>
   <div id="wrap" align="center">
      <h1>게시글 리스트</h1>
      <table class="list">
         <%-- 게시글 등록 링크 --%>
         <tr>
            <td colspan="5" style="border: white; text-align: right">
               <a href="BoardServlet?command=board_write_form">게시글 등록</a>
            </td>
         </tr>
         <%-- 테이블 헤더 --%>
         <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
            <th>조회</th>
         </tr>
         <%--
            JSTL의 <c:forEach> 태그를 사용하여 게시글 목록 반복 출력
            - items="${boardList}": BoardListAction에서 저장한 "boardList" 속성 접근
            - var="board": 각 반복에서 사용할 변수명 (BoardVO 객체)
            - ${boardList}는 request.getAttribute("boardList")와 동일
         --%>
         <c:forEach var="board" items="${boardList}">
            <tr class="record">
               <%-- 게시글 번호 출력 --%>
               <td>${board.num}</td>
               <%-- 게시글 제목 출력 (클릭 시 상세보기 페이지로 이동) --%>
               <td>
                  <a href="BoardServlet?command=board_view&num=${board.num}">
                     ${board.title}
                  </a>
               </td>
               <%-- 작성자 이름 출력 --%>
               <td>${board.name}</td>
               <%-- 작성일 출력 (날짜 포맷팅 적용) --%>
               <%-- fmt:formatDate 태그로 Timestamp를 읽기 쉬운 날짜 형식으로 변환 --%>
               <td><fmt:formatDate value="${board.writedate}" /></td>
               <%-- 조회수 출력 --%>
               <td>${board.readcount}</td>
            </tr>
         </c:forEach>
         <%--
            게시글이 없으면 위의 <c:forEach>가 실행되지 않아
            테이블에 데이터 행이 표시되지 않음
         --%>
      </table>
   </div>
</body>
</html>