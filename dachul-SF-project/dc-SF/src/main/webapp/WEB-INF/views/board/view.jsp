<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 상세보기 - 대철이제철 게시판</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <jsp:include page="../common/header.jsp" />
    <div class="container">
        <div class="board-view-header">
            <h2>게시글 상세보기</h2>
        </div>
        
        <div class="board-view">
            <!-- 게시글 제목 -->
            <div class="board-view-title">
                ${board.title}
            </div>
            
            <!-- 메타 정보 -->
            <div class="board-view-meta">
                <div class="meta-item">
                    <label>작성자</label>
                    <span>${board.writer}</span>
                </div>
                
                <div class="meta-item">
                    <label>작성일</label>
                    <span>
                        <fmt:parseDate value="${board.regdate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                        <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </span>
                </div>
                
                <div class="meta-item">
                    <label>조회수</label>
                    <span>${board.hit}</span>
                </div>
            </div>
            
            <!-- 게시글 내용 -->
            <div class="board-content-wrapper">
                <div class="board-content">${board.content}</div>
            </div>
        </div>
        
        <!-- 액션 버튼 -->
        <div class="action-buttons">
            <sec:authorize access="isAuthenticated()">
                <sec:authentication property="principal.username" var="currentUser"/>
                <c:if test="${currentUser == board.writer}">
                    <!-- 작성자만 수정 가능 -->
                    <a href="${pageContext.request.contextPath}/board/update?seq=${board.seq}" 
                       class="btn-edit">수정</a>
                </c:if>
                <c:if test="${currentUser == board.writer}">
                    <!-- 작성자는 삭제 가능 -->
                    <a href="${pageContext.request.contextPath}/board/delete?seq=${board.seq}" 
                       class="btn-delete"
                       onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
                </c:if>
                <sec:authorize access="hasRole('ADMIN')">
                    <c:if test="${currentUser != board.writer}">
                        <!-- 관리자도 삭제 가능 -->
                        <a href="${pageContext.request.contextPath}/board/delete?seq=${board.seq}" 
                           class="btn-delete"
                           onclick="return confirm('관리자 권한으로 삭제하시겠습니까?')">삭제</a>
                    </c:if>
                </sec:authorize>
            </sec:authorize>
            <a href="${pageContext.request.contextPath}/board/list" class="btn-list">목록으로</a>
            <sec:authorize access="hasRole('ADMIN')">
                <a href="${pageContext.request.contextPath}/admin/main" class="btn-list">관리자</a>
            </sec:authorize>
        </div>
        
        <!-- 댓글 작성 폼 -->
        <div class="reply-section">
            <h3>댓글</h3>
            <sec:authorize access="isAuthenticated()">
                <sec:authentication property="principal.username" var="currentUser"/>
                <form id="replyForm" class="reply-form">
                    <input type="hidden" name="bno" value="${board.seq}" />
                    <div class="reply-input-group" style="display: flex; align-items: flex-start; gap: 8px; width: 100%; padding: 12px; border: 1px solid #E5E5E5; border-radius: 4px; background-color: #FFFFFF; box-sizing: border-box;">
                        <textarea name="replyText" id="replyText" class="reply-textarea" rows="3" placeholder="댓글을 입력하세요" required style="flex: 1; width: 0; min-width: 0; padding: 12px 16px; border: none; border-radius: 4px; font-size: 14px; font-family: inherit; resize: vertical; min-height: 80px; max-height: 200px; line-height: 1.6; color: #222222; background-color: transparent; box-sizing: border-box; outline: none;"></textarea>
                        <button type="submit" class="addReplyBtn" style="flex-shrink: 0; width: auto; min-width: 70px; max-width: 100px; padding: 12px 20px; height: 80px; background-color: #4A5568; color: #FFFFFF; border: none; border-radius: 4px; font-size: 14px; font-weight: 500; cursor: pointer; white-space: nowrap; box-sizing: border-box;">등록</button>
                    </div>
                </form>
            </sec:authorize>
            <sec:authorize access="!isAuthenticated()">
                <p style="text-align: center; color: var(--text-secondary); padding: 20px;">댓글을 작성하려면 로그인이 필요합니다.</p>
            </sec:authorize>
        </div>
        
        <!-- 댓글 목록 -->
        <div class="reply-list-section">
            <h4>댓글 목록</h4>
            <ul class="list-group replyList">
            </ul>
            
            <!-- 댓글 페이징 -->
            <div class="pagination-wrapper" style="margin-top: 20px;">
                <ul class="pagination">
                </ul>
            </div>
        </div>
    </div>
    
    <!-- Axios -->
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    
    <script type="text/javascript">
        // 변수 선언
        let currentPage = 1;
        let currentSize = 10;
        let editingRno = null; // 현재 수정 중인 댓글 번호
        const bno = parseInt(<c:out value="${board.seq}"/>);
        const contextPath = '<c:out value="${pageContext.request.contextPath}"/>';
        <sec:authorize access="isAuthenticated()">
        <sec:authentication property="principal.username" var="currentUser"/>
        const currentUser = '<c:out value="${currentUser}"/>';
        </sec:authorize>
        <sec:authorize access="!isAuthenticated()">
        const currentUser = null;
        </sec:authorize>
        
        const replyForm = document.querySelector("#replyForm");
        const replyList = document.querySelector(".replyList");
        
        // 댓글 등록
        if(replyForm) {
            document.querySelector(".addReplyBtn").addEventListener("click", e => {
                e.preventDefault();
                e.stopPropagation();
                
                const formData = new FormData(replyForm);
                const data = Object.fromEntries(formData.entries());
                data.bno = parseInt(data.bno);
                data.replyer = currentUser;
                
                axios.post(contextPath + '/replies', JSON.stringify(data), {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(res => {
                    replyForm.reset();
                    alert("댓글이 등록되었습니다.");
                    editingRno = null;
                    getReplies(1, true);
                })
                .catch(err => {
                    console.error("댓글 등록 실패:", err);
                    alert("댓글 등록에 실패했습니다.");
                });
            }, false);
        }
        
        // 댓글 목록 조회
        function getReplies(pageNum, goLast) {
            axios.get(contextPath + '/replies/' + bno + '/list', {
                params: {
                    page: pageNum || currentPage,
                    size: currentSize
                }
            })
            .then(res => {
                const data = res.data;
                const {totalCount, page, size} = data;
                
                if(goLast && totalCount > 0 && (totalCount > (page * size))) {
                    const lastPage = Math.ceil(totalCount / size);
                    getReplies(lastPage);
                } else {
                    currentPage = page;
                    currentSize = size;
                    printReplies(data);
                }
            })
            .catch(err => {
                console.error("댓글 목록 조회 실패:", err);
            });
        }
        
        // 초기 댓글 목록 로드
        getReplies(1);
        
        // 댓글 목록 출력
        function printReplies(data) {
            const {replyDTOList, page, size, prev, next, start, end, pageNums, totalCount} = data;
            
            let liStr = "";
            
            if(!replyDTOList || replyDTOList.length === 0) {
                liStr = '<li class="list-group-item" style="text-align: center; padding: 40px; color: var(--text-secondary);">댓글이 없습니다.</li>';
            } else {
                for(let i = 0; i < replyDTOList.length; i++) {
                    const replyDTO = replyDTOList[i];
                    const rno = replyDTO.rno || '';
                    const replyText = replyDTO.replyText || '';
                    const replyer = replyDTO.replyer || '';
                    const replyDate = replyDTO.replyDate || '';
                    const canEdit = currentUser && replyer === currentUser;
                    
                    // 수정 모드인지 확인
                    if(editingRno === rno) {
                        // 수정 모드
                        liStr += '<li class="list-group-item reply-edit-mode" data-rno="' + rno + '">' +
                                    '<div style="margin-bottom: 8px;">' +
                                        '<textarea class="reply-edit-text" style="width: 100%; padding: 8px; border: none; border-radius: 4px; font-size: 14px; font-family: inherit; resize: vertical; min-height: 60px;">' + escapeHtml(replyText) + '</textarea>' +
                                    '</div>' +
                                    '<div style="display: flex; justify-content: flex-end; gap: 8px;">' +
                                        '<button class="reply-save-btn" data-rno="' + rno + '" style="background: var(--primary); color: white; border: none; padding: 6px 16px; border-radius: 4px; font-size: 13px; cursor: pointer;">저장</button>' +
                                        '<button class="reply-cancel-btn" data-rno="' + rno + '" style="background: var(--secondary); color: white; border: none; padding: 6px 16px; border-radius: 4px; font-size: 13px; cursor: pointer;">취소</button>' +
                                    '</div>' +
                                '</li>';
                    } else {
                        // 일반 모드
                        liStr += '<li class="list-group-item" data-rno="' + rno + '">' +
                                    '<div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 8px;">' +
                                        '<div style="flex: 1;">' +
                                            '<span class="reply-text" style="font-weight: 500; color: var(--text-primary); font-size: 14px; white-space: pre-wrap;">' + escapeHtml(replyText || '(내용 없음)') + '</span>' +
                                        '</div>' +
                                        '<div style="font-size: 12px; color: var(--text-secondary); margin-left: 16px; white-space: nowrap;">' + formatDate(replyDate) + '</div>' +
                                    '</div>' +
                                    '<div style="display: flex; justify-content: space-between; align-items: center;">' +
                                        '<span style="font-size: 13px; color: var(--text-secondary);">' + escapeHtml(replyer || '(작성자 없음)') + '</span>' +
                                        (canEdit ? '<div style="display: flex; gap: 6px;">' +
                                            '<button class="reply-edit-btn" data-rno="' + rno + '" style="background: var(--primary); color: white; border: none; padding: 4px 12px; border-radius: 4px; font-size: 12px; cursor: pointer;">수정</button>' +
                                            '<button class="reply-delete-btn" data-rno="' + rno + '" style="background: var(--danger); color: white; border: none; padding: 4px 12px; border-radius: 4px; font-size: 12px; cursor: pointer;">삭제</button>' +
                                        '</div>' : '') +
                                    '</div>' +
                                '</li>';
                    }
                }
            }
            
            if(replyList) {
                replyList.innerHTML = liStr;
            }
            
            // 페이징 출력
            const paginationEl = document.querySelector(".pagination");
            if(paginationEl) {
                let pageStr = "";
                
                if(prev) {
                    pageStr += '<li class="page-item">' +
                                '<a class="page-link" href="' + (start - 1) + '">이전</a>' +
                              '</li>';
                }
                
                for(let i of pageNums) {
                    pageStr += '<li class="page-item ' + (page == i ? 'active' : '') + '">' +
                                '<a class="page-link" href="' + i + '">' + i + '</a>' +
                              '</li>';
                }
                
                if(next) {
                    pageStr += '<li class="page-item">' +
                                '<a class="page-link" href="' + (end + 1) + '">다음</a>' +
                              '</li>';
                }
                
                paginationEl.innerHTML = pageStr;
            }
        }
        
        // HTML 이스케이프 함수
        function escapeHtml(text) {
            if(!text) return '';
            const div = document.createElement('div');
            div.textContent = text;
            return div.innerHTML;
        }
        
        // 날짜 포맷 함수
        function formatDate(dateStr) {
            if(!dateStr) return '';
            try {
                const date = new Date(dateStr);
                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
                const day = String(date.getDate()).padStart(2, '0');
                const hours = String(date.getHours()).padStart(2, '0');
                const minutes = String(date.getMinutes()).padStart(2, '0');
                return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
            } catch(e) {
                return dateStr;
            }
        }
        
        // 페이징 클릭 이벤트
        const paginationEl = document.querySelector(".pagination");
        if(paginationEl) {
            paginationEl.addEventListener("click", e => {
                e.preventDefault();
                e.stopPropagation();
                
                const target = e.target.closest('a');
                if(target) {
                    const href = target.getAttribute("href");
                    if(href) {
                        editingRno = null;
                        getReplies(parseInt(href));
                    }
                }
            }, false);
        }
        
        // 댓글 수정/삭제 이벤트 (이벤트 위임)
        if(replyList) {
            replyList.addEventListener("click", function(e) {
                e.stopPropagation();
                
                // 수정 버튼 클릭
                if(e.target.classList.contains('reply-edit-btn')) {
                    const rno = parseInt(e.target.getAttribute('data-rno'));
                    if(!rno) return;
                    
                    const listItem = e.target.closest('li');
                    const replyTextEl = listItem.querySelector('.reply-text');
                    if(!replyTextEl) return;
                    
                    const replyText = replyTextEl.textContent;
                    editingRno = rno;
                    
                    // 수정 모드로 전환
                    const editHtml = '<li class="list-group-item reply-edit-mode" data-rno="' + rno + '">' +
                                        '<div style="margin-bottom: 8px;">' +
                                            '<textarea class="reply-edit-text" style="width: 100%; padding: 8px; border: none; border-radius: 4px; font-size: 14px; font-family: inherit; resize: vertical; min-height: 60px;">' + escapeHtml(replyText) + '</textarea>' +
                                        '</div>' +
                                        '<div style="display: flex; justify-content: flex-end; gap: 8px;">' +
                                            '<button class="reply-save-btn" data-rno="' + rno + '" style="background: var(--primary); color: white; border: none; padding: 6px 16px; border-radius: 4px; font-size: 13px; cursor: pointer;">저장</button>' +
                                            '<button class="reply-cancel-btn" data-rno="' + rno + '" style="background: var(--secondary); color: white; border: none; padding: 6px 16px; border-radius: 4px; font-size: 13px; cursor: pointer;">취소</button>' +
                                        '</div>' +
                                    '</li>';
                    listItem.outerHTML = editHtml;
                    
                    // 포커스 설정
                    const textarea = document.querySelector('.reply-edit-text');
                    if(textarea) {
                        textarea.focus();
                        textarea.setSelectionRange(textarea.value.length, textarea.value.length);
                    }
                    return;
                }
                
                // 저장 버튼 클릭
                if(e.target.classList.contains('reply-save-btn')) {
                    const rno = parseInt(e.target.getAttribute('data-rno'));
                    if(!rno) return;
                    
                    const listItem = e.target.closest('li');
                    const textarea = listItem.querySelector('.reply-edit-text');
                    if(!textarea) return;
                    
                    const replyText = textarea.value.trim();
                    
                    if(!replyText) {
                        alert("댓글 내용을 입력해주세요.");
                        return;
                    }
                    
                    axios.put(contextPath + '/replies/' + rno, JSON.stringify({ replyText: replyText }), {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then(function(res) {
                        editingRno = null;
                        alert("댓글이 수정되었습니다.");
                        getReplies(currentPage);
                    })
                    .catch(function(err) {
                        console.error("댓글 수정 실패:", err);
                        alert("댓글 수정에 실패했습니다.");
                    });
                    return;
                }
                
                // 취소 버튼 클릭
                if(e.target.classList.contains('reply-cancel-btn')) {
                    editingRno = null;
                    getReplies(currentPage);
                    return;
                }
                
                // 삭제 버튼 클릭
                if(e.target.classList.contains('reply-delete-btn')) {
                    const rno = parseInt(e.target.getAttribute('data-rno'));
                    if(!rno) return;
                    
                    if(!confirm("정말 삭제하시겠습니까?")) {
                        return;
                    }
                    
                    axios.delete(contextPath + '/replies/' + rno)
                    .then(function(res) {
                        editingRno = null;
                        alert("댓글이 삭제되었습니다.");
                        
                        // 삭제 후 현재 페이지가 비어있을 수 있으므로 처리
                        getReplies(currentPage).then(() => {
                            // 현재 페이지에 댓글이 없고 이전 페이지가 있으면 이전 페이지로 이동
                            const replyList = document.querySelector(".replyList");
                            if(replyList && replyList.children.length === 1 && 
                               replyList.children[0].textContent.includes('댓글이 없습니다') && 
                               currentPage > 1) {
                                getReplies(currentPage - 1);
                            }
                        });
                    })
                    .catch(function(err) {
                        console.error("댓글 삭제 실패:", err);
                        alert("댓글 삭제에 실패했습니다.");
                    });
                    return;
                }
            });
        }
    </script>
    <jsp:include page="../common/footer.jsp" />
</body>
</html>
