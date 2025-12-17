<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file= "/WEB-INF/views/includes/header.jsp" %>

<div class="row justify-content-center">
	<div class="col-lg-12">
		<div class="card shadow mb-4">
			<div class="card-header py-3 d-flex justify-content-betweenalign-items-center">
				<h6 class="m-0 font-weight-bold text-primary"></h6>
				<a href="/board/register" class="btn btn-primary btn-sm">
						새 글 등록
				</a>
			</div>
			
			<div class="card-body">
				<table class="table table-boardered" id="dataTable">
					<thead>
						<th>Bno</th>
						<th>Title</th>
						<th>Writer</th>
						<th>RegDate</th>
					</thead>
					<tbody class="tbody">
						<c:forEach var="board" items="${dto.boardDTOList}">
							<tr data-bno=${board.bno}>
								<td><c:out value="${board.bno}" /></td>
								<td>
									<c:url var="readUrl" value="/board/read/${board.bno}">
									  <c:param name="page" value="${dto.page}" />
									  <c:param name="size" value="${dto.size}" />
									  <c:param name="types" value="${dto.types}" />
									  <c:param name="keyword" value="${dto.keyword}" />
									</c:url>
									<a href="${readUrl}">
										<c:out value="${board.title}" />
									</a>
								</td>
								<td><c:out value="${board.writer}" /></td>
								<td><c:out value="${board.createdDate}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<!-- 검색 조건  -->
				<div class="card-body">
					<div class="d-flex justify-content-end" style="margin-bottom: 2em">
						<div style="width:50%;" class="d-flex">
							<select name="typeSelect" class="form-select me-2">
								<option value="">--</option>
								<option value="T" ${dto.types == 'T' ? 'selected' : '' }>제목</option>
								<option value="C" ${dto.types == 'C' ? 'selected' : '' }>내용</option>
								<option value="W" ${dto.types == 'W' ? 'selected' : '' }>작성자</option>
								<option value="TC" ${dto.types == 'TC' ? 'selected' : '' }>제목 OR 내용</option>
								<option value="TW" ${dto.types == 'TW' ? 'selected' : '' }>제목 OR 작성자</option>
								<option value="TCW" ${dto.types == 'TCW' ? 'selected' : '' }>제목 OR 내용 OR 작성자</option>
							</select>
						</div>
						<input type="text" class="form-control me-2" 
								name="keywordInput" value="<c:out value='${dto.keyword}'/>" />
						<button class="btn btn-outline-info searchBtn">Search</button>		
					</div>
				</div>
				<!-- end 검색 조건 -->
				
				<!-- 페이징 처리  -->
				<div class="d-flex justify-content-center">
				  <ul class="pagination ">
				  	
				  	<c:if test="${dto.prev}">
					    <li class="page-item">
					      <a class="page-link" href="${dto.start - 1 }" tabindex="-1">Prev</a>
					    </li>
				    </c:if>
				    
				    <c:forEach var="num" items="${dto.pageNums}">
				    	<li class="page-item ${dto.page == num ? 'active' : ''}">
				    		<a class="page-link" href="${num}">${num}</a>
				    	</li>
				    </c:forEach>
				    
				    <c:if test="${dto.next}">
					    <li class="page-item">
					      <a class="page-link" href="${dto.end + 1}">Next</a>
					    </li>
				    </c:if>
				    
				  </ul>
				</div>
				<!-- end 페이징 처리  -->
			</div>	
		</div>
	</div>
</div>


<div class="modal" tabindex="-1" id="myModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Modal title</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <p><span id="modalResult"></span>번 글이 등록되었습니다.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary">Save changes</button>
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

<!-- 
   defer 속성 설명
     - HTML 문서를 먼저 모두 파싱한 뒤에 app.js를 실행하도록 지연(load defer)시키는 옵션
     - 스크립트 다운로드는 병렬로 진행되지만, 실행은 DOM 생성이 끝난 후 처리됨
     - DOM 요소를 찾는 코드(document.querySelector 등)가 안정적으로 실행됨
     - 화면 렌더링을 차단(block)하지 않아 성능이 좋아짐
     - 여러 개의 defer 스크립트는 HTML에 작성된 순서대로 실행됨
-->
<script type="text/javascript" defer="defer">
	const result = '${result}'
	
	console.log("result :"  + result);
	
	const myModal = new bootstrap.Modal(document.getElementById('myModal'));
	
	if(result){
		
		document.getElementById('modalResult').innerText = result;
		
		myModal.show();
	}
	
	// 페이징 이벤트 처리
	const pagingDiv = document.querySelector(".pagination");
	if(pagingDiv) {
		pagingDiv.addEventListener("click", (e) => {
		
		//a 태크 기본 동작(페이지 이동) 막기
		e.preventDefault();
		
		// 이벤트 버블링 방지(부모 요소로 이벤트 전파 차단)
		e.stopPropagation();
		
		const target = e.target;		
		
		const targetPage =  target.getAttribute("href");
		const size = ${dto.size} || 10 ;
		
		const params = new URLSearchParams({
			page: targetPage,
			size: size
		});
		
		const types = '${dto.types}';
		const keyword = '${dto.keyword}';
		
		if(types && types !== '' && keyword && keyword !== ''){			
			params.set("types", types);
			params.set("keyword", keyword);
		}
		
		//?page=5&size=10		
		// 컨텍스트 경로 가져오기
		let contextPath = '${pageContext.request.contextPath}';
		// JSP EL이 작동하지 않거나 빈 문자열인 경우, 현재 경로에서 /board/list를 제거하여 추출
		if (!contextPath || contextPath === '' || contextPath === '${pageContext.request.contextPath}') {
			const currentPath = window.location.pathname;
			// /sp1/board/list 또는 /board/list 형태에서 /board/list를 제거
			if (currentPath.includes('/board/list')) {
				contextPath = currentPath.replace('/board/list', '').replace(/\/$/, '');
			} else {
				// /board/list가 없는 경우 첫 번째 경로 세그먼트 추출
				const match = currentPath.match(/^(\/[^\/]+)/);
				contextPath = match ? match[1] : '';
			}
		}
		window.location.href = contextPath + '/board/list?' + params.toString();
		
		}, false);
	}
	
	//검색 조건
	document.addEventListener("DOMContentLoaded", function() {
		const searchBtn = document.querySelector(".searchBtn");
		console.log("검색 버튼 찾기:", searchBtn);
		
		if(searchBtn) {
			searchBtn.addEventListener("click", function(e) {
				e.preventDefault();
				console.log("검색 버튼 클릭됨!");
				
				const keywordInput = document.querySelector("input[name='keywordInput']");
				const typeSelect = document.querySelector("select[name='typeSelect']");
				
				console.log("keywordInput:", keywordInput);
				console.log("typeSelect:", typeSelect);
				
				if(!keywordInput || !typeSelect) {
					console.error("검색 입력 요소를 찾을 수 없습니다.");
					alert("검색 입력 요소를 찾을 수 없습니다.");
					return;
				}
				
				const keyword = keywordInput.value.trim();
				const types = typeSelect.value;
				
				console.log("입력된 값 - types:", types, "keyword:", keyword);
				
				const params = new URLSearchParams({
					page: '1',
					size: '10'
				});
				
				// types와 keyword가 모두 있을 때만 파라미터에 추가
				if(types && types !== '' && keyword && keyword !== '') {
					params.set("types", types);
					params.set("keyword", keyword);
				}
				
			// 컨텍스트 경로 가져오기
			let contextPath = '${pageContext.request.contextPath}';
			// JSP EL이 작동하지 않거나 빈 문자열인 경우, 현재 경로에서 /board/list를 제거하여 추출
			if (!contextPath || contextPath === '' || contextPath === '${pageContext.request.contextPath}') {
				const currentPath = window.location.pathname;
				// /sp1/board/list 또는 /board/list 형태에서 /board/list를 제거
				if (currentPath.includes('/board/list')) {
					contextPath = currentPath.replace('/board/list', '').replace(/\/$/, '');
				} else {
					// /board/list가 없는 경우 첫 번째 경로 세그먼트 추출
					const match = currentPath.match(/^(\/[^\/]+)/);
					contextPath = match ? match[1] : '';
				}
			}
			const url = contextPath + '/board/list?' + params.toString();
			console.log("현재 경로:", window.location.pathname);
			console.log("추출된 contextPath:", contextPath);
			console.log("최종 검색 URL:", url);
			
			window.location.href = url;
			});
		} else {
			console.error("검색 버튼을 찾을 수 없습니다!");
		}
	});
</script>



<%@ include file= "/WEB-INF/views/includes/footer.jsp" %>