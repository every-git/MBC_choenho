# JSP 로그인 시스템 흐름도 - Part 1

> 전체 시스템의 구조와 로그인/회원가입 프로세스 상세 도식화

---

## 📋 목차

1. [전체 시스템 아키텍처](#1-전체-시스템-아키텍처)
2. [로그인 프로세스 상세 흐름도](#2-로그인-프로세스-상세-흐름도)
3. [회원가입 프로세스 상세 흐름도](#3-회원가입-프로세스-상세-흐름도)
4. [아이디 중복 체크 프로세스](#4-아이디-중복-체크-프로세스)

---

## 1. 전체 시스템 아키텍처

### 1.1 MVC 패턴 구조

```mermaid
graph TB
    subgraph "브라우저 (Client)"
        A[사용자]
    end
    
    subgraph "View Layer - JSP"
        B1[login.jsp]
        B2[join.jsp]
        B3[main.jsp]
        B4[memberUpdate.jsp]
        B5[idCheck.jsp]
    end
    
    subgraph "Controller Layer - Servlet"
        C1[LoginServlet]
        C2[JoinServlet]
        C3[UpdateServlet]
        C4[LogoutServlet]
        C5[IdCheckServlet]
    end
    
    subgraph "Model Layer - DAO/VO"
        D1[MemberDAO]
        D2[MemberVO]
    end
    
    subgraph "Data Layer"
        E[("Oracle Database (member 테이블)")]
    end
    
    subgraph "Session Management"
        F["HttpSession<br/>- userid<br/>- loginUser"]
    end
    
    A -->|HTTP Request| B1
    A -->|HTTP Request| B2
    B1 -->|submit| C1
    B2 -->|submit| C2
    B3 -->|logout| C4
    B4 -->|update| C3
    B5 -->|idCheck| C5
    
    C1 --> D1
    C2 --> D1
    C3 --> D1
    C5 --> D1
    
    D1 --> E
    D1 -.MemberVO 객체.-> D2
    
    C1 -.세션 저장.-> F
    C3 -.세션 업데이트.-> F
    C4 -.세션 삭제.-> F
    
    C1 -->|forward| B3
    C2 -->|forward| B1
    C3 -->|forward| B3
    C5 -->|forward| B5
```

### 1.2 파일 구조와 역할

```mermaid
graph LR
    subgraph "Java 파일 (로직)"
        A1["MemberVO.java<br/>데이터 저장 객체"]
        A2["MemberDAO.java<br/>DB 접근 전문가"]
        A3["LoginServlet.java<br/>로그인 처리"]
        A4["JoinServlet.java<br/>회원가입 처리"]
        A5["UpdateServlet.java<br/>정보수정 처리"]
        A6["LogoutServlet.java<br/>로그아웃 처리"]
        A7["IdCheckServlet.java<br/>중복체크 처리"]
    end
    
    subgraph "JSP 파일 (화면)"
        B1["login.jsp<br/>로그인 화면"]
        B2["join.jsp<br/>회원가입 화면"]
        B3["main.jsp<br/>메인 화면"]
        B4["memberUpdate.jsp<br/>정보수정 화면"]
        B5["idCheck.jsp<br/>중복체크 팝업"]
    end
    
    subgraph "JavaScript 파일"
        C1["member.js<br/>유효성 검증"]
    end
    
    A3 -.사용.-> A2
    A4 -.사용.-> A2
    A5 -.사용.-> A2
    A7 -.사용.-> A2
    
    A2 -.생성/반환.-> A1
    
    A3 -.forward.-> B3
    A4 -.forward.-> B1
    A5 -.forward.-> B4
    A7 -.forward.-> B5
    
    B1 -.호출.-> C1
    B2 -.호출.-> C1
    B4 -.호출.-> C1
```

### 1.3 데이터 흐름 개요

```mermaid
sequenceDiagram
    participant U as 사용자
    participant B as 브라우저
    participant JSP as JSP 페이지
    participant JS as JavaScript
    participant S as Servlet
    participant DAO as MemberDAO
    participant DB as Database
    participant Session as HttpSession
    
    Note over U,Session: 전체 데이터 흐름
    
    U->>B: 페이지 요청
    B->>JSP: HTTP Request
    JSP->>U: 화면 표시
    
    U->>JSP: 데이터 입력 & 제출
    JSP->>JS: 유효성 검증
    
    alt 검증 실패
        JS->>U: alert 메시지
    else 검증 성공
        JS->>S: POST 요청
        S->>DAO: 비즈니스 로직 요청
        DAO->>DB: SQL 실행
        DB->>DAO: 결과 반환
        DAO->>S: 처리 결과
        
        alt 성공
            S->>Session: 데이터 저장
            S->>JSP: forward (성공 페이지)
        else 실패
            S->>JSP: forward (에러 메시지)
        end
        
        JSP->>U: 결과 화면
    end
```

---

## 2. 로그인 프로세스 상세 흐름도

### 2.1 로그인 전체 시퀀스 다이어그램

```mermaid
sequenceDiagram
    autonumber
    participant User as "철수 (사용자)"
    participant Browser as 브라우저
    participant LoginJSP as login.jsp
    participant MemberJS as member.js
    participant LoginServlet as LoginServlet
    participant DAO as MemberDAO
    participant DB as Oracle DB
    participant Session as HttpSession
    participant MainJSP as main.jsp
    
    Note over User,MainJSP: 로그인 프로세스 (12단계)
    
    User->>Browser: 로그인 페이지 접속 login.do
    Browser->>LoginServlet: GET /login.do
    
    activate LoginServlet
    Note over LoginServlet: doGet() 실행
    LoginServlet->>LoginServlet: removeAttribute("userid") removeAttribute("message")
    LoginServlet->>LoginJSP: forward
    deactivate LoginServlet
    
    LoginJSP->>Browser: HTML 렌더링
    Browser->>User: 로그인 화면 표시
    
    User->>LoginJSP: 아이디: chulsoo 비밀번호: 1234 로그인 버튼 클릭
    
    LoginJSP->>MemberJS: onclick="return loginCheck#40;#41;"
    activate MemberJS
    MemberJS->>MemberJS: 아이디 입력 체크
    MemberJS->>MemberJS: 비밀번호 입력 체크
    
    alt 입력값 없음
        MemberJS->>User: alert("입력해주세요")
        MemberJS-->>LoginJSP: return false (제출 중단)
    else 모든 입력값 OK
        MemberJS-->>LoginJSP: return true (제출 진행)
        deactivate MemberJS
        
        LoginJSP->>LoginServlet: " POST /login.do userid=chulsoo&pwd=1234"
        
        activate LoginServlet
        Note over LoginServlet: doPost() 실행
        LoginServlet->>LoginServlet: request.getParameter("userid") request.getParameter("pwd")
        
        LoginServlet->>DAO: userCheck("chulsoo", "1234")
        activate DAO
        DAO->>DB: SELECT pwd FROM member WHERE userid='chulsoo'
        activate DB
        DB-->>DAO: pwd='1234'
        deactivate DB
        
        DAO->>DAO: 비밀번호 비교 DB: 1234 vs 입력: 1234
        
        alt 비밀번호 일치
            DAO-->>LoginServlet: return 1 (성공)
        else 비밀번호 불일치
            DAO-->>LoginServlet: return 0 (실패)
        else DB 오류
            DAO-->>LoginServlet: return -1 (오류)
        end
        deactivate DAO
        
        alt result == 1 (성공)
            LoginServlet->>Session: session.setAttribute("userid", "chulsoo")
            
            LoginServlet->>DAO: getMember("chulsoo")
            activate DAO
            DAO->>DB: SELECT * FROM member WHERE userid='chulsoo'
            DB-->>DAO: 전체 회원 정보
            DAO->>DAO: MemberVO 객체 생성
            DAO-->>LoginServlet: MemberVO 반환
            deactivate DAO
            
            LoginServlet->>Session: session.setAttribute("loginUser", mvo)
            LoginServlet->>MainJSP: forward("/member/main.jsp")
            
        else result == 0 (실패)
            LoginServlet->>LoginServlet: request.setAttribute("message", "로그인 실패")
            LoginServlet->>LoginServlet: request.setAttribute("userid", "chulsoo")
            LoginServlet->>LoginJSP: forward("/member/login.jsp")
            LoginJSP->>User: 에러 메시지 표시 아이디 유지
        end
        deactivate LoginServlet
        
        MainJSP->>MainJSP: 세션 체크 ${empty loginUser}
        MainJSP->>Browser: HTML 렌더링
        Browser->>User: "김철수님 환영합니다" 표시
    end
```

### 2.2 LoginServlet 메소드 플로우차트

```mermaid
flowchart TD
    Start([사용자가 login.do 요청])
    
    subgraph "doGet() 메소드"
        A1[요청 받음]
        A2["removeAttribute<br/>(userid)"]
        A3["removeAttribute<br/>(message)"]
        A4[login.jsp로 forward]
    end
    
    subgraph "사용자 입력"
        B1[아이디/비밀번호 입력]
        B2{"JavaScript<br/>loginCheck()"}
        B3[alert 표시]
        B4[폼 제출]
    end
    
    subgraph "doPost() 메소드"
        C1[POST 요청 받음]
        C2["request.setCharacterEncoding<br/>(UTF-8)"]
        C3["userid = request.getParameter<br/>(userid)"]
        C4["pwd = request.getParameter<br/>(pwd)"]
        C5["dao = MemberDAO.getInstance()"]
        C6["result = dao.userCheck<br/>(userid, pwd)"]
        C7{"result 값<br/>확인"}
        
        D1["session.setAttribute<br/>(userid)"]
        D2["mvo = dao.getMember<br/>(userid)"]
        D3["session.setAttribute<br/>(loginUser, mvo)"]
        D4[url = /member/main.jsp]
        
        E1["request.setAttribute<br/>(message, 실패)"]
        E2["request.setAttribute<br/>(userid)"]
        E3[url = /member/login.jsp]
        
        F1["request.setAttribute<br/>(message, 오류)"]
        F2[url = /member/login.jsp]
        
        G1[RequestDispatcher dis]
        G2["dis.forward<br/>(request, response)"]
    end
    
    End([화면 표시])
    
    Start --> A1
    A1 --> A2
    A2 --> A3
    A3 --> A4
    A4 --> B1
    B1 --> B2
    
    B2 -->|검증 실패| B3
    B3 --> B1
    B2 -->|검증 성공| B4
    
    B4 --> C1
    C1 --> C2
    C2 --> C3
    C3 --> C4
    C4 --> C5
    C5 --> C6
    C6 --> C7
    
    C7 -->|result == 1<br/>성공| D1
    D1 --> D2
    D2 --> D3
    D3 --> D4
    D4 --> G1
    
    C7 -->|result == 0<br/>실패| E1
    E1 --> E2
    E2 --> E3
    E3 --> G1
    
    C7 -->|result == -1<br/>오류| F1
    F1 --> F2
    F2 --> G1
    
    G1 --> G2
    G2 --> End
```

### 2.3 MemberDAO.userCheck() 메소드 상세

```mermaid
flowchart TD
    Start([userCheck 시작])
    
    A1[result = -1 초기화]
    A2[try 블록 시작]
    A3["con = getConnection()"]
    A4["SQL 쿼리 준비<br/>SELECT pwd FROM member<br/>WHERE userid = ?"]
    A5["pstmt = con.prepareStatement<br/>(sql)"]
    A6["pstmt.setString<br/>(1, userid)"]
    A7["rs = pstmt.executeQuery()"]
    
    B1{"rs.next()<br/>데이터 있나?"}
    B2["dbPwd = rs.getString<br/>(pwd)"]
    B3{"dbPwd.equals(pwd)<br/>비밀번호 일치?"}
    B4[result = 1]
    B5[result = 0]
    B6[result = 0]
    
    C1[catch Exception]
    C2["e.printStackTrace()"]
    C3[result = -1]
    
    D1[finally 블록]
    D2["rs.close()"]
    D3["pstmt.close()"]
    D4["con.close()"]
    
    End([return result])
    
    Start --> A1
    A1 --> A2
    A2 --> A3
    A3 --> A4
    A4 --> A5
    A5 --> A6
    A6 --> A7
    A7 --> B1
    
    B1 -->|Yes<br/>아이디 존재| B2
    B2 --> B3
    B3 -->|Yes<br/>일치| B4
    B3 -->|No<br/>불일치| B5
    
    B1 -->|No<br/>아이디 없음| B6
    
    B4 --> D1
    B5 --> D1
    B6 --> D1
    
    A2 -.오류 발생.-> C1
    C1 --> C2
    C2 --> C3
    C3 --> D1
    
    D1 --> D2
    D2 --> D3
    D3 --> D4
    D4 --> End
```

### 2.4 세션 관리 흐름

```mermaid
stateDiagram-v2
    [*] --> 비로그인상태: 초기상태
    
    비로그인상태 --> 로그인시도: 아이디/비밀번호 입력
    
    로그인시도 --> 인증중: POST 요청
    
    인증중 --> 로그인성공: userCheck() == 1
    인증중 --> 로그인실패: userCheck() == 0
    인증중 --> DB오류: userCheck() == -1
    
    로그인성공 --> 세션생성: session.setAttribute()
    
    세션생성 --> 로그인상태: HttpSession 활성화
    
    로그인실패 --> 비로그인상태: 에러 메시지
    DB오류 --> 비로그인상태: 오류 메시지
    
    로그인상태 --> 페이지접근가능: main.jsp, memberUpdate.jsp
    
    페이지접근가능 --> 정보수정: UpdateServlet
    정보수정 --> 세션업데이트: 수정 성공
    세션업데이트 --> 로그인상태
    
    로그인상태 --> 세션만료: 30분 타임아웃
    로그인상태 --> 로그아웃: logout.do
    
    로그아웃 --> 세션무효화: session.invalidate()
    세션만료 --> 세션무효화
    
    세션무효화 --> 비로그인상태
    
    비로그인상태 --> [*]
```

---

## 3. 회원가입 프로세스 상세 흐름도

### 3.1 회원가입 전체 시퀀스 다이어그램

```mermaid
sequenceDiagram
    autonumber
    participant User as "영희 (사용자)"
    participant Browser as 브라우저
    participant JoinJSP as join.jsp
    participant MemberJS as member.js
    participant JoinServlet as JoinServlet
    participant IdCheckServlet as IdCheckServlet
    participant DAO as MemberDAO
    participant DB as Oracle DB
    participant LoginPage as login.jsp
    
    Note over User,LoginPage: 회원가입 프로세스
    
    User->>Browser: 회원가입 버튼 클릭
    Browser->>JoinServlet: GET /join.do
    
    activate JoinServlet
    Note over JoinServlet: doGet() 실행
    JoinServlet->>JoinJSP: forward
    deactivate JoinServlet
    
    JoinJSP->>Browser: HTML 렌더링
    Browser->>User: 회원가입 폼 표시
    
    User->>JoinJSP: 정보 입력 이름: 이영희 아이디: younghee 비밀번호: 5678 ...
    
    Note over User,DB: 아이디 중복 체크 프로세스
    User->>JoinJSP: 중복 체크 버튼 클릭
    JoinJSP->>MemberJS: idCheck#40;#41;
    
    activate MemberJS
    MemberJS->>MemberJS: 아이디 입력 확인
    
    alt 아이디 미입력
        MemberJS->>User: alert("아이디를 입력해주세요")
    else 아이디 입력됨
        MemberJS->>Browser: window.open(idCheck.do?userid=younghee)
        deactivate MemberJS
        
        Browser->>IdCheckServlet: " GET /idCheck.do?userid=younghee"
        
        activate IdCheckServlet
        Note over IdCheckServlet: doGet() 실행
        IdCheckServlet->>DAO: confirmID("younghee")
        
        activate DAO
        DAO->>DB: SELECT userid FROM member WHERE userid='younghee'
        
        alt 아이디 존재
            DB-->>DAO: 데이터 있음
            DAO-->>IdCheckServlet: return 1 (중복)
        else 아이디 없음
            DB-->>DAO: 데이터 없음
            DAO-->>IdCheckServlet: return 0 (사용가능)
        end
        deactivate DAO
        
        IdCheckServlet->>IdCheckServlet: request.setAttribute("result", result)
        IdCheckServlet->>Browser: forward idCheck.jsp (팝업)
        deactivate IdCheckServlet
        
        Browser->>User: 중복 체크 결과 표시
        
        alt 사용 가능 (result == 0)
            User->>Browser: "사용" 버튼 클릭
            Browser->>MemberJS: idok("younghee")
            activate MemberJS
            MemberJS->>JoinJSP: opener.frm.userid.value = "younghee"
            MemberJS->>JoinJSP: opener.frm.reid.value = "younghee"
            MemberJS->>JoinJSP: opener.frm.userid.readOnly = true
            MemberJS->>Browser: window.close()
            deactivate MemberJS
        else 중복 (result == 1)
            User->>User: 다른 아이디 입력 필요
        end
    end
    
    Note over User,LoginPage: 회원가입 제출
    User->>JoinJSP: 확인 버튼 클릭
    JoinJSP->>MemberJS: onclick="return joinCheck#40;#41;"
    
    activate MemberJS
    MemberJS->>MemberJS: 이름 체크
    MemberJS->>MemberJS: 아이디 길이 체크 (4자 이상)
    MemberJS->>MemberJS: 비밀번호 체크
    MemberJS->>MemberJS: 비밀번호 일치 체크
    MemberJS->>MemberJS: reid 체크 (중복체크 했는지)
    
    alt 검증 실패
        MemberJS->>User: alert(에러 메시지)
        MemberJS-->>JoinJSP: return false
    else 검증 성공
        MemberJS-->>JoinJSP: return true
        deactivate MemberJS
        
        JoinJSP->>JoinServlet: POST /join.do 전체 회원 정보
        
        activate JoinServlet
        Note over JoinServlet: doPost() 실행
        JoinServlet->>JoinServlet: 모든 파라미터 수신
        JoinServlet->>JoinServlet: reid와 userid 비교
        
        alt reid 없거나 불일치
            JoinServlet->>User: "중복 체크 필요" 메시지
        else reid와 userid 일치
            JoinServlet->>JoinServlet: MemberVO 객체 생성 모든 정보 설정
            JoinServlet->>DAO: insertMember(mvo)
            
            activate DAO
            DAO->>DB: INSERT INTO member VALUES (이영희, younghee, ...)
            
            alt 저장 성공
                DB-->>DAO: 1 row inserted
                DAO-->>JoinServlet: return 1
            else 저장 실패
                DB-->>DAO: 오류 (예: 중복키)
                DAO-->>JoinServlet: return -1
            end
            deactivate DAO
            
            alt 성공 (result == 1)
                JoinServlet->>JoinServlet: request.setAttribute("message", "가입 완료")
                JoinServlet->>LoginPage: redirect /login.do
            else 실패
                JoinServlet->>JoinServlet: request.setAttribute("message", "가입 실패")
                JoinServlet->>JoinJSP: forward
            end
        end
        deactivate JoinServlet
        
        LoginPage->>User: "회원 가입이 완료되었습니다" 표시
    end
```

### 3.2 JoinServlet 메소드 플로우차트

```mermaid
flowchart TD
    Start([사용자가 join.do 요청])
    
    subgraph "doGet() 메소드"
        A1[요청 받음]
        A2[join.jsp로 forward]
    end
    
    subgraph "사용자 입력 및 검증"
        B1[회원 정보 입력]
        B2[아이디 중복 체크]
        B3[확인 버튼 클릭]
        B4{"JavaScript<br/>joinCheck()"}
        B5[alert 표시]
    end
    
    subgraph "doPost() 메소드"
        C1[POST 요청 받음]
        C2[인코딩 설정]
        C3["name = getParameter<br/>(name)"]
        C4["userid = getParameter<br/>(userid)"]
        C5["pwd = getParameter<br/>(pwd)"]
        C6["email = getParameter<br/>(email)"]
        C7["phone = getParameter<br/>(phone)"]
        C8["admin = getParameter<br/>(admin)"]
        C9["reid = getParameter<br/>(reid)"]
        
        D1{"reid == null 또는<br/>!reid.equals(userid)"}
        D2[message = 중복체크 필요]
        D3[join.jsp로 forward]
        
        E1[admin 문자열을 int로 변환]
        E2["MemberVO mvo =<br/>new MemberVO()"]
        E3["mvo에 모든 데이터 설정<br/>setName, setUserid, ..."]
        E4["dao = MemberDAO<br/>.getInstance()"]
        E5["result = dao.insertMember<br/>(mvo)"]
        
        F1{result == 1}
        F2[message = 회원 가입 완료]
        F3[url = /login.do]
        F4[message = 가입 실패]
        F5[url = /member/join.jsp]
        
        G1[RequestDispatcher]
        G2["forward<br/>(request, response)"]
    end
    
    End([화면 표시])
    
    Start --> A1
    A1 --> A2
    A2 --> B1
    B1 --> B2
    B2 --> B3
    B3 --> B4
    
    B4 -->|검증 실패| B5
    B5 --> B1
    B4 -->|검증 성공| C1
    
    C1 --> C2
    C2 --> C3
    C3 --> C4
    C4 --> C5
    C5 --> C6
    C6 --> C7
    C7 --> C8
    C8 --> C9
    C9 --> D1
    
    D1 -->|Yes<br/>중복체크 안함| D2
    D2 --> D3
    D3 --> End
    
    D1 -->|No<br/>중복체크 완료| E1
    E1 --> E2
    E2 --> E3
    E3 --> E4
    E4 --> E5
    E5 --> F1
    
    F1 -->|Yes<br/>성공| F2
    F2 --> F3
    F3 --> G1
    
    F1 -->|No<br/>실패| F4
    F4 --> F5
    F5 --> G1
    
    G1 --> G2
    G2 --> End
```

---

## 4. 아이디 중복 체크 프로세스

### 4.1 아이디 중복 체크 상세 흐름

```mermaid
sequenceDiagram
    autonumber
    participant User as 사용자
    participant JoinPage as "join.jsp (부모 창)"
    participant JS as member.js
    participant Popup as "idCheck.jsp (팝업 창)"
    participant Servlet as IdCheckServlet
    participant DAO as MemberDAO
    participant DB as Database
    
    Note over User,DB: 아이디 중복 체크 전체 프로세스
    
    User->>JoinPage: 아이디 입력: "younghee"
    User->>JoinPage: "중복 체크" 버튼 클릭
    
    JoinPage->>JS: onclick="idCheck()"
    activate JS
    
    JS->>JS: userid = document.frm.userid.value
    
    alt userid가 비어있음
        JS->>User: alert("아이디를 입력해주세요")
    else userid 입력됨
        JS->>JS: url = contextPath + "/idCheck.do?userid=" + userid
        JS->>Popup: window.open(url, "popup", options)
        deactivate JS
        
        Popup->>Servlet: " GET /idCheck.do?userid=younghee"
        
        activate Servlet
        Servlet->>Servlet: userid = request.getParameter("userid")
        
        alt userid가 null 또는 공백
            Servlet->>Servlet: message = "아이디를 입력해주세요"
            Servlet->>Popup: forward (메시지만)
            Popup->>User: 메시지 표시
        else userid 입력됨
            Servlet->>DAO: confirmID(userid)
            
            activate DAO
            DAO->>DAO: SQL 준비 SELECT userid FROM member WHERE userid = ?
            DAO->>DB: executeQuery()
            
            alt 데이터 존재 (rs.next() == true)
                DB-->>DAO: userid 반환
                DAO->>DAO: result = 1 (중복)
            else 데이터 없음 (rs.next() == false)
                DB-->>DAO: 빈 결과셋
                DAO->>DAO: result = 0 (사용가능)
            end
            
            DAO-->>Servlet: return result
            deactivate DAO
            
            Servlet->>Servlet: request.setAttribute("userid", userid)
            Servlet->>Servlet: request.setAttribute("result", result)
            Servlet->>Popup: forward /member/idCheck.jsp
            deactivate Servlet
            
            alt result == 1 (중복)
                Popup->>Popup: <c:if test="${result == 1}">
                Popup->>User: "younghee는 이미 사용 중인 아이디입니다"
                User->>JoinPage: 다른 아이디 입력 필요
                
            else result == 0 (사용가능)
                Popup->>Popup: <c:if test="${result == 0}">
                Popup->>User: "younghee는 사용 가능한 아이디입니다" [사용] 버튼 표시
                
                User->>Popup: [사용] 버튼 클릭
                Popup->>JS: onclick="idok('younghee')"
                
                activate JS
                JS->>JoinPage: window.opener.document.frm.userid.value = "younghee"
                JS->>JoinPage: window.opener.document.frm.reid.value = "younghee"
                JS->>JoinPage: window.opener.document.frm.userid.readOnly = true
                JS->>Popup: window.close()
                deactivate JS
                
                Note over JoinPage: 부모 창 상태 변경 - userid: "younghee" (읽기전용) - reid: "younghee" (hidden)
            end
        end
    end
```

### 4.2 MemberDAO.confirmID() 메소드

```mermaid
flowchart TD
    Start([confirmID 시작])
    
    A1[result = -1 초기화]
    A2[try 블록]
    A3["con = getConnection()"]
    A4["SQL 준비<br/>SELECT userid FROM member<br/>WHERE userid = ?"]
    A5["pstmt.setString<br/>(1, userid)"]
    A6["rs = executeQuery()"]
    
    B1{"rs.next()?"}
    B2[result = 1 중복]
    B3[result = 0 사용가능]
    
    C1[catch Exception]
    C2["e.printStackTrace()"]
    C3[result = -1]
    
    D1[finally]
    D2["리소스 정리<br/>rs, pstmt, con close"]
    
    End([return result])
    
    Start --> A1
    A1 --> A2
    A2 --> A3
    A3 --> A4
    A4 --> A5
    A5 --> A6
    A6 --> B1
    
    B1 -->|true<br/>데이터 있음| B2
    B1 -->|false<br/>데이터 없음| B3
    
    B2 --> D1
    B3 --> D1
    
    A2 -.오류.-> C1
    C1 --> C2
    C2 --> C3
    C3 --> D1
    
    D1 --> D2
    D2 --> End
```

### 4.3 팝업과 부모 창 통신

```mermaid
graph TB
    subgraph "부모 창 (join.jsp)"
        A1[form name='frm']
        A2[input name='userid']
        A3[input name='reid' type='hidden']
        A4[button 중복 체크]
    end
    
    subgraph "JavaScript (member.js)"
        B1["function idCheck()"]
        B2["window.open()"]
        B3["function idok(userid)"]
    end
    
    subgraph "팝업 창 (idCheck.jsp)"
        C1[아이디 입력]
        C2[중복 체크 버튼]
        C3[결과 표시]
        C4[사용 버튼 result==0일 때만]
    end
    
    subgraph "서버 (IdCheckServlet)"
        D1[doGet()]
        D2[confirmID() 호출]
        D3[결과 반환]
    end
    
    A4 -->|클릭| B1
    B1 -->|userid 가져오기| A2
    B1 -->|새 창 열기| B2
    B2 -->|GET 요청| D1
    
    D1 --> D2
    D2 --> D3
    D3 -->|forward| C3
    
    C4 -->|클릭| B3
    B3 -->|window.opener| A2
    B3 -->|값 설정| A3
    B3 -.readOnly=true.-> A2
    B3 -.window.close.-> C1
    
    style A3 fill:#ffeb3b
    style B3 fill:#4caf50
    style C4 fill:#2196f3
```

---

**Part 2로 계속됩니다...**

