<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<!-- ============================================ -->
<!-- 게시판 목록 영역 시작 -->
<!-- ============================================ -->
<!-- Bootstrap의 row 클래스: 가로 공간을 12칸으로 나눔, justify-content-center는 중앙 정렬 -->
<div class="row justify-content-center">
	<!-- col-lg-12: 큰 화면에서 12칸(전체 너비) 사용 -->
	<div class="col-lg-12">
		<!-- card: Bootstrap 카드 컴포넌트, shadow는 그림자 효과, mb-4는 하단 마진 -->
		<div class="card shadow mb-4">
			<!-- 카드 헤더: 게시판 제목 영역 -->
			<div class="card-header py-3">
				<h6 class="m-0 font-weight-bold text-primary">Board List</h6>
			</div>
			
			<!-- ============================================ -->
			<!-- 게시글 목록 테이블 영역 -->
			<!-- ============================================ -->
			<div class="card-body">
				<!-- 테이블: 게시글 목록을 표시하는 테이블 -->
				<!-- table-bordered: 테이블 테두리 표시 -->
				<table class="table table-bordered" id="dataTable">
					<!-- 테이블 헤더: 컬럼 이름들 -->
					<thead>
						<tr>
							<th>Bno</th>        <!-- 게시글 번호 -->
							<th>Title</th>      <!-- 제목 -->
							<th>Writer</th>     <!-- 작성자 -->
							<th>RegDate</th>    <!-- 등록일 -->
						</tr>
					</thead>
					<!-- 테이블 바디: 실제 게시글 데이터가 들어갈 영역 -->
					<tbody class="tbody">
						<!-- JSTL의 forEach: dto.boardList에 있는 각 게시글을 반복 처리 -->
						<!-- var="board": 반복문에서 사용할 변수명 -->
						<!-- items="${dto.boardList}": Controller에서 전달받은 게시글 리스트 -->
						<c:forEach var="board" items="${dto.boardList}">
							<!-- 각 게시글을 한 행(tr)으로 표시 -->
							<!-- data-bno: HTML5 데이터 속성, 게시글 번호를 저장 (나중에 JavaScript에서 사용 가능) -->
							<tr data-bno="${board.bno}">
								<!-- 게시글 번호 표시 -->
								<!-- c:out: XSS 공격 방지를 위해 HTML 특수문자를 이스케이프 처리 -->
								<td><c:out value="${board.bno}" /></td>
								
								<!-- 제목: 클릭하면 상세보기 페이지로 이동하는 링크 -->
								<td>
									<!-- href: 링크 주소 -->
									<!-- ${pageContext.request.contextPath}: 프로젝트 경로 (예: /sp1) -->
									<!-- /board/read/${board.bno}: 게시글 번호를 URL에 포함 -->
									<a href="${pageContext.request.contextPath}/board/read/${board.bno}">
										<c:out value="${board.title}" />
									</a>
								</td>
								
								<!-- 작성자 표시 -->
								<td><c:out value="${board.writer}" /></td>
								
								<!-- 등록일 표시 -->
								<td><c:out value="${board.createdDate}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<!--검색 영역-->
			<div class="card-body">
				<div class="d-flex justify-content-end" style="margin-bottom: 2em;">
					<div style="width: 50%;" class="d-flex">
						<select id="typeSelect" class="form-select form-control me-2">
							<option value="">--</option>
							<option value="T" <c:if test="${dto.types == 'T'}">selected</c:if>>제목</option>
							<option value="C" <c:if test="${dto.types == 'C'}">selected</c:if>>내용</option>
							<option value="W" <c:if test="${dto.types == 'W'}">selected</c:if>>작성자</option>
							<option value="TC" <c:if test="${dto.types == 'TC'}">selected</c:if>>제목+내용</option>
							<option value="TW" <c:if test="${dto.types == 'TW'}">selected</c:if>>제목+작성자</option>
							<option value="TCW" <c:if test="${dto.types == 'TCW'}">selected</c:if>>제목+내용+작성자</option>
						</select>
						<input type="text" id="keywordInput" class="form-control me-2" placeholder="Search" value="<c:out value="${dto.keyword}" />">
						<button class="btn btn-outline-info searchBtn" type="button">Search</button>
					</div>
				</div>
			</div>
			
			<!-- ============================================ -->
			<!-- 페이지네이션 영역 -->
			<!-- ============================================ -->
			<!-- card-footer: 카드 하단 영역에 페이지네이션 배치 -->
			<div class="card-footer">
				<!-- Bootstrap pagination 컴포넌트 -->
				<!-- justify-content-center: 페이지 번호들을 중앙 정렬 -->
				<!-- mb-0: 하단 마진 제거 -->
				<ul class="pagination justify-content-center mb-0">
					
					<!-- ============================================ -->
					<!-- 이전 버튼 -->
					<!-- ============================================ -->
					<!-- page-item: Bootstrap 페이지네이션 아이템 -->
					<!-- c:if: 조건문, dto.prev가 false면 'disabled' 클래스 추가 (비활성화) -->
					<li class="page-item <c:if test='${!dto.prev}'>disabled</c:if>">
					<!-- c:choose: 다중 조건문 (if-else if-else와 유사) -->
					<!-- dto.prev가 true면 링크, false면 span 태그 사용 -->
					<c:choose>
						<c:when test="${dto.prev}">
							<a class="page-link" 
							   href="${pageContext.request.contextPath}/board/list?page=${dto.start - 1}&size=${dto.size}<c:if test='${dto.types != null and dto.types != ""}'>&type=<c:out value='${dto.types}' /></c:if><c:if test='${dto.keyword != null and dto.keyword != ""}'>&keyword=<c:out value='${dto.keyword}' /></c:if>">
								이전
							</a>
						</c:when>
						<c:otherwise>
							<span class="page-link">이전</span>
						</c:otherwise>
					</c:choose>
					</li>
					
					<!-- ============================================ -->
					<!-- 페이지 번호 버튼들 -->
					<!-- ============================================ -->
					<!-- dto.pageNums: 화면에 보여줄 페이지 번호들의 리스트 (예: [1,2,3,4,5,6,7,8,9,10]) -->
					<!-- 각 페이지 번호를 반복하여 버튼 생성 -->
					<c:forEach var="num" items="${dto.pageNums}">
						<!-- page-item: 각 페이지 번호 버튼 -->
						<!-- c:if: 현재 페이지(dto.page)와 반복 중인 번호(num)가 같으면 'active' 클래스 추가 (강조 표시) -->
						<!-- 현재 페이지와 같으면 active 클래스 추가하여 강조 표시 -->
						<li class="page-item <c:if test='${dto.page == num}'>active</c:if>">
							<a class="page-link" 
							   href="${pageContext.request.contextPath}/board/list?page=${num}&size=${dto.size}<c:if test='${dto.types != null and dto.types != ""}'>&type=<c:out value='${dto.types}' /></c:if><c:if test='${dto.keyword != null and dto.keyword != ""}'>&keyword=<c:out value='${dto.keyword}' /></c:if>">
								<c:out value="${num}" />
							</a>
						</li>
					</c:forEach>
					
					<!-- ============================================ -->
					<!-- 다음 버튼 -->
					<!-- ============================================ -->
					<!-- dto.next가 true면 링크, false면 span 태그 사용 -->
					<li class="page-item <c:if test='${!dto.next}'>disabled</c:if>">
						<c:choose>
							<c:when test="${dto.next}">
								<a class="page-link" 
								   href="${pageContext.request.contextPath}/board/list?page=${dto.end + 1}&size=${dto.size}<c:if test='${dto.types != null and dto.types != ""}'>&type=<c:out value='${dto.types}' /></c:if><c:if test='${dto.keyword != null and dto.keyword != ""}'>&keyword=<c:out value='${dto.keyword}' /></c:if>">
									다음
								</a>
							</c:when>
							<c:otherwise>
								<span class="page-link">다음</span>
							</c:otherwise>
						</c:choose>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>
<!-- ============================================ -->
<!-- 게시판 목록 영역 끝 -->
<!-- ============================================ -->

<!-- 등록 완료 모달 -->
<div class="modal" tabindex="-1" id="myModal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">알림</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body">
				<p><span id="modalResult"></span>번 글이 등록되었습니다.</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	console.log('스크립트 시작');
	
	// 등록 완료 모달 처리
	const result = '${result}';
	
	if(result) {
		const myModal = new bootstrap.Modal(document.getElementById('myModal'));
		document.getElementById('modalResult').innerText = result;
		myModal.show();
	}
	
	// 검색 기능 초기화 함수
	function initSearch() {
		console.log('검색 기능 초기화 시작');
		
		const searchBtn = document.querySelector('.searchBtn');
		const typeSelect = document.getElementById('typeSelect');
		const keywordInput = document.getElementById('keywordInput');
		
		console.log('검색 요소 확인:', {
			searchBtn: searchBtn,
			typeSelect: typeSelect,
			keywordInput: keywordInput
		});
		
		if(!searchBtn) {
			console.error('검색 버튼을 찾을 수 없습니다!');
			return;
		}
		
		if(!typeSelect) {
			console.error('검색 타입 선택을 찾을 수 없습니다!');
			return;
		}
		
		if(!keywordInput) {
			console.error('검색 입력 필드를 찾을 수 없습니다!');
			return;
		}
		
		// 검색 함수
		function performSearch() {
			try {
				const type = typeSelect.value || '';
				const keyword = keywordInput.value.trim() || '';
				const size = ${dto.size != null ? dto.size : 10};
				const contextPath = '${pageContext.request.contextPath}';
				
				console.log('검색 실행:', {type: type, keyword: keyword, size: size, contextPath: contextPath});
				
				// URL 파라미터 생성
				const params = ['page=1', 'size=' + size];
				
				if(keyword) {
					if(type) {
						params.push('type=' + encodeURIComponent(type));
					}
					params.push('keyword=' + encodeURIComponent(keyword));
				}
				
				const url = contextPath + '/board/list?' + params.join('&');
				console.log('생성된 URL:', url);
				
				window.location.href = url;
			} catch(error) {
				console.error('검색 오류:', error);
				alert('검색 중 오류: ' + error.message);
			}
		}
		
		// 검색 버튼 클릭 이벤트
		searchBtn.addEventListener('click', function(e) {
			e.preventDefault();
			e.stopPropagation();
			console.log('검색 버튼 클릭됨!');
			performSearch();
		});
		
		// Enter 키 이벤트
		keywordInput.addEventListener('keypress', function(e) {
			if(e.key === 'Enter' || e.keyCode === 13) {
				e.preventDefault();
				e.stopPropagation();
				console.log('Enter 키 입력됨!');
				performSearch();
			}
		});
		
		console.log('검색 기능 초기화 완료');
	}
	
	// 페이지네이션 스크롤 처리
	function initPagination() {
		document.querySelectorAll('.pagination .page-link[href]').forEach(function(link) {
			link.addEventListener('click', function() {
				window.scrollTo({ top: 0, behavior: 'smooth' });
			});
		});
	}
	
	// DOM 로드 완료 시 실행
	if(document.readyState === 'loading') {
		console.log('문서 로딩 중, DOMContentLoaded 대기');
		document.addEventListener('DOMContentLoaded', function() {
			console.log('DOMContentLoaded 실행됨');
			initSearch();
			initPagination();
		});
	} else {
		console.log('문서 이미 로드됨, 즉시 실행');
		initSearch();
		initPagination();
	}
	
	console.log('스크립트 로드 완료');
</script>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
