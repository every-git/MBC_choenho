<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ include
file="/WEB-INF/views/includes/header.jsp" %>

<div class="row justify-content-center">
  <div class="col-lg-12">
    <div class="card shadow mb-4">
      <div class="card-header py-3">
        <h6 class="m-0 fw-bold text-primary">Board Read</h6>
      </div>
      <div class="card-body">
        <div class="mb-3 input-group input-group-lg">
          <span class="input-group-text">Bno</span>
          <input
            type="text"
            class="form-control"
            value="<c:out value='${board.bno}'/>"
            readonly
          />
        </div>

        <div class="mb-3 input-group input-group-lg">
          <span class="input-group-text">Title</span>
          <input
            type="text"
            name="title"
            class="form-control"
            value="<c:out value='${board.title}'/>"
            readonly
          />
        </div>

        <div class="mb-3 input-group input-group-lg">
          <span class="input-group-text">Content</span>
          <textarea class="form-control" name="content" rows="3" readonly>
<c:out value="${board.content}"/></textarea
          >
        </div>

        <div class="mb-3 input-group input-group-lg">
          <span class="input-group-text">Writer</span>
          <input
            type="text"
            name="writer"
            class="form-control"
            value="<c:out value='${board.writer}'/>"
            readonly
          />
        </div>

        <div class="mb-3 input-group input-group-lg">
          <span class="input-group-text">RegDate</span>
          <input
            type="text"
            name="regDate"
            class="form-control"
            value="<c:out value='${board.createdDate}'/>"
            readonly
          />
        </div>

        <div class="float-end">
          <c:url var="readUrl" value="/board/list">
            <c:param name="page" value="${page}" />
            <c:param name="size" value="${size}" />
            <c:param name="types" value="${types}" />
            <c:param name="keyword" value="${keyword}" />
          </c:url>
          <a href="${readUrl}">
            <button type="button" class="btn btn-info btnList">LIST</button>
          </a>
          <c:if test="${!board.delFlag}">
            <a href="/board/modify/${board.bno}">
              <button type="button" class="btn btn-warning btnModify">
                MODIFY
              </button>
            </a>
          </c:if>
        </div>
      </div>
    </div>
  </div>
</div>

<div class="col-lg-12">
  <div class="card shadow mb-4">
    <div class="m-4">
      <!--댓글 작성 폼 -->
      <form id="replyForm" class="mt-4">
        <!-- 게시글 번호 hidden 처리 -->
        <input type="hidden" name="bno" value="${board.bno}" />

        <div class="mb-3 input-group input-group-lg">
          <span class="input-group-text">Replyer</span>
          <input type="text" name="replyer" class="form-control" required />
        </div>

        <div class="mb-3 input-group">
          <span class="input-group-text">Reply Text</span>
          <textarea
            name="replyText"
            class="form-control"
            rows="3"
            required
          ></textarea>
        </div>

        <div class="text-end">
          <button type="submit" class="btn btn-primary addReplyBtn">
            Submit Reply
          </button>
        </div>
      </form>
      <!-- 댓글 작성 폼 끝 -->
    </div>
  </div>
</div>

<div class="col-lg-12">
  <div class="card shadow mb-4">
    <div class="m-4">
      <!--댓글 목록 -->
      <ul class="list-group replyList">
        <li class="list-group-item">
          <div class="d-flex justify-content-between">
            <div><strong>번호</strong> - 댓글 내용</div>
            <div class="text-muted small">작성일</div>
          </div>
          <div class="mt-1 text-secondary small">작성자</div>
        </li>
      </ul>
      <!-- 댓글 목록 -->
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

<script type="text/javascript">
  console.log("스크립트 시작");
  
  // DOM이 로드된 후 실행
  document.addEventListener("DOMContentLoaded", function() {
    console.log("DOM 로드 완료");
    
    const bno = ${board.bno};
    console.log("bno 값:", bno);
    
    if(!bno) {
      console.error("bno 값이 없습니다!");
      return;
    }
    
    let currentPage = 1;
    let currentSize = 10;

    const replyForm = document.querySelector("#replyForm");
    const addReplyBtn = document.querySelector(".addReplyBtn");
    const replyList = document.querySelector(".replyList");
    
    if(!replyForm) {
      console.error("replyForm을 찾을 수 없습니다.");
    }
    if(!addReplyBtn) {
      console.error("addReplyBtn을 찾을 수 없습니다.");
    }
    if(!replyList) {
      console.error("replyList를 찾을 수 없습니다.");
    }

    // 댓글 등록 버튼 이벤트
    if(addReplyBtn) {
      addReplyBtn.addEventListener("click", (e) => {
        e.preventDefault();
        e.stopPropagation();
        console.log("댓글 등록 버튼 클릭");

        const formData = new FormData(replyForm);

        // FormData를 URL-encoded 형식으로 변환 (Spring이 ReplyDTO로 자동 바인딩)
        const params = new URLSearchParams();
        params.append("bno", formData.get("bno"));
        params.append("replyer", formData.get("replyer"));
        params.append("replyText", formData.get("replyText"));

        console.log("댓글 등록 요청:", params.toString());

        axios
          .post("/reply/", params.toString(), {
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
          })
          .then(function (response) {
            console.log("댓글 등록 성공:", response.data);
            alert("댓글이 등록되었습니다.");
            replyForm.reset();
            // 댓글 목록 다시 불러오기
            getReplies(1);
          })
          .catch(function (error) {
            console.error("댓글 등록 실패:", error);
            alert(
              "댓글 등록에 실패했습니다: " + (error.response?.data || error.message)
            );
          });
      });
    }

    function getReplies(pageNum){
      console.log("댓글 목록 요청 시작 - pageNum:", pageNum, "bno:", bno);
      axios.get(`/reply/${bno}/list`, {
        params: {
          page: pageNum || currentPage,
          size: currentSize
        }
      }).then(
        res => {
          console.log("댓글 목록 응답 성공:", res);
          console.log("댓글 목록 응답 데이터:", res.data);
          const data = res.data;
          currentPage = data.page || pageNum || 1;
          currentSize = data.size || 10;
          // 댓글 목록 표시
          displayReplies(data);
        }
      )
      .catch(
        error => {
          console.error("댓글 목록 조회 실패:", error);
          console.error("에러 상세:", error.response);
          if(error.response) {
            console.error("에러 상태:", error.response.status);
            console.error("에러 데이터:", error.response.data);
          }
        }
      );
    }

    function displayReplies(data){
      console.log("displayReplies 호출 시작");
      console.log("받은 데이터:", data);
      
      if(!data) {
        console.error("데이터가 없습니다!");
        return;
      }
      
      const {replyDTOList, page, size, prev, next, start, end, pageNums} = data;
      console.log("replyDTOList:", replyDTOList);
      console.log("replyDTOList 길이:", replyDTOList ? replyDTOList.length : "null");
  
      let liStr = "";

      if(!replyList) {
        console.error("replyList 요소를 찾을 수 없습니다.");
        return;
      }
      
      // 기존 댓글 목록 초기화 (헤더만 남기기)
      replyList.innerHTML = `
        <li class="list-group-item">
          <div class="d-flex justify-content-between">
            <div><strong>번호</strong> - 댓글 내용</div>
            <div class="text-muted small">작성일</div>
          </div>
          <div class="mt-1 text-secondary small">작성자</div>
        </li>
      `;
      
      if(replyDTOList && replyDTOList.length > 0){
        console.log("댓글 개수:", replyDTOList.length);
        for(let i = 0; i < replyDTOList.length; i++){
          const replyDTO = replyDTOList[i];
          console.log("댓글 " + i + ":", replyDTO);
          const replyDate = replyDTO.replyDate || '';
          const replyText = replyDTO.replyText || '';
          const replyer = replyDTO.replyer || '';
          const rno = replyDTO.rno || '';
          
          liStr += `
            <li class="list-group-item" data-rno="${rno}">
              <div class="d-flex justify-content-between">
                <div>
                  <strong>${rno}</strong> - ${replyText}
                </div>
                <div class="text-muted small">
                  ${replyDate}
                </div>
              </div>
              <div class="mt-1 text-secondary small">
                ${replyer}
              </div>
            </li>`;
        }
        replyList.innerHTML += liStr;
        console.log("댓글 목록 표시 완료");
      } else {
        console.log("댓글이 없습니다.");
        liStr = `<li class="list-group-item text-center text-muted">댓글이 없습니다.</li>`;
        replyList.innerHTML += liStr;
      }
    }

    // 페이지 로드 시 댓글 목록 불러오기
    console.log("초기 댓글 목록 불러오기 시작");
    getReplies(1);
  });

</script>
<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
