<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/shopping.css">
</head>
<body>
   <div id="wrap" align="center">
      <h1>상품 삭제 - 관리자 페이지</h1>
      <form action="${pageContext.request.contextPath}/productDelete.do" method="post" onsubmit="return confirm('정말 삭제하시겠습니까?')">
         <table>
            <tr>
               <td><c:choose>
                     <c:when test="${empty product.pictureUrl}">
                        <img src="${pageContext.request.contextPath}/upload/noimage.gif">
                     </c:when>
                     <c:otherwise>
                        <img src="${pageContext.request.contextPath}/upload/${product.pictureUrl}">
                     </c:otherwise>
                  </c:choose></td>
               <td>
                  <table>
                     <tr>
                        <th style="width: 80px">상 품 명</th>
                        <td>${product.name}</td>
                     </tr>
                     <tr>
                        <th>가 격</th>
                        <td>${product.price}원</td>
                     </tr>
                     <tr>
                        <th>설 명</th>
                        <td><div style="height: 220px; width: 100%">
                        ${product.description}</div></td>
                     </tr>
                  </table>
               </td>
            </tr>
         </table>
         <br> <input type="hidden" name="code" value="${product.code}">
         <input type="submit" value="삭제"> 
         <input type="button" value="목록" onclick="location.href='${pageContext.request.contextPath}/productList.do'">
      </form>
   </div>
</body>
</html>