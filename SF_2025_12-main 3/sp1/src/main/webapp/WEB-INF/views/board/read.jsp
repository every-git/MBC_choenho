<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page isELIgnored="false" %> <%@ taglib prefix="c"
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

      <div aria-label="댓글 페이지 네비게이션" class="mt-4">
        <ul class="pagination justify-content-center">
          <li class="page-item disabled">
            <a class="page-link" href="#" tabindex="-1">이전</a>
          </li>
          <li class="page-item active">
            <a class="page-link" href="#">1</a>
          </li>
          <li class="page-item">
            <a class="page-link" href="#">2</a>
          </li>
          <li class="page-item">
            <a class="page-link" href="#">3</a>
          </li>
          <li class="page-item">
            <a class="page-link" href="#">다음</a>
          </li>
        </ul>
      </div>
      <!-- 페이징 끝 -->
    </div>
  </div>
</div>

<div
  class="modal fade"
  id="replyModal"
  tabindex="-1"
  aria-labelledby="replyModalLabel"
  aria-hidden="true"
>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="replyModalLabel">댓글 수정 / 삭제</h5>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="modal"
          aria-label="Close"
        ></button>
      </div>

      <div class="modal-body">
        <form id="replyModForm">
          <input type="hidden" name="rno" value="33" />
          <div class="mb-3">
            <label for="replyText" class="form-label">댓글 내용</label>
            <input
              type="text"
              name="replyText"
              id="replyText"
              class="form-control"
              value="Reply Text"
            />
          </div>
        </form>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-primary btnReplyMod">수정</button>
        <button type="button" class="btn btn-danger btnReplyDel">삭제</button>
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
          닫기
        </button>
      </div>
    </div>
  </div>
</div>

<!-- 댓글 목록 -->

<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

<script type="text/javascript">
  <%-- JSP가 이 스크립트 블록을 해석하지 않도록 주석 처리 --%>

    //HTML에서 id가 "replyForm"인 폼(form) 요소를 찾아 변수에 저장합니다.
    const replyForm = document.querySelector("#replyForm");

    // 클래스가 "addReplyBtn"인 버튼을 찾아 클릭 이벤트 리스너를 등록합니다.
    document.querySelector(".addReplyBtn").addEventListener("click", e=>{

        // 폼 제출 시 페이지가 새로고침되는 브라우저의 기본 동작을 막습니다.
        e.preventDefault();

        // 클릭 이벤트가 상위 요소로 퍼져나가는 것(버블링)을 방지합니다.
        e.stopPropagation();

        // 폼(replyForm) 안에 입력된 모든 데이터를 FormData 객체로 생성합니다.
        const formData = new FormData(replyForm);

        // FormData의 데이터를 서버로 보내기 좋게 일반 객체(Key: Value 형태)로 변환합니다.
        // ※ 주의: 소문자 object가 아니라 대문자 Object로 써야 에러가 나지 않습니다.
        const data = Object.fromEntries(formData.entries());


        const jsonData = JSON.stringify(data);

        console.log("---------jsonData------------")
        console.log(data);
        console.log(jsonData)

        // axios 라이브러리를 사용해 "/replies" 주소로 POST 방식의 데이터 전송을 요청합니다.
        axios.post("/replies", jsonData, {
              headers: {
                  // 보내는 데이터의 형식이 JSON임을 서버에 알려줍니다.
                  'Content-Type': 'application/json'
              }
         })
        // 서버 전송에 성공했을 때 실행되는 구간입니다.
        .then(res => {
             console.log("------성공 응답-------------");
             console.log(res.data); // 서버에서 보내준 결과 데이터를 콘솔에 출력합니다.

             // 전송이 성공했으므로 폼에 입력되어 있던 내용을 모두 비웁니다.
             replyForm.reset();

             // 성공 알림
             alert("댓글이 등록되었습니다.");

             // 댓글 목록 새로고침 (마지막 페이지로 이동하여 새 댓글 확인)
             getReplies(1, true);
         })
        // 서버 전송 중 에러가 발생했을 때 실행되는 구간입니다.
        .catch(err => {
             // 에러 메시지와 함께 서버의 응답 내용을 콘솔에 출력합니다.
             console.error("댓글 등록 실패:", err.response);

             // 에러 알림
             const errorMsg = err.response?.data || err.response?.statusText || "댓글 등록에 실패했습니다.";
             alert("오류: " + errorMsg);
        });

    }, false); // 이벤트 캡처링 단계를 사용하지 않겠다는 의미의 기본값입니다.

    let currentPage = 1;
    let currentSize = 10;

    const bno = <c:out value="${board.bno}"/>;

    console.log("bno 값:", bno);

    ////localhost:8080/replies/49999/list?page=2&size=10
    function getReplies(pageNum, goLast){

      axios.get('/replies/' + bno + '/list', {
        params: {
          page: pageNum || currentPage,
          size: currentSize
        }
      }).then(
        res => {
          console.log("=== 댓글 목록 응답 ===", res.data);
          const data = res.data;

          const {totalCount, page, size}  = data;

        if(goLast && (totalCount> (page*size))){
            const lastPage = Math.ceil(totalCount/size);
            getReplies(lastPage);
        }else{
            currentPage = page;
            currentSize = size;
            printReplies(data)
          }

        }
      )
      .catch(err => {
        console.error("댓글 목록 조회 실패:", err);
      });
    }

    getReplies(1);

    const replyList = document.querySelector(".replyList");

    function printReplies(data){
      console.log("=== printReplies 전체 데이터 ===", JSON.stringify(data, null, 2));
      console.log("=== printReplies 데이터 타입 ===", typeof data);

      const {replyDTOList, page,size, prev, next, start, end, pageNums}  = data;

      console.log("replyDTOList:", replyDTOList);
      console.log("replyDTOList 타입:", typeof replyDTOList);
      console.log("replyDTOList 길이:", replyDTOList?.length);
      console.log("replyDTOList가 배열인가?", Array.isArray(replyDTOList));

      let liStr = "";

      if(!replyDTOList || replyDTOList.length === 0){
        liStr = '<li class="list-group-item">댓글이 없습니다.</li>';
        console.log("댓글 목록이 비어있습니다.");
      } else {
        console.log('댓글 ' + replyDTOList.length + '개를 처리합니다.');

        for(let i = 0; i < replyDTOList.length; i++){
          const replyDTO = replyDTOList[i];
          console.log('=== 댓글 ' + (i+1) + '번째 ===');
          console.log("replyDTO 원본:", replyDTO);
          console.log("replyDTO JSON:", JSON.stringify(replyDTO, null, 2));
          console.log("replyDTO 키 목록:", Object.keys(replyDTO));

          // 모든 필드 확인
          for(let key in replyDTO) {
            const typeValue = typeof replyDTO[key];
            console.log('  ' + key + ':', replyDTO[key], '(type: ' + typeValue + ')');
          }

          // 필드 접근 시도 (다양한 방법)
          const rno = replyDTO.rno ?? replyDTO['rno'] ?? '';
          const replyText = replyDTO.replyText ?? replyDTO['replyText'] ?? '';
          const replyer = replyDTO.replyer ?? replyDTO['replyer'] ?? '';
          const replyDate = replyDTO.replyDate ?? replyDTO['replyDate'] ?? '';

          console.log('  추출된 값 - rno: "' + rno + '", replyText: "' + replyText + '", replyer: "' + replyer + '"');

          // 값이 없으면 경고
          if(!rno && !replyText && !replyer) {
            console.warn("⚠️ 모든 필드가 비어있습니다! 데이터 매핑 문제일 수 있습니다.");
          }

          liStr +=    '<li class="list-group-item" data-rno="' + rno + '">' +
                        '<div class="d-flex justify-content-between" >' +
                          '<div>' +
                            '<strong>' + (rno || 'N/A') + '</strong> - ' + (replyText || '(내용 없음)') +
                          '</div>' +
                          '<div class="text-muted small">' +
                            (replyDate || '') +
                          '</div>' +
                        '</div>' +
                        '<div class="mt-1 text-secondary small">' +
                          (replyer || '(작성자 없음)') +
                        '</div>' +
                      '</li>';

        }//end for
      }

      replyList.innerHTML = liStr
      console.log("=== 댓글 목록 렌더링 완료 ===");

      let pageStr = "";

      if(prev){
        pageStr += '<li class="page-item">' +
                        '<a class="page-link" href="' + (start - 1) + '" tabindex="-1">이전</a>' +
                      '</li>';
        }

        for(let i of pageNums){
          pageStr += '<li class="page-item ' + (page == i ? 'active' : '') + '">' +
                        '<a class="page-link" href="' + i + '">' + i + '</a>' +
          '</li>';
        }

        if(next){
          pageStr += '<li class="page-item">' +
                        '<a class="page-link" href="' + (end + 1) + '">다음</a>' +
                      '</li>';
        }

        document.querySelector(".pagination").innerHTML = pageStr;
    }

    document.querySelector(".pagination").addEventListener("click", e=>{
      e.preventDefault();
      e.stopPropagation();

      const target = e.target;
      const href = target.getAttribute("href");
      if(!href){
        return;
      }
      getReplies(href);
    }, false);


    // 모달과 폼을 전역 스코프에서 선언
    const replyModal = new bootstrap.Modal(document.querySelector('#replyModal'));
    const replyModForm = document.querySelector('#replyModForm');
    
    // 현재 선택된 rno를 저장할 전역 변수
    let currentRno = null;

    // 댓글 클릭 이벤트 (댓글 목록이 로드된 후에만 작동)
    if(replyList) {
      replyList.addEventListener("click", function(e){
        //가장 가까운 상위 li 요소를 찾는다.
        const targetLi = e.target.closest("li");
        if(!targetLi) { return; }

        const rno = targetLi.getAttribute("data-rno");
        console.log("targetLi:", targetLi);
        console.log("rno:", rno);

        if(!rno){ return; }

        axios.get('/replies/' + rno).then(function(res) {
          const targetReply = res.data;
          console.log("=== 댓글 조회 성공 ===");
          console.log("조회된 댓글:", targetReply);
          console.log("조회된 댓글의 rno:", targetReply.rno);
          console.log("조회된 댓글의 rno 타입:", typeof targetReply.rno);

          if(targetReply.deflag == false){
            // 전역 변수에 rno 저장 (가장 중요!)
            currentRno = targetReply.rno;
            console.log("=== currentRno 설정 완료 ===");
            console.log("currentRno 값:", currentRno);
            console.log("currentRno 타입:", typeof currentRno);
            
            // 모달 폼에 데이터 채우기
            if(replyModForm) {
              const rnoInput = replyModForm.querySelector("input[name='rno']");
              const replyTextInput = replyModForm.querySelector("input[name='replyText']");
              
              if(rnoInput) {
                rnoInput.value = targetReply.rno;
                console.log("모달 폼에 rno 설정:", rnoInput.value);
              } else {
                console.error("모달 폼의 rno input을 찾을 수 없습니다!");
              }
              if(replyTextInput) {
                replyTextInput.value = targetReply.replyText;
              }
            } else {
              console.error("replyModForm을 찾을 수 없습니다!");
            }

            // 삭제 버튼에 직접 data-rno 속성 추가
            const deleteBtn = document.querySelector(".btnReplyDel");
            if(deleteBtn) {
              deleteBtn.setAttribute("data-rno", targetReply.rno);
              console.log("삭제 버튼에 rno 설정:", deleteBtn.getAttribute("data-rno"));
            } else {
              console.error("삭제 버튼을 찾을 수 없습니다!");
            }

            console.log("모달 표시 전 최종 확인 - currentRno:", currentRno);
            replyModal.show();
          }else{
            alert("삭제된 댓글입니다.");
          }


        }).catch(function(err) {
          console.error("댓글 조회 실패:", err);
        });
      }, false);
    }

    //삭제 - 전역 변수 currentRno를 우선적으로 사용
    document.querySelector(".btnReplyDel").addEventListener("click", function(e){
      e.preventDefault();
      e.stopPropagation();

      console.log("=== 삭제 버튼 클릭 ===");
      console.log("전역 currentRno:", currentRno);
      console.log("currentRno 타입:", typeof currentRno);
      
      // 모달이 열려있는지 확인
      const modal = document.querySelector('#replyModal');
      const isModalOpen = modal && modal.classList.contains('show');
      console.log("모달이 열려있는가?", isModalOpen);

      // 전역 변수 currentRno를 우선적으로 사용
      let rno = currentRno;
      
      // currentRno가 없으면 다른 방법으로 시도
      if(!rno) {
        // 모달 폼에서 가져오기
        const modal = document.querySelector('#replyModal');
        const form = modal ? modal.querySelector('#replyModForm') : null;
        if(form) {
          const rnoInput = form.querySelector("input[name='rno']");
          rno = rnoInput ? rnoInput.value.trim() : null;
          console.log("모달 폼에서 가져온 rno:", rno);
        }
        
        // 여전히 없으면 삭제 버튼의 data-rno 속성에서 가져오기
        if(!rno) {
          const deleteBtn = document.querySelector(".btnReplyDel");
          if(deleteBtn) {
            rno = deleteBtn.getAttribute("data-rno");
            console.log("삭제 버튼의 data-rno:", rno);
          }
        }
      }

      console.log("최종 rno 값:", rno);
      console.log("rno 타입:", typeof rno);

      // rno 값 검증 - 더 엄격하게
      if(!rno) {
        alert("댓글 정보를 찾을 수 없습니다. 댓글을 다시 선택해주세요.");
        console.error("rno 값이 null 또는 undefined입니다.");
        return;
      }

      // 문자열로 변환 후 검증
      const rnoStr = String(rno).trim();
      if(rnoStr === '' || rnoStr === '33' || rnoStr === 'undefined' || rnoStr === 'null' || rnoStr === 'NaN') {
        alert("댓글 정보를 찾을 수 없습니다. 댓글을 다시 선택해주세요.");
        console.error("rno 값이 유효하지 않습니다:", rnoStr);
        return;
      }

      // 숫자로 변환하여 검증
      const rnoNum = parseInt(rnoStr, 10);
      console.log("rnoStr:", rnoStr, "-> rnoNum:", rnoNum);
      
      if(isNaN(rnoNum) || rnoNum <= 0) {
        alert("유효하지 않은 댓글 번호입니다.");
        console.error("rno가 숫자가 아니거나 0 이하입니다. 원본:", rnoStr, "변환:", rnoNum);
        return;
      }

      // 삭제 확인 다이얼로그
      if(!confirm("정말로 이 댓글을 삭제하시겠습니까?")) {
        return;
      }

      // rnoNum이 제대로 설정되었는지 다시 확인하고, rnoStr도 함께 확인
      if(!rnoNum || isNaN(rnoNum) || !rnoStr || rnoStr === '') {
        alert("댓글 번호를 가져올 수 없습니다.");
        console.error("rnoNum이 유효하지 않습니다. rnoNum:", rnoNum, "rnoStr:", rnoStr);
        return;
      }

      // 최종적으로 사용할 rno 값 결정 - rnoStr을 직접 사용 (이미 검증됨)
      // rnoNum 대신 rnoStr을 사용하여 변환 오류 방지
      const finalRno = rnoStr;
      console.log("=== 삭제 요청 정보 ===");
      console.log("원본 rno:", rno);
      console.log("rnoStr (문자열, 검증됨):", rnoStr);
      console.log("rnoNum (숫자, 검증용):", rnoNum);
      console.log("finalRno (최종 사용값, rnoStr 사용):", finalRno);
      console.log("finalRno 타입:", typeof finalRno);
      
      // URL 생성 - finalRno(rnoStr)를 직접 사용
      const deleteUrl = '/replies/' + finalRno;
      console.log("생성된 요청 URL:", deleteUrl);
      console.log("요청 메서드: DELETE");
      
      // URL이 올바르게 생성되었는지 최종 확인
      if(!deleteUrl || deleteUrl === '/replies/' || deleteUrl === '/replies/undefined' || deleteUrl === '/replies/null' || deleteUrl.includes('NaN')) {
        alert("댓글 번호가 올바르지 않습니다. 다시 시도해주세요.");
        console.error("URL 생성 실패! deleteUrl:", deleteUrl, "finalRno:", finalRno);
        return;
      }

      axios.delete(deleteUrl).then( res => {
        const data = res.data;

        console.log("삭제 성공:", data);

        // 성공 메시지
        alert("댓글이 삭제되었습니다.");

        // 전역 변수 초기화
        currentRno = null;

        replyModal.hide();

        getReplies(currentPage);
      }).catch(err => {
        console.error("=== 삭제 실패 상세 정보 ===");
        console.error("에러 객체:", err);
        console.error("에러 응답:", err.response);
        console.error("요청 URL:", err.config?.url);
        console.error("요청 메서드:", err.config?.method);
        console.error("요청 데이터:", err.config?.data);
        
        // 에러 메시지 표시
        let errorMsg = "댓글 삭제에 실패했습니다.";
        if(err.response) {
          if(err.response.data) {
            errorMsg = err.response.data;
          } else if(err.response.statusText) {
            errorMsg = err.response.statusText;
          }
        }
        alert("오류: " + errorMsg);
      });
    }, false);

    //수정
    document.querySelector(".btnReplyMod").addEventListener("click", function(e){
      e.preventDefault();
      e.stopPropagation();

      console.log("=== 수정 버튼 클릭 ===");
      console.log("전역 currentRno:", currentRno);

      // rno 값 가져오기 (삭제 기능과 동일한 방식)
      let rno = currentRno;
      
      // currentRno가 없으면 다른 방법으로 시도
      if(!rno) {
        // 모달 폼에서 가져오기
        const modal = document.querySelector('#replyModal');
        const form = modal ? modal.querySelector('#replyModForm') : null;
        if(form) {
          const rnoInput = form.querySelector("input[name='rno']");
          rno = rnoInput ? rnoInput.value.trim() : null;
          console.log("모달 폼에서 가져온 rno:", rno);
        }
        
        // 여전히 없으면 삭제 버튼의 data-rno 속성에서 가져오기
        if(!rno) {
          const deleteBtn = document.querySelector(".btnReplyDel");
          if(deleteBtn) {
            rno = deleteBtn.getAttribute("data-rno");
            console.log("삭제 버튼의 data-rno:", rno);
          }
        }
      }

      console.log("수정할 rno 값:", rno);
      console.log("rno 타입:", typeof rno);

      // rno 값 검증
      if(!rno) {
        alert("댓글 정보를 찾을 수 없습니다. 댓글을 다시 선택해주세요.");
        console.error("rno 값이 null 또는 undefined입니다.");
        return;
      }

      // 문자열로 변환 후 검증
      const rnoStr = String(rno).trim();
      if(rnoStr === '' || rnoStr === '33' || rnoStr === 'undefined' || rnoStr === 'null' || rnoStr === 'NaN') {
        alert("댓글 정보를 찾을 수 없습니다. 댓글을 다시 선택해주세요.");
        console.error("rno 값이 유효하지 않습니다:", rnoStr);
        return;
      }

      // 숫자로 변환하여 검증
      const rnoNum = parseInt(rnoStr, 10);
      if(isNaN(rnoNum) || rnoNum <= 0) {
        alert("유효하지 않은 댓글 번호입니다.");
        console.error("rno가 숫자가 아니거나 0 이하입니다. 원본:", rnoStr, "변환:", rnoNum);
        return;
      }

      // 모달 폼에서 replyText 가져오기
      const modal = document.querySelector('#replyModal');
      const form = modal ? modal.querySelector('#replyModForm') : null;
      if(!form) {
        alert("댓글 폼을 찾을 수 없습니다.");
        console.error("모달 폼을 찾을 수 없습니다.");
        return;
      }

      const replyTextInput = form.querySelector("input[name='replyText']");
      const replyText = replyTextInput ? replyTextInput.value.trim() : null;

      console.log("수정할 댓글 내용:", replyText);

      // 댓글 내용 검증
      if(!replyText || replyText === '') {
        alert("댓글 내용을 입력해주세요.");
        return;
      }

      // 수정 확인 다이얼로그
      if(!confirm("댓글을 수정하시겠습니까?")) {
        return;
      }

      // JSON 형식으로 데이터 준비
      const updateData = {
        replyText: replyText
      };

      const updateUrl = '/replies/' + rnoStr;
      console.log("=== 수정 요청 정보 ===");
      console.log("요청 URL:", updateUrl);
      console.log("요청 메서드: PUT");
      console.log("요청 데이터:", updateData);

      axios.put(updateUrl, JSON.stringify(updateData), {
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(res => {
        const data = res.data;

        console.log("수정 성공:", data);

        // 성공 메시지
        alert("댓글이 수정되었습니다.");

        // 전역 변수 초기화
        currentRno = null;

        replyModal.hide();

        getReplies(currentPage);
      }).catch(err => {
        console.error("=== 수정 실패 상세 정보 ===");
        console.error("에러 객체:", err);
        console.error("에러 응답:", err.response);
        console.error("요청 URL:", err.config?.url);
        console.error("요청 메서드:", err.config?.method);
        console.error("요청 데이터:", err.config?.data);
        
        // 에러 메시지 표시
        let errorMsg = "댓글 수정에 실패했습니다.";
        if(err.response) {
          if(err.response.data) {
            errorMsg = err.response.data;
          } else if(err.response.statusText) {
            errorMsg = err.response.statusText;
          }
        }
        alert("오류: " + errorMsg);
      });
    }, false);
</script>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
