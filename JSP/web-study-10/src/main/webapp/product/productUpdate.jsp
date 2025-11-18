<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/shopping.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/script/product.js"></script>
</head>
<body>
   <div id="wrap" align="center">
      <h1>상품 수정 - 관리자 페이지</h1>
      <form method="post" enctype="multipart/form-data" name="frm" action="${pageContext.request.contextPath}/productUpdate.do">
        <!-- code 값은 히든으로 숨겨서 서버에 전송 -->
        <input type="hidden" name="code" value="${product.code}"> 
        <!-- 이미지 변경이 없으면 기존 이미지 그대로 사용 -->
         <input type="hidden" name="nonmakeImg" value="${product.pictureUrl}">
         <!-- 상품 정보 표시 테이블 -->
         <table>
            <!-- 상품 이미지 표시 행 -->
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
                        <th style="width: 80px">상품명</th>
                        <td><input type="text" name="name" value="${product.name}"
                           size="80"></td>
                     </tr>
                     <tr>
                        <th>가 격</th>
                        <td><input type="text" name="price"
                           value="${product.price}"> 원</td>
                     </tr>
                     <tr>
                        <th>사 진</th>
                        <td><input type="file" name="pictureUrl" accept="image/*"><br>
                           (주의사항 : 이미지를 변경하고자 할때만 선택하시오)</td>
                     </tr>
                     <tr>
                        <th>설 명</th>
                        <td><textarea cols="90" rows="10" name="description">${product.description}</textarea>
                        </td>
                     </tr>
                  </table>
               </td>
            </tr>
         </table>
         <br> 
         <input type="submit" value="수정" onclick="return productCheck()"> 
        <input type="reset" value="다시작성"> <input type="button" value="목록" onclick="location.href='${pageContext.request.contextPath}/productList.do'">
      </form>
   </div>
</body>
</html>