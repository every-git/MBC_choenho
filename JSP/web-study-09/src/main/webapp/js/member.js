/**
 * 로그인 폼 유효성 검증 함수
 * 
 * 역할: 로그인 버튼 클릭 시 아이디와 비밀번호가 입력되었는지 확인
 * 
 * @returns {boolean} 검증 통과 시 true, 실패 시 false
 * 
 * 사용 예시:
 * - login.jsp의 로그인 버튼: onclick="return loginCheck()"
 * - true 반환 시: 폼 제출 진행
 * - false 반환 시: 폼 제출 중단
 */
function loginCheck() {
    // 아이디 입력 체크
    if(document.frm.userid.value == "") {
        alert("아이디를 입력해주세요.");
        document.frm.userid.focus();  // 아이디 입력 필드로 포커스 이동
        return false;  // 폼 제출 중단
    }
    
    // 비밀번호 입력 체크
    if(document.frm.pwd.value == "") {
        alert("비밀번호를 입력해주세요.");
        document.frm.pwd.focus();  // 비밀번호 입력 필드로 포커스 이동
        return false;  // 폼 제출 중단
    }
    
    return true;  // 모든 검증 통과 시 폼 제출 허용
}

/**
 * 아이디 중복 체크 함수
 * 
 * 역할: 회원가입 페이지에서 아이디 중복 체크 팝업을 열기
 * 
 * 처리 과정:
 * 1. 폼과 아이디 입력 필드 존재 확인
 * 2. 아이디 입력 여부 확인
 * 3. contextPath 가져오기 (절대 경로 생성용)
 * 4. 중복 체크 URL 생성
 * 5. 팝업 창 열기
 * 
 * 사용 예시:
 * - join.jsp의 중복 체크 버튼: onclick="idCheck()"
 */
function idCheck() {
    // 폼 존재 확인
    if(!document.frm) {
        alert("폼을 찾을 수 없습니다.");
        return;  // 함수 종료
    }
    
    // 아이디 입력 필드 존재 확인
    if(!document.frm.userid) {
        alert("아이디 입력 필드를 찾을 수 없습니다.");
        return;  // 함수 종료
    }
    
    // 입력된 아이디 값 가져오기
    var useridValue = document.frm.userid.value;
    
    // 아이디 입력 여부 확인
    if(useridValue == "" || useridValue.trim() == "") {
        alert("아이디를 입력해주세요.");
        document.frm.userid.focus();  // 아이디 입력 필드로 포커스 이동
        return;  // 함수 종료
    }
    
    /**
     * contextPath 가져오기
     * - join.jsp에서 설정한 window.contextPath 사용
     * - 없으면 자동으로 추출 (URL 경로에서 첫 번째 경로 추출)
     * - 예: /web-study-09/join.do → /web-study-09
     */
    var ctxPath = window.contextPath;
    if(!ctxPath && typeof contextPath !== 'undefined') {
        ctxPath = contextPath;
    }
    if(!ctxPath) {
        var pathname = window.location.pathname;
        var match = pathname.match(/^\/([^\/]+)/);  // 정규식으로 첫 번째 경로 추출
        if(match && match[1]) {
            ctxPath = '/' + match[1];
        } else {
            ctxPath = '/web-study-09';  // 기본값
        }
    }
    
    /**
     * 중복 체크 URL 생성
     * - 절대 URL로 생성하여 팝업에서도 정상 작동하도록 함
     * - encodeURIComponent: URL에 포함될 수 없는 문자를 인코딩
     */
    var baseUrl = window.location.protocol + "//" + window.location.host;
    var url = baseUrl + ctxPath + "/idCheck.do?userid=" + encodeURIComponent(useridValue);
    
    /**
     * 팝업 창 열기
     * - window.open(): 새 창 열기
     * - 첫 번째 인자: 열 URL
     * - 두 번째 인자: 창 이름 ("idCheckPopup"으로 고정하여 같은 팝업 재사용)
     * - 세 번째 인자: 창 옵션 (크기, 메뉴바 등)
     */
    try {
        var popup = window.open(url, "idCheckPopup", "toolbar=no, menubar=no, scrollbars=yes, resizable=no, width=450, height=200");
        
        /**
         * 팝업 차단 확인
         * - 브라우저가 팝업을 차단했는지 확인
         * - 약간의 지연 후 확인 (팝업이 열리는 시간 고려)
         */
        setTimeout(function() {
            if(!popup || popup.closed || typeof popup.closed == 'undefined') {
                alert("팝업이 차단되었습니다.\n브라우저 주소창 옆의 팝업 차단 아이콘을 클릭하여 팝업을 허용해주세요.");
            }
        }, 100);
        
    } catch(e) {
        // 팝업 열기 실패 시 에러 메시지 표시
        alert("팝업을 열 수 없습니다: " + e.message);
    }
}

/**
 * 아이디 사용 확인 함수
 * 
 * 역할: 중복 체크 팝업에서 "사용" 버튼 클릭 시 부모 창의 폼에 아이디 설정
 * 
 * @param {string} userid 사용할 아이디
 * 
 * 처리 과정:
 * 1. 부모 창(opener) 존재 확인
 * 2. 부모 창의 폼에 아이디 설정
 * 3. reid 필드에도 아이디 설정 (중복 체크 확인용)
 * 4. 아이디 필드를 읽기 전용으로 변경
 * 5. 팝업 창 닫기
 * 
 * 사용 예시:
 * - idCheck.jsp의 사용 버튼: onclick="idok('${userid}')"
 */
function idok(userid) {
    /**
     * 부모 창 존재 확인 및 폼 업데이트
     * - opener: 팝업을 연 부모 창을 가리킴
     * - opener.document.frm: 부모 창의 회원가입 폼
     */
    if(window.opener && window.opener.document && window.opener.document.frm) {
        // 부모 창의 아이디 필드에 값 설정
        window.opener.document.frm.userid.value = userid;
        
        // 중복 체크 확인용 hidden 필드에도 값 설정
        // JoinServlet에서 이 값이 userid와 일치하는지 확인
        window.opener.document.frm.reid.value = userid;
        
        // 아이디 필드를 읽기 전용으로 변경 (수정 방지)
        window.opener.document.frm.userid.readOnly = true;
    } else {
        // 부모 창을 찾을 수 없는 경우 에러 메시지 표시
        alert("오류: 부모 창을 찾을 수 없습니다. 팝업을 닫고 다시 시도해주세요.");
    }
    
    // 팝업 창 닫기
    window.close();
}

/**
 * 회원가입 폼 유효성 검증 함수
 * 
 * 역할: 회원가입 폼 제출 전에 모든 필수 항목이 올바르게 입력되었는지 확인
 * 
 * @returns {boolean} 검증 통과 시 true, 실패 시 false
 * 
 * 검증 항목:
 * 1. 이름 입력 여부 및 길이 (최대 10자)
 * 2. 아이디 입력 여부 및 길이 (최소 4자)
 * 3. 비밀번호 입력 여부
 * 4. 비밀번호와 비밀번호 확인 일치 여부
 * 5. 아이디 중복 체크 여부 (reid 필드 확인)
 * 
 * 사용 예시:
 * - join.jsp의 확인 버튼: onclick="return joinCheck()"
 * - memberUpdate.jsp의 확인 버튼: onclick="return joinCheck()"
 */
function joinCheck() {
   // 이름 입력 체크
   if (document.frm.name.value.length == 0) {
      alert("이름을 써주세요.");
      frm.name.focus();  // 이름 입력 필드로 포커스 이동
      return false;  // 폼 제출 중단
   }
   
   // 이름 길이 체크 (최대 10자)
   if (document.frm.name.value.length > 10) {
      alert("이름은 10자 이하여야 합니다.");
      frm.name.focus();
      return false;  // 폼 제출 중단
   }
   
   // 아이디 입력 체크
   if (document.frm.userid.value.length == 0) {
      alert("아이디를 써주세요");
      frm.userid.focus();
      return false;  // 폼 제출 중단
   }
   
   // 아이디 길이 체크 (최소 4자)
   if (document.frm.userid.value.length < 4) {
      alert("아이디는 4글자이상이어야 합니다.");
      frm.userid.focus();
      return false;  // 폼 제출 중단
   }
   
   // 비밀번호 입력 체크
   if (document.frm.pwd.value == "") {
      alert("암호는 반드시 입력해야 합니다.");
      frm.pwd.focus();
      return false;  // 폼 제출 중단
   }
   
   // 비밀번호와 비밀번호 확인 일치 여부 체크
   if (document.frm.pwd.value != document.frm.pwd_check.value) {
      alert("암호가 일치하지 않습니다.");
      frm.pwd.focus();
      return false;  // 폼 제출 중단
   }
   
   // 아이디 중복 체크 여부 확인
   // reid 필드는 중복 체크 팝업에서 "사용" 버튼을 클릭했을 때 설정됨
   if (document.frm.reid.value.length == 0) {
      alert("중복 체크를 하지 않았습니다.");
      frm.userid.focus();
      return false;  // 폼 제출 중단
   }
   
   return true;  // 모든 검증 통과 시 폼 제출 허용
}
