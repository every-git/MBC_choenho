# JSP 로그인 시스템 흐름도 - Part 2

> 회원정보 수정과 로그아웃 프로세스 상세 도식화

---

## 📋 목차

1. [회원정보 수정 프로세스](#1-회원정보-수정-프로세스)
2. [로그아웃 프로세스](#2-로그아웃-프로세스)
3. [세션 생명주기 관리](#3-세션-생명주기-관리)
4. [전체 시스템 통합 흐름도](#4-전체-시스템-통합-흐름도)

---

## 1. 회원정보 수정 프로세스

### 1.1 회원정보 수정 전체 시퀀스 다이어그램

```mermaid
sequenceDiagram
    autonumber
    participant User as "철수 (로그인된 사용자)"
    participant MainJSP as main.jsp
    participant UpdateServlet as UpdateServlet
    participant DAO as MemberDAO
    participant DB as Oracle DB
    participant Session as HttpSession
    participant UpdateJSP as memberUpdate.jsp
    participant MemberJS as member.js
    
    Note over User,MemberJS: 회원정보 수정 프로세스
    
    User->>MainJSP: "회원정보변경" 버튼 클릭
    MainJSP->>UpdateServlet: " GET /memberUpdate.do?userid=chulsoo"
    
    activate UpdateServlet
    Note over UpdateServlet: doGet() 실행
    
    UpdateServlet->>Session: session.getAttribute("loginUser")
    
    alt 세션 없음 (로그인 안됨)
        Session-->>UpdateServlet: null
        UpdateServlet->>User: redirect /login.do "로그인이 필요합니다"
    else 세션 있음 (로그인됨)
        Session-->>UpdateServlet: MemberVO loginUser
        
        UpdateServlet->>UpdateServlet: userid = request.getParameter("userid")
        
        Note over UpdateServlet: 보안 체크: URL 파라미터 검증
        UpdateServlet->>UpdateServlet: if (!userid.equals(loginUser.getUserid())) userid = loginUser.getUserid()
        
        UpdateServlet->>DAO: getMember(userid)
        activate DAO
        DAO->>DB: SELECT * FROM member WHERE userid='chulsoo'
        DB-->>DAO: 최신 회원 정보
        DAO->>DAO: MemberVO 객체 생성
        DAO-->>UpdateServlet: MemberVO mvo
        deactivate DAO
        
        UpdateServlet->>UpdateServlet: request.setAttribute("mvo", mvo)
        UpdateServlet->>UpdateJSP: forward
    end
    deactivate UpdateServlet
    
    UpdateJSP->>UpdateJSP: 기존 정보 표시 - 이름 (readonly) - 아이디 (readonly) - 이메일 - 전화번호 - 비밀번호 (새로 입력)
    UpdateJSP->>User: 수정 화면 표시
    
    User->>UpdateJSP: 정보 수정 email: chulsoo_new@naver.com phone: 010-9999-8888
    User->>UpdateJSP: "확인" 버튼 클릭
    
    UpdateJSP->>MemberJS: onclick="return joinCheck()"
    activate MemberJS
    MemberJS->>MemberJS: 모든 필드 유효성 검증
    
    alt 검증 실패
        MemberJS->>User: alert(에러 메시지)
        MemberJS-->>UpdateJSP: return false
    else 검증 성공
        MemberJS-->>UpdateJSP: return true
        deactivate MemberJS
        
        UpdateJSP->>UpdateServlet: POST /memberUpdate.do 수정된 전체 정보
        
        activate UpdateServlet
        Note over UpdateServlet: doPost() 실행
        
        UpdateServlet->>UpdateServlet: 모든 파라미터 수신 userid, pwd, email, phone, admin
        UpdateServlet->>UpdateServlet: MemberVO mvo 생성 및 설정
        
        UpdateServlet->>DAO: updateMember(mvo)
        activate DAO
        DAO->>DB: UPDATE member SET pwd=?, email=?, phone=?, admin=? WHERE userid='chulsoo'
        
        alt 업데이트 성공
            DB-->>DAO: 1 row updated
            DAO-->>UpdateServlet: return 1
        else 업데이트 실패
            DB-->>DAO: 0 row updated
            DAO-->>UpdateServlet: return -1
        end
        deactivate DAO
        
        alt result == 1 (성공)
            Note over UpdateServlet,Session: 세션 업데이트 필수!
            UpdateServlet->>DAO: getMember(userid)
            activate DAO
            DAO->>DB: SELECT * FROM member WHERE userid='chulsoo'
            DB-->>DAO: 최신 정보
            DAO-->>UpdateServlet: MemberVO updatedMvo
            deactivate DAO
            
            UpdateServlet->>Session: session.setAttribute("loginUser", updatedMvo)
            UpdateServlet->>UpdateServlet: request.setAttribute("message", "수정 완료")
            UpdateServlet->>MainJSP: forward /member/main.jsp
            
            MainJSP->>User: "회원정보가 수정되었습니다" 최신 정보 표시
            
        else result != 1 (실패)
            UpdateServlet->>UpdateServlet: request.setAttribute("message", "수정 실패")
            UpdateServlet->>UpdateJSP: forward
            UpdateJSP->>User: 에러 메시지 표시
        end
        deactivate UpdateServlet
    end
```

### 1.2 UpdateServlet 메소드 플로우차트

```mermaid
flowchart TD
    Start([사용자 요청])
    
    subgraph "doGet() - 수정 페이지 표시"
        A1[GET 요청 받음]
        A2["session = request<br/>.getSession()"]
        A3["loginUser = session<br/>.getAttribute(loginUser)"]
        A4{"loginUser<br/>== null?"}
        A5[redirect /login.do]
        A6["userid = request<br/>.getParameter(userid)"]
        A7{"userid.equals<br/>(loginUser.getUserid())?"}
        A8["userid = loginUser.getUserid()<br/>보안: 강제 변경"]
        A9["dao = MemberDAO<br/>.getInstance()"]
        A10["mvo = dao.getMember<br/>(userid)"]
        A11["request.setAttribute<br/>(mvo, mvo)"]
        A12[forward /member/memberUpdate.jsp]
    end
    
    subgraph "사용자 입력"
        B1[기존 정보 표시]
        B2[수정할 내용 입력]
        B3[확인 버튼 클릭]
        B4{"JavaScript<br/>joinCheck()"}
        B5[alert 메시지]
    end
    
    subgraph "doPost() - 수정 처리"
        C1[POST 요청 받음]
        C2[인코딩 설정]
        C3["모든 파라미터 수신<br/>userid, pwd, email,<br/>phone, admin"]
        C4["MemberVO mvo =<br/>new MemberVO()"]
        C5[mvo에 모든 값 설정]
        C6["dao = MemberDAO<br/>.getInstance()"]
        C7["result = dao.updateMember<br/>(mvo)"]
        
        D1{result == 1?}
        
        E1["updatedMvo = dao.getMember<br/>(userid)"]
        E2["session.setAttribute<br/>(loginUser, updatedMvo)"]
        E3[message = 수정 완료]
        E4[url = /member/main.jsp]
        
        F1[message = 수정 실패]
        F2[url = /member/memberUpdate.jsp]
        
        G1["request.setAttribute<br/>(message)"]
        G2["forward(url)"]
    end
    
    End([화면 표시])
    
    Start --> A1
    A1 --> A2
    A2 --> A3
    A3 --> A4
    
    A4 -->|Yes<br/>로그인 안됨| A5
    A5 --> End
    
    A4 -->|No<br/>로그인됨| A6
    A6 --> A7
    
    A7 -->|No<br/>다른 사용자| A8
    A8 --> A9
    
    A7 -->|Yes<br/>본인| A9
    A9 --> A10
    A10 --> A11
    A11 --> A12
    A12 --> B1
    
    B1 --> B2
    B2 --> B3
    B3 --> B4
    
    B4 -->|검증 실패| B5
    B5 --> B2
    
    B4 -->|검증 성공| C1
    C1 --> C2
    C2 --> C3
    C3 --> C4
    C4 --> C5
    C5 --> C6
    C6 --> C7
    C7 --> D1
    
    D1 -->|Yes<br/>성공| E1
    E1 --> E2
    E2 --> E3
    E3 --> E4
    E4 --> G1
    
    D1 -->|No<br/>실패| F1
    F1 --> F2
    F2 --> G1
    
    G1 --> G2
    G2 --> End
```

### 1.3 MemberDAO.updateMember() 메소드

```mermaid
flowchart TD
    Start([updateMember 시작])
    
    A1[result = -1 초기화]
    A2[try 블록]
    A3["con = getConnection()"]
    A4["SQL 준비<br/>UPDATE member SET<br/>pwd=?, email=?, phone=?,<br/>admin=? WHERE userid=?"]
    A5["pstmt = con.prepareStatement<br/>(sql)"]
    A6["pstmt.setString<br/>(1, mvo.getPwd())"]
    A7["pstmt.setString<br/>(2, mvo.getEmail())"]
    A8["pstmt.setString<br/>(3, mvo.getPhone())"]
    A9["pstmt.setInt<br/>(4, mvo.getAdmin())"]
    A10["pstmt.setString<br/>(5, mvo.getUserid())"]
    A11["result = pstmt<br/>.executeUpdate()"]
    
    B1{result > 0?}
    B2["업데이트 성공<br/>affected rows = result"]
    B3["업데이트 실패<br/>result = 0"]
    
    C1[catch Exception]
    C2["e.printStackTrace()"]
    C3[result = -1]
    
    D1[finally]
    D2["리소스 정리<br/>pstmt, con close"]
    
    End([return result])
    
    Start --> A1
    A1 --> A2
    A2 --> A3
    A3 --> A4
    A4 --> A5
    A5 --> A6
    A6 --> A7
    A7 --> A8
    A8 --> A9
    A9 --> A10
    A10 --> A11
    A11 --> B1
    
    B1 -->|Yes| B2
    B1 -->|No| B3
    
    B2 --> D1
    B3 --> D1
    
    A2 -.오류.-> C1
    C1 --> C2
    C2 --> C3
    C3 --> D1
    
    D1 --> D2
    D2 --> End
```

### 1.4 세션 업데이트의 중요성

```mermaid
graph TB
    subgraph "세션 업데이트 전"
        A1[세션 loginUser]
        A2["email: chulsoo@example.com<br/>phone: 010-1234-5678"]
        A3[DB]
        A4["email: chulsoo_new@naver.com<br/>phone: 010-9999-8888"]
    end
    
    subgraph "세션 업데이트"
        B1["UpdateServlet.doPost()"]
        B2["1\. DB 업데이트 성공"]
        B3["2\. dao.getMember(userid)"]
        B4["3\. 최신 정보 가져오기"]
        B5["4\. session.setAttribute<br/>(loginUser, updatedMvo)"]
    end
    
    subgraph "세션 업데이트 후"
        C1[세션 loginUser]
        C2["email: chulsoo_new@naver.com<br/>phone: 010-9999-8888"]
        C3[DB]
        C4["email: chulsoo_new@naver.com<br/>phone: 010-9999-8888"]
    end
    
    A1 --> A2
    A3 --> A4
    
    A2 -.불일치!.-> A4
    
    B1 --> B2
    B2 --> B3
    B3 --> B4
    B4 --> B5
    
    B5 --> C1
    C1 --> C2
    C3 --> C4
    
    C2 -.일치!.-> C4
    
    style A2 fill:#ffcdd2
    style A4 fill:#ffcdd2
    style C2 fill:#c8e6c9
    style C4 fill:#c8e6c9
```

### 1.5 보안: URL 파라미터 검증

```mermaid
sequenceDiagram
    participant User as "악의적 사용자 (chulsoo로 로그인)"
    participant Browser as 브라우저
    participant Servlet as UpdateServlet
    participant Session as HttpSession
    participant DAO as MemberDAO
    
    Note over User,DAO: 시나리오: URL 조작 시도
    
    User->>Browser: URL 직접 수정 /memberUpdate.do?userid=younghee
    Browser->>Servlet: GET 요청
    
    activate Servlet
    Servlet->>Session: getAttribute("loginUser")
    Session-->>Servlet: loginUser.userid = "chulsoo"
    
    Servlet->>Servlet: 요청 userid = "younghee"
    
    Note over Servlet: 보안 체크 실행
    Servlet->>Servlet: if (!userid.equals(loginUser.getUserid()))
    Servlet->>Servlet: userid = loginUser.getUserid()
    
    Note over Servlet: userid가 "chulsoo"로 강제 변경됨!
    
    Servlet->>DAO: getMember("chulsoo")
    DAO-->>Servlet: chulsoo의 정보
    
    Servlet->>User: chulsoo의 수정 화면 표시 (younghee 정보 접근 차단!)
    deactivate Servlet
    
    Note over User,DAO: ✅ 보안 위협 차단 성공
```

---

## 2. 로그아웃 프로세스

### 2.1 로그아웃 시퀀스 다이어그램

```mermaid
sequenceDiagram
    autonumber
    participant User as 사용자
    participant MainJSP as main.jsp
    participant LogoutServlet as LogoutServlet
    participant Session as HttpSession
    participant LoginServlet as LoginServlet
    participant LoginJSP as login.jsp
    
    Note over User,LoginJSP: 로그아웃 프로세스 (매우 간단!)
    
    User->>MainJSP: 로그인된 상태에서 "로그아웃" 버튼 클릭
    
    MainJSP->>LogoutServlet: POST /logout.do (또는 GET)
    
    activate LogoutServlet
    Note over LogoutServlet: doPost() → doGet() 호출
    
    LogoutServlet->>LogoutServlet: doGet(request, response)
    
    Note over LogoutServlet: doGet() 실행
    
    LogoutServlet->>Session: session = request.getSession()
    
    alt 세션 존재
        Session-->>LogoutServlet: HttpSession 객체
        
        Note over LogoutServlet,Session: 세션 무효화 실행
        LogoutServlet->>Session: session.invalidate()
        
        Note over Session: 모든 세션 데이터 삭제 - userid - loginUser - 기타 모든 속성
        
        Session-->>LogoutServlet: 세션 무효화 완료
        
    else 세션 없음
        Session-->>LogoutServlet: null (이미 로그아웃 상태)
    end
    
    LogoutServlet->>LoginServlet: response.sendRedirect (contextPath + "/login.do")
    deactivate LogoutServlet
    
    Note over LogoutServlet,LoginServlet: redirect = 새로운 GET 요청
    
    activate LoginServlet
    LoginServlet->>LoginServlet: doGet() 실행
    LoginServlet->>LoginJSP: forward
    deactivate LoginServlet
    
    LoginJSP->>User: 로그인 화면 표시 (세션 없음 = 비로그인 상태)
```

### 2.2 LogoutServlet 플로우차트

```mermaid
flowchart TD
    Start([로그아웃 요청])
    
    subgraph "doPost() 메소드"
        A1[POST 요청 받음]
        A2[doGet(request, response) 호출]
    end
    
    subgraph "doGet() 메소드"
        B1["GET 요청 받음<br/>또는 doPost에서 호출"]
        B2["session = request<br/>.getSession()"]
        B3{"session<br/>!= null?"}
        B4["session.invalidate()<br/>세션 무효화"]
        B5["모든 세션 데이터 삭제<br/>- userid<br/>- loginUser<br/>- 기타 속성"]
        B6["contextPath = request<br/>.getContextPath()"]
        B7["response.sendRedirect<br/>(contextPath + /login.do)"]
    end
    
    C1[LoginServlet으로 리다이렉트]
    C2[login.jsp 표시]
    
    End([로그인 화면])
    
    Start --> A1
    A1 --> A2
    A2 --> B1
    
    Start -.GET 요청.-> B1
    
    B1 --> B2
    B2 --> B3
    
    B3 -->|Yes<br/>세션 있음| B4
    B4 --> B5
    B5 --> B6
    
    B3 -->|No<br/>세션 없음| B6
    
    B6 --> B7
    B7 --> C1
    C1 --> C2
    C2 --> End
```

### 2.3 세션 무효화 상세

```mermaid
stateDiagram-v2
    [*] --> 로그인상태: 로그인 성공
    
    state 로그인상태 {
        [*] --> 세션활성
        
        state 세션활성 {
            state "세션 데이터" as data
            data: userid = "chulsoo"
            data: loginUser = MemberVO{...}
            data: 생성시간 = timestamp
            data: 마지막접근 = timestamp
        }
    }
    
    로그인상태 --> 로그아웃처리: logout.do 호출
    
    state 로그아웃처리 {
        [*] --> 세션가져오기
        세션가져오기 --> 세션무효화: session.invalidate()
        세션무효화 --> 리다이렉트: sendRedirect(login.do)
    }
    
    로그아웃처리 --> 비로그인상태
    
    state 비로그인상태 {
        [*] --> 세션없음
        
        state 세션없음 {
            state "세션 상태" as nodata
            nodata: session = null
            nodata: 또는
            nodata: 세션 데이터 없음
        }
    }
    
    비로그인상태 --> [*]
```

### 2.4 forward vs redirect 비교 (로그아웃 시나리오)

```mermaid
graph TB
    subgraph "Forward 사용 시 (X)"
        A1[로그아웃 요청]
        A2[LogoutServlet]
        A3["session.invalidate()"]
        A4[forward login.jsp]
        A5[브라우저 주소창]
        A6[/logout.do]
        
        A1 --> A2
        A2 --> A3
        A3 --> A4
        A4 --> A5
        A5 --> A6
        
        style A6 fill:#ffcdd2
    end
    
    subgraph "Redirect 사용 시 (O)"
        B1[로그아웃 요청]
        B2[LogoutServlet]
        B3["session.invalidate()"]
        B4[redirect /login.do]
        B5[브라우저 새 요청]
        B6[LoginServlet]
        B7[브라우저 주소창]
        B8[/login.do]
        
        B1 --> B2
        B2 --> B3
        B3 --> B4
        B4 --> B5
        B5 --> B6
        B6 --> B7
        B7 --> B8
        
        style B8 fill:#c8e6c9
    end
    
    Note1["문제: URL이 logout.do로 남음<br/>새로고침 시 혼란"]
    Note2["좋음: URL이 login.do로 변경<br/>명확한 상태 표시"]
    
    A6 -.-> Note1
    B8 -.-> Note2
```

---

## 3. 세션 생명주기 관리

### 3.1 세션 전체 생명주기

```mermaid
stateDiagram-v2
    [*] --> 세션없음: 초기 상태
    
    세션없음 --> 로그인시도: 아이디/비밀번호 입력
    
    로그인시도 --> 인증처리: LoginServlet.doPost()
    
    state 인증처리 {
        [*] --> DB조회
        DB조회 --> 비밀번호검증
        비밀번호검증 --> [*]
    }
    
    인증처리 --> 세션생성: 인증 성공
    인증처리 --> 세션없음: 인증 실패
    
    state 세션생성 {
        [*] --> setAttribute
        setAttribute: session.setAttribute("userid")
        setAttribute: session.setAttribute("loginUser")
        setAttribute --> [*]
    }
    
    세션생성 --> 세션활성: HttpSession 객체 생성
    
    state 세션활성 {
        [*] --> 페이지이동
        페이지이동 --> 세션확인
        세션확인 --> 페이지접근허용: 세션 유효
        페이지접근허용 --> 페이지이동
        
        세션확인 --> 로그인페이지이동: 세션 무효
    }
    
    세션활성 --> 정보수정: UpdateServlet
    
    state 정보수정 {
        [*] --> DB업데이트
        DB업데이트 --> 세션업데이트
        세션업데이트: session.setAttribute("loginUser", updatedMvo)
        세션업데이트 --> [*]
    }
    
    정보수정 --> 세션활성: 수정 완료
    
    세션활성 --> 세션무효화: 다음 중 하나
    
    state 세션무효화 {
        [*] --> 로그아웃: session.invalidate()
        [*] --> 타임아웃: 30분 무활동
        [*] --> 브라우저종료: 완전 종료
        [*] --> 서버재시작: 서버 다운/재시작
        
        로그아웃 --> [*]
        타임아웃 --> [*]
        브라우저종료 --> [*]
        서버재시작 --> [*]
    }
    
    세션무효화 --> 세션없음
    
    세션없음 --> [*]
```

### 3.2 세션 타임아웃 메커니즘

```mermaid
sequenceDiagram
    participant User as 사용자
    participant Browser as 브라우저
    participant Server as 서버
    participant Session as HttpSession
    participant Timer as 타임아웃 타이머
    
    Note over User,Timer: 세션 타임아웃 시나리오
    
    User->>Browser: 로그인 성공
    Browser->>Server: 세션 생성 요청
    Server->>Session: 새 세션 생성
    activate Session
    Session->>Timer: 타임아웃 타이머 시작 (30분)
    activate Timer
    
    Note over Session,Timer: 세션 생성 시각: 10:00 마지막 접근: 10:00
    
    User->>Browser: 페이지 이동 (10:15)
    Browser->>Server: 요청 + 세션 ID
    Server->>Session: lastAccessedTime 업데이트
    Session->>Timer: 타이머 리셋
    
    Note over Session,Timer: 마지막 접근: 10:15 만료 예정: 10:45
    
    User->>Browser: 또 페이지 이동 (10:20)
    Browser->>Server: 요청 + 세션 ID
    Server->>Session: lastAccessedTime 업데이트
    Session->>Timer: 타이머 리셋
    
    Note over Session,Timer: 마지막 접근: 10:20 만료 예정: 10:50
    
    Note over User: 30분간 활동 없음...
    
    Timer->>Timer: 시간 체크 (10:50)
    Timer->>Session: 타임아웃 발생!
    Session->>Session: 세션 자동 무효화
    deactivate Session
    deactivate Timer
    
    User->>Browser: 페이지 이동 시도 (10:55)
    Browser->>Server: 요청 + 세션 ID
    Server->>Session: 세션 조회
    Session-->>Server: null (만료됨)
    Server->>User: 로그인 페이지로 리다이렉트 "세션이 만료되었습니다"
```

### 3.3 세션 데이터 흐름

```mermaid
graph TB
    subgraph "로그인 시"
        A1[LoginServlet]
        A2["session.setAttribute<br/>(userid, chulsoo)"]
        A3["session.setAttribute<br/>(loginUser, MemberVO)"]
        A4[세션 저장소]
        
        A1 --> A2
        A2 --> A3
        A3 --> A4
    end
    
    subgraph "페이지 접근 시"
        B1[main.jsp]
        B2["session.getAttribute<br/>(loginUser)"]
        B3[세션 저장소]
        B4{세션 있나?}
        B5["페이지 표시<br/>환영 메시지"]
        B6[login.do로 리다이렉트]
        
        B1 --> B2
        B2 --> B3
        B3 --> B4
        B4 -->|Yes| B5
        B4 -->|No| B6
    end
    
    subgraph "정보 수정 시"
        C1[UpdateServlet]
        C2[DB 업데이트]
        C3["dao.getMember<br/>(최신 정보)"]
        C4["session.setAttribute<br/>(loginUser, updatedMvo)"]
        C5[세션 저장소]
        
        C1 --> C2
        C2 --> C3
        C3 --> C4
        C4 --> C5
    end
    
    subgraph "로그아웃 시"
        D1[LogoutServlet]
        D2["session.invalidate()"]
        D3[세션 저장소]
        D4[세션 삭제]
        
        D1 --> D2
        D2 --> D3
        D3 --> D4
    end
    
    A4 -.세션 공유.-> B3
    B3 -.세션 공유.-> C5
    C5 -.세션 공유.-> D3
```

---

## 4. 전체 시스템 통합 흐름도

### 4.1 모든 기능 통합 시퀀스

```mermaid
sequenceDiagram
    participant U as 사용자
    participant L as login.jsp
    participant LS as LoginServlet
    participant J as join.jsp
    participant JS as JoinServlet
    participant M as main.jsp
    participant UPS as UpdateServlet
    participant UP as memberUpdate.jsp
    participant LOS as LogoutServlet
    participant DAO as MemberDAO
    participant DB as Database
    participant S as HttpSession
    
    Note over U,S: 1. 회원가입
    U->>J: 회원가입 페이지
    U->>JS: 회원정보 제출
    JS->>DAO: insertMember()
    DAO->>DB: INSERT
    DB-->>JS: 성공
    JS->>L: 가입 완료, 로그인하세요
    
    Note over U,S: 2. 로그인
    U->>L: 아이디/비밀번호 입력
    U->>LS: 로그인 제출
    LS->>DAO: userCheck()
    DAO->>DB: SELECT
    DB-->>LS: 인증 성공
    LS->>S: 세션 생성
    LS->>M: 메인 페이지로
    
    Note over U,S: 3. 메인 페이지 사용
    M->>S: 세션 확인
    S-->>M: 로그인 상태
    M->>U: 환영 메시지
    
    Note over U,S: 4. 정보 수정
    U->>M: 회원정보변경 클릭
    M->>UPS: GET /memberUpdate.do
    UPS->>S: 로그인 체크
    UPS->>DAO: getMember()
    DAO->>DB: SELECT
    UPS->>UP: 수정 화면
    U->>UP: 정보 수정
    U->>UPS: POST 제출
    UPS->>DAO: updateMember()
    DAO->>DB: UPDATE
    UPS->>S: 세션 업데이트
    UPS->>M: 수정 완료
    
    Note over U,S: 5. 로그아웃
    U->>M: 로그아웃 클릭
    M->>LOS: 로그아웃 요청
    LOS->>S: session.invalidate()
    S-->>LOS: 세션 삭제
    LOS->>L: 로그인 페이지로
```

### 4.2 페이지 간 네비게이션 맵

```mermaid
graph TB
    Start([웹사이트 접속])
    
    subgraph "비로그인 영역"
        Login[login.jsp 로그인 페이지]
        Join[join.jsp 회원가입 페이지]
        IdCheck[idCheck.jsp 중복체크 팝업]
    end
    
    subgraph "로그인 영역 (세션 필요)"
        Main[main.jsp 메인 페이지]
        Update[memberUpdate.jsp 정보수정 페이지]
    end
    
    subgraph "컨트롤러 (Servlet)"
        LS[LoginServlet /login.do]
        JoS[JoinServlet /join.do]
        ICS[IdCheckServlet /idCheck.do]
        UPS[UpdateServlet /memberUpdate.do]
        LOS[LogoutServlet /logout.do]
    end
    
    Start --> Login
    
    Login -->|회원가입 버튼| Join
    Login -->|로그인 제출| LS
    
    Join -->|중복체크| ICS
    ICS --> IdCheck
    IdCheck -.사용 클릭.-> Join
    Join -->|가입 제출| JoS
    JoS -->|성공| Login
    JoS -->|실패| Join
    
    LS -->|성공 세션 생성| Main
    LS -->|실패| Login
    
    Main -->|정보수정| UPS
    UPS -->|GET| Update
    Update -->|POST| UPS
    UPS -->|성공| Main
    UPS -->|실패| Update
    
    Main -->|로그아웃| LOS
    Update -->|취소| Main
    
    LOS -->|세션 삭제| Login
    
    Main -.세션 없으면.-> Login
    Update -.세션 없으면.-> Login
    
    style Login fill:#e3f2fd
    style Join fill:#e3f2fd
    style Main fill:#c8e6c9
    style Update fill:#c8e6c9
    style LOS fill:#ffcdd2
```

### 4.3 데이터베이스 연동 전체 흐름

```mermaid
graph TB
    subgraph "프레젠테이션 계층"
        JSP1[login.jsp]
        JSP2[join.jsp]
        JSP3[main.jsp]
        JSP4[memberUpdate.jsp]
    end
    
    subgraph "컨트롤 계층"
        S1[LoginServlet]
        S2[JoinServlet]
        S3[UpdateServlet]
        S4[LogoutServlet]
        S5[IdCheckServlet]
    end
    
    subgraph "비즈니스 계층"
        DAO["MemberDAO<br/>싱글톤"]
        
        M1["userCheck()<br/>로그인 검증"]
        M2["getMember()<br/>회원 조회"]
        M3["insertMember()<br/>회원 등록"]
        M4["updateMember()<br/>회원 수정"]
        M5["confirmID()<br/>중복 확인"]
    end
    
    subgraph "데이터 전송 객체"
        VO["MemberVO<br/>name, userid, pwd,<br/>email, phone, admin"]
    end
    
    subgraph "데이터베이스"
        DB[("Oracle DB<br/>member 테이블")]
        
        Q1["SELECT pwd<br/>WHERE userid=?"]
        Q2["SELECT *<br/>WHERE userid=?"]
        Q3["INSERT INTO member<br/>VALUES ..."]
        Q4["UPDATE member SET ...<br/>WHERE userid=?"]
        Q5["SELECT userid<br/>WHERE userid=?"]
    end
    
    JSP1 --> S1
    JSP2 --> S2
    JSP3 --> S3
    JSP3 --> S4
    JSP2 --> S5
    JSP4 --> S3
    
    S1 --> M1
    S1 --> M2
    S2 --> M3
    S3 --> M2
    S3 --> M4
    S4 -.세션만 처리.-> S4
    S5 --> M5
    
    M1 --> DAO
    M2 --> DAO
    M3 --> DAO
    M4 --> DAO
    M5 --> DAO
    
    DAO -.생성/반환.-> VO
    
    M1 --> Q1
    M2 --> Q2
    M3 --> Q3
    M4 --> Q4
    M5 --> Q5
    
    Q1 --> DB
    Q2 --> DB
    Q3 --> DB
    Q4 --> DB
    Q5 --> DB
```

### 4.4 에러 처리 흐름

```mermaid
flowchart TD
    Start([사용자 요청])
    
    A[JSP에서 폼 제출]
    B{JavaScript 유효성 검증}
    C[alert 에러 메시지]
    D[Servlet으로 전송]
    
    E{로그인 필요한가?}
    F[세션 확인]
    G{세션 있나?}
    H[login.do로 리다이렉트]
    
    I[비즈니스 로직 실행]
    J[DAO 메소드 호출]
    
    K{DB 연결 성공?}
    L[try-catch로 예외 처리]
    M[result = -1 오류 코드]
    
    N[SQL 실행]
    O{SQL 성공?}
    P[result = 1 또는 데이터]
    Q[result = 0 또는 -1]
    
    R[Servlet에서 result 확인]
    S{result 값 체크}
    
    T["성공 페이지로 forward<br/>성공 메시지 포함"]
    U["실패 페이지로 forward<br/>에러 메시지 포함"]
    
    V[JSP에서 메시지 표시]
    
    End([사용자에게 결과 표시])
    
    Start --> A
    A --> B
    
    B -->|검증 실패| C
    C --> A
    B -->|검증 성공| D
    
    D --> E
    
    E -->|No| I
    E -->|Yes| F
    F --> G
    
    G -->|No| H
    H --> End
    G -->|Yes| I
    
    I --> J
    J --> K
    
    K -->|No 연결 실패| L
    L --> M
    M --> R
    
    K -->|Yes 연결 성공| N
    N --> O
    
    O -->|Yes| P
    O -->|No| Q
    
    P --> R
    Q --> R
    
    R --> S
    
    S -->|성공| T
    S -->|실패| U
    
    T --> V
    U --> V
    V --> End
```

---

## 5. 주요 메소드 호출 체인

### 5.1 로그인 메소드 호출 체인

```mermaid
graph LR
    A["user clicks<br/>로그인"]
    B["loginCheck()"]
    C["LoginServlet.doPost()"]
    D["MemberDAO.getInstance()"]
    E["MemberDAO.userCheck()"]
    F["getConnection()"]
    G["executeQuery()"]
    H["MemberDAO.getMember()"]
    I["session.setAttribute()"]
    J["forward to main.jsp"]
    
    A --> B
    B -->|return true| C
    C --> D
    D --> E
    E --> F
    F --> G
    G -->|result=1| C
    C --> H
    H --> C
    C --> I
    I --> J
```

### 5.2 회원가입 메소드 호출 체인

```mermaid
graph LR
    A["user clicks<br/>확인"]
    B["joinCheck()"]
    C["JoinServlet.doPost()"]
    D[reid 검증]
    E[MemberVO 생성]
    F["MemberDAO.getInstance()"]
    G["MemberDAO.insertMember()"]
    H["getConnection()"]
    I["executeUpdate()"]
    J["redirect to login.do"]
    
    A --> B
    B -->|return true| C
    C --> D
    D -->|pass| E
    E --> F
    F --> G
    G --> H
    H --> I
    I -->|result=1| C
    C --> J
```

### 5.3 정보수정 메소드 호출 체인

```mermaid
graph TB
    subgraph "GET 요청 (수정 화면)"
        A1["UpdateServlet.doGet()"]
        A2["session.getAttribute()"]
        A3["MemberDAO.getMember()"]
        A4["request.setAttribute()"]
        A5["forward<br/>memberUpdate.jsp"]
        
        A1 --> A2
        A2 --> A3
        A3 --> A4
        A4 --> A5
    end
    
    subgraph "POST 요청 (수정 처리)"
        B1["UpdateServlet.doPost()"]
        B2[MemberVO 생성]
        B3["MemberDAO.updateMember()"]
        B4["executeUpdate()"]
        B5["MemberDAO.getMember()<br/>최신 정보"]
        B6["session.setAttribute()<br/>세션 업데이트"]
        B7["forward main.jsp"]
        
        B1 --> B2
        B2 --> B3
        B3 --> B4
        B4 -->|success| B5
        B5 --> B6
        B6 --> B7
    end
    
    A5 -.사용자 수정.-> B1
```

---

## 6. 보안 체크포인트 정리

```mermaid
mindmap
    root((JSP 로그인 시스템 보안))
        입력 검증
            JavaScript 클라이언트 검증
                빈 값 체크
                길이 체크
                형식 체크
            서버 사이드 검증
                파라미터 null 체크
                데이터 타입 검증
                비즈니스 룰 검증
        SQL 인젝션 방지
            PreparedStatement 사용
            ? 파라미터 바인딩
            직접 문자열 연결 금지
        세션 보안
            로그인 체크
                세션 존재 확인
                세션 만료 체크
            세션 업데이트
                정보 수정 시
                최신 데이터 동기화
            세션 무효화
                로그아웃 시
                타임아웃 시
        권한 검증
            URL 파라미터 체크
                userid 조작 방지
                본인 정보만 접근
            아이디 중복 체크
                reid 필드 검증
                서버 재확인
        비밀번호 보안
            평문 저장 금지
                학습용 예외
            실전에서 암호화
                BCrypt 등
                단방향 해시
```

---

**끝! 🎉**

이 문서로 JSP 로그인 시스템의 전체 흐름을 완벽하게 이해할 수 있습니다.

