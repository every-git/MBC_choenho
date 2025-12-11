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
							   href="${pageContext.request.contextPath}/board/list?page=${dto.start - 1}&size=${dto.size}">
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
							   href="${pageContext.request.contextPath}/board/list?page=${num}&size=${dto.size}">
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
								   href="${pageContext.request.contextPath}/board/list?page=${dto.end + 1}&size=${dto.size}">
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

<script type="text/javascript" defer="defer">
	// 등록 완료 모달 처리
	const result = '${result}';
	
	if(result) {
		const myModal = new bootstrap.Modal(document.getElementById('myModal'));
		document.getElementById('modalResult').innerText = result;
		myModal.show();
	}
	
	// 페이지네이션 이벤트 처리
	document.addEventListener('DOMContentLoaded', function() {
		// 페이지 이동 시 상단으로 부드럽게 스크롤
		document.querySelectorAll('.pagination .page-link[href]').forEach(function(link) {
			link.addEventListener('click', function() {
				window.scrollTo({ top: 0, behavior: 'smooth' });
			});
		});
	});
</script>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
