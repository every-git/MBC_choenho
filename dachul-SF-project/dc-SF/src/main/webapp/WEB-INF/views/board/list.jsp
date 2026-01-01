<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판 목록 - 대철이제철 게시판</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h2>게시판</h2>
        
        <div class="board-header">
            <sec:authorize access="isAuthenticated()">
                <a href="${pageContext.request.contextPath}/board/write">글쓰기</a>
            </sec:authorize>
        </div>
        
        <div class="board-list">
            <c:forEach var="board" items="${dto.boardDTOList}">
                <div class="board-item">
                    <div class="board-title">
                        <a href="${pageContext.request.contextPath}/board/view?seq=${board.seq}">
                            ${board.title}
                        </a>
                    </div>
                    <div class="board-meta">
                        <span class="board-writer">${board.writer}</span>
                        <span class="board-separator">|</span>
                        <span class="board-date">
                            <fmt:parseDate value="${board.regdate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
                            <fmt:formatDate value="${parsedDate}" pattern="yyyy.MM.dd HH:mm"/>
                        </span>
                        <span class="board-separator">|</span>
                        <span class="board-hit">조회 ${board.hit}</span>
                    </div>
                </div>
            </c:forEach>
            
            <c:if test="${empty dto.boardDTOList}">
                <div class="board-empty">
                    등록된 게시글이 없습니다.
                </div>
            </c:if>
        </div>
        
        <!-- 페이징 처리 -->
        <div class="pagination-wrapper">
            <ul class="pagination justify-content-center">
                <c:if test="${dto.prev}">
                    <li class="page-item">
                        <a class="page-link" href="${dto.start - 1}" tabindex="-1">이전</a>
                    </li>
                </c:if>
                
                <c:forEach var="num" items="${dto.pageNums}">
                    <li class="page-item ${dto.page == num ? 'active' : ''}">
                        <a class="page-link" href="${num}">${num}</a>
                    </li>
                </c:forEach>
                
                <c:if test="${dto.next}">
                    <li class="page-item">
                        <a class="page-link" href="${dto.end + 1}">다음</a>
                    </li>
                </c:if>
            </ul>
        </div>
        
        <div class="links">
            <a href="${pageContext.request.contextPath}/">메인으로</a>
            <sec:authorize access="isAuthenticated()">
                <sec:authorize access="hasRole('ADMIN')">
                    <a href="${pageContext.request.contextPath}/admin/main">관리자</a>
                </sec:authorize>
                <a href="${pageContext.request.contextPath}/logout">로그아웃</a>
            </sec:authorize>
        </div>
    </div>
    
    <script type="text/javascript">
        // 페이징 클릭 이벤트 처리
        const pagingDiv = document.querySelector(".pagination");
        if(pagingDiv) {
            pagingDiv.addEventListener("click", (e) => {
                e.preventDefault();
                e.stopPropagation();
                
                const target = e.target;
                const targetPage = target.getAttribute("href");
                const size = ${dto.size} || 10;
                
                if(targetPage) {
                    const params = new URLSearchParams({
                        page: targetPage,
                        size: size
                    });
                    
                    self.location = `${pageContext.request.contextPath}/board/list?${params.toString()}`;
                }
            }, false);
        }
    </script>
</body>
</html>
