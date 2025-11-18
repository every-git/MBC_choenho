# JSP 상품 관리 시스템 흐름도 - Part 2

> 상품 수정과 삭제 프로세스 상세 도식화

---

## 📋 목차

1. [상품 수정 프로세스](#1-상품-수정-프로세스)
2. [상품 삭제 프로세스](#2-상품-삭제-프로세스)
3. [전체 시스템 통합 흐름도](#3-전체-시스템-통합-흐름도)
4. [POST-Redirect-GET 패턴](#4-post-redirect-get-패턴)

---

## 1. 상품 수정 프로세스

### 1.1 상품 수정 전체 시퀀스 다이어그램

```mermaid
sequenceDiagram
    autonumber
    participant User as "관리자"
    participant ListJSP as productList.jsp
    participant UpdateServlet as ProductUpdateServlet
    participant DAO as ProductDAO
    participant DB as MySQL DB
    participant UpdateJSP as productUpdate.jsp
    participant ProductJS as product.js
    participant FS as File System
    
    Note over User,FS: 상품 수정 프로세스
    
    User->>ListJSP: "상품 수정" 링크 클릭 (특정 상품)
    ListJSP->>UpdateServlet: " GET /productUpdate.do?code=6"
    
    activate UpdateServlet
    Note over UpdateServlet: doGet() 실행 - 수정 화면 표시
    
    UpdateServlet->>UpdateServlet: code = request.getParameter("code")
    UpdateServlet->>DAO: dao = ProductDAO.getInstance()
    UpdateServlet->>DAO: vo = dao.selectProductByCode(code)
    
    activate DAO
    DAO->>DAO: con = DBManager.getConnection()
    DAO->>DB: SELECT * FROM product WHERE code = ?
    activate DB
    DB-->>DAO: 해당 상품 데이터
    deactivate DB
    
    DAO->>DAO: ProductVO vo = new ProductVO()
    DAO->>DAO: vo에 데이터 설정
    DAO-->>UpdateServlet: ProductVO 반환
    deactivate DAO
    
    UpdateServlet->>UpdateServlet: request.setAttribute("product", vo)
    UpdateServlet->>UpdateJSP: forward("/product/productUpdate.jsp")
    deactivate UpdateServlet
    
    UpdateJSP->>UpdateJSP: 기존 상품 정보 표시 - 상품명: ${product.name} - 가격: ${product.price} - 이미지: ${product.pictureUrl} - 설명: ${product.description}
    UpdateJSP->>User: 수정 화면 표시
    
    User->>UpdateJSP: 정보 수정 예: 가격 변경, 설명 수정 이미지는 변경 안 함
    User->>UpdateJSP: "수정" 버튼 클릭
    
    UpdateJSP->>ProductJS: onclick="return productCheck()"
    activate ProductJS
    ProductJS->>ProductJS: 유효성 검증
    
    alt 검증 실패
        ProductJS->>User: alert(에러 메시지)
        ProductJS-->>UpdateJSP: return false
    else 검증 성공
        ProductJS-->>UpdateJSP: return true
        deactivate ProductJS
        
        UpdateJSP->>UpdateServlet: " POST /productUpdate.do enctype: multipart/form-data"
        
        activate UpdateServlet
        Note over UpdateServlet: doPost() 실행 - 수정 처리
        
        UpdateServlet->>UpdateServlet: request.setCharacterEncoding("utf-8")
        UpdateServlet->>UpdateServlet: contextPath 저장
        UpdateServlet->>UpdateServlet: path = context.getRealPath("upload")
        
        UpdateServlet->>FS: MultipartRequest multi 생성
        activate FS
        
        alt 새 이미지 업로드
            FS->>FS: 파일 저장
            FS-->>UpdateServlet: 새 파일명
        else 이미지 미변경
            FS-->>UpdateServlet: null
        end
        deactivate FS
        
        UpdateServlet->>UpdateServlet: code = parseInt(multi.getParameter("code"))
        UpdateServlet->>UpdateServlet: name = multi.getParameter("name")
        UpdateServlet->>UpdateServlet: price = parseInt(multi.getParameter("price"))
        UpdateServlet->>UpdateServlet: description = multi.getParameter("description")
        UpdateServlet->>UpdateServlet: pictureUrl = multi.getFilesystemName("pictureUrl")
        
        alt pictureUrl == null (이미지 미변경)
            UpdateServlet->>UpdateServlet: pictureUrl = multi.getParameter("nonmakeImg") (기존 이미지 유지)
        end
        
        UpdateServlet->>UpdateServlet: ProductVO vo = new ProductVO() vo에 모든 데이터 설정
        
        UpdateServlet->>DAO: dao.updateProduct(vo)
        activate DAO
        DAO->>DB: UPDATE product SET name=?, price=?, pictureurl=?, description=? WHERE code=?
        activate DB
        DB-->>DAO: 1 row updated
        deactivate DB
        DAO-->>UpdateServlet: void (완료)
        deactivate DAO
        
        UpdateServlet->>ListJSP: response.sendRedirect(contextPath + "/productList.do")
        deactivate UpdateServlet
        
        Note over ListJSP: 브라우저가 새로운 GET 요청
        ListJSP->>User: 상품 목록 화면 (수정된 정보 반영됨)
    end
```

### 1.2 ProductUpdateServlet 메소드 플로우차트

```mermaid
flowchart TD
    Start([사용자 요청])
    
    subgraph "doGet - 수정 화면 표시"
        A1["GET 요청 받음<br/>code 파라미터"]
        A2["code 값<br/>가져오기"]
        A3["DAO<br/>인스턴스"]
        A4["상품 정보<br/>조회"]
        A5["request에<br/>저장"]
        A6["forward<br/>productUpdate.jsp"]
    end
    
    subgraph "사용자 입력"
        B1[기존 정보 표시]
        B2[수정할 내용 입력]
        B3[이미지 변경<br/>선택사항]
        B4[수정 버튼 클릭]
        B5{"JavaScript<br/>검증"}
        B6[alert 메시지]
    end
    
    subgraph "doPost - 수정 처리"
        C1[POST 요청 받음]
        C2[인코딩 설정]
        C3[contextPath 저장]
        C4[upload 경로<br/>가져오기]
        C5[MultipartRequest<br/>생성]
        
        D1[code 값<br/>가져오기]
        D2[name 값<br/>가져오기]
        D3[price 값<br/>가져오기]
        D4[description 값<br/>가져오기]
        D5[pictureUrl<br/>파일명 가져오기]
        
        E1{pictureUrl<br/>null 인가?}
        E2[기존 이미지명<br/>사용]
        
        F1[ProductVO<br/>생성]
        F2[모든 값 설정]
        F3[DB 업데이트]
        F4[목록으로<br/>redirect]
    end
    
    End([목록 화면])
    
    Start --> A1
    A1 --> A2
    A2 --> A3
    A3 --> A4
    A4 --> A5
    A5 --> A6
    A6 --> B1
    
    B1 --> B2
    B2 --> B3
    B3 --> B4
    B4 --> B5
    
    B5 -->|검증 실패| B6
    B6 --> B2
    
    B5 -->|검증 성공| C1
    C1 --> C2
    C2 --> C3
    C3 --> C4
    C4 --> C5
    C5 --> D1
    
    D1 --> D2
    D2 --> D3
    D3 --> D4
    D4 --> D5
    D5 --> E1
    
    E1 -->|Yes<br/>이미지 미변경| E2
    E2 --> F1
    
    E1 -->|No<br/>새 이미지| F1
    F1 --> F2
    F2 --> F3
    F3 --> F4
    F4 --> End
```

### 1.3 ProductDAO.updateProduct() 메소드 상세

```mermaid
flowchart TD
    Start([updateProduct 시작<br/>매개변수: ProductVO vo])
    
    A1[try 블록 시작]
    A2["con = DBManager<br/>.getConnection()"]
    A3["SQL 준비<br/>UPDATE product SET<br/>name=?, price=?,<br/>pictureurl=?, description=?<br/>WHERE code=?"]
    A4["pstmt = con<br/>.prepareStatement(sql)"]
    A5["pstmt.setString<br/>(1, vo.getName())"]
    A6["pstmt.setInt<br/>(2, vo.getPrice())"]
    A7["pstmt.setString<br/>(3, vo.getPictureUrl())"]
    A8["pstmt.setString<br/>(4, vo.getDescription())"]
    A9["pstmt.setInt<br/>(5, vo.getCode())"]
    A10["pstmt.executeUpdate()"]
    
    B1[catch Exception]
    B2["e.printStackTrace()"]
    
    C1[finally 블록]
    C2["DBManager.close<br/>(con, pstmt)"]
    
    End([메소드 종료])
    
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
    A10 --> C1
    
    A1 -.오류.-> B1
    B1 --> B2
    B2 --> C1
    
    C1 --> C2
    C2 --> End
```

### 1.4 이미지 변경 처리 로직

```mermaid
graph TB
    subgraph "productUpdate.jsp 폼"
        A1["<input type='hidden' name='code' value='${product.code}'>"]
        A2["<input type='hidden' name='nonmakeImg' value='${product.pictureUrl}'>"]
        A3["기존 이미지 표시:<br/><img src='upload/${product.pictureUrl}'>"]
        A4["<input type='file' name='pictureUrl'>"]
        A5["주의사항: 이미지를 변경하고자 할때만 선택"]
    end
    
    subgraph "Servlet 처리 - doPost"
        B1["pictureUrl = multi<br/>.getFilesystemName('pictureUrl')"]
        B2{pictureUrl<br/>== null?}
        B3["새 파일명 사용<br/>사용자가 선택한 새 이미지"]
        B4["pictureUrl = nonmakeImg<br/>기존 이미지 유지"]
    end
    
    subgraph "결과"
        C1["DB에 새 파일명 저장<br/>+ 서버에 새 파일 존재"]
        C2["DB에 기존 파일명 유지<br/>+ 서버의 기존 파일 그대로"]
    end
    
    A1 --> B1
    A2 --> B1
    A4 --> B1
    
    B1 --> B2
    
    B2 -->|No<br/>사용자가 파일 선택| B3
    B2 -->|Yes<br/>파일 미선택| B4
    
    B3 --> C1
    B4 --> C2
    
    style A2 fill:#ffeb3b
    style B4 fill:#4caf50
```

---

## 2. 상품 삭제 프로세스

### 2.1 상품 삭제 전체 시퀀스 다이어그램

```mermaid
sequenceDiagram
    autonumber
    participant User as "관리자"
    participant ListJSP as productList.jsp
    participant DeleteServlet as ProductDeleteServlet
    participant DAO as ProductDAO
    participant DB as MySQL DB
    participant DeleteJSP as productDelete.jsp
    
    Note over User,DeleteJSP: 상품 삭제 프로세스
    
    User->>ListJSP: "상품 삭제" 링크 클릭
    ListJSP->>DeleteServlet: " GET /productDelete.do?code=6"
    
    activate DeleteServlet
    Note over DeleteServlet: doGet() 실행 - 삭제 확인 화면
    
    DeleteServlet->>DeleteServlet: code = request.getParameter("code")
    DeleteServlet->>DAO: dao = ProductDAO.getInstance()
    DeleteServlet->>DAO: vo = dao.selectProductByCode(code)
    
    activate DAO
    DAO->>DB: SELECT * FROM product WHERE code = ?
    activate DB
    DB-->>DAO: 해당 상품 데이터
    deactivate DB
    DAO-->>DeleteServlet: ProductVO 반환
    deactivate DAO
    
    DeleteServlet->>DeleteServlet: request.setAttribute("product", vo)
    DeleteServlet->>DeleteJSP: forward("/product/productDelete.jsp")
    deactivate DeleteServlet
    
    DeleteJSP->>DeleteJSP: 상품 정보 표시 (읽기 전용) - 이미지 표시 - 상품명, 가격, 설명
    DeleteJSP->>User: 삭제 확인 화면
    
    User->>DeleteJSP: "삭제" 버튼 클릭
    
    DeleteJSP->>DeleteJSP: onsubmit="return confirm('정말 삭제하시겠습니까?')"
    
    alt 사용자가 "취소" 선택
        DeleteJSP->>DeleteJSP: return false
        Note over DeleteJSP: 폼 제출 중단
    else 사용자가 "확인" 선택
        DeleteJSP->>DeleteServlet: " POST /productDelete.do code=6 (hidden)"
        
        activate DeleteServlet
        Note over DeleteServlet: doPost() 실행 - 삭제 처리
        
        DeleteServlet->>DeleteServlet: code = Integer.parseInt(request.getParameter("code"))
        DeleteServlet->>DAO: dao = ProductDAO.getInstance()
        DeleteServlet->>DAO: dao.deleteProduct(code)
        
        activate DAO
        DAO->>DAO: con = DBManager.getConnection()
        DAO->>DB: DELETE FROM product WHERE code = ?
        activate DB
        DB-->>DAO: 1 row deleted
        deactivate DB
        DAO-->>DeleteServlet: void (완료)
        deactivate DAO
        
        DeleteServlet->>ListJSP: response.sendRedirect(contextPath + "/productList.do")
        deactivate DeleteServlet
        
        Note over ListJSP: 브라우저가 새로운 GET 요청
        ListJSP->>User: 상품 목록 화면 (삭제된 상품 없어짐)
    end
```

### 2.2 ProductDeleteServlet 플로우차트

```mermaid
flowchart TD
    Start([사용자 요청])
    
    subgraph "doGet - 삭제 확인 화면"
        A1["GET 요청 받음<br/>code 파라미터"]
        A2["code = request<br/>.getParameter(code)"]
        A3["dao = ProductDAO<br/>.getInstance()"]
        A4["vo = dao<br/>.selectProductByCode(code)"]
        A5["request.setAttribute<br/>(product, vo)"]
        A6["forward<br/>/product/productDelete.jsp"]
    end
    
    subgraph "사용자 확인"
        B1[삭제 확인 화면 표시]
        B2[삭제 버튼 클릭]
        B3{"confirm<br/>정말 삭제?"}
        B4[제출 중단]
    end
    
    subgraph "doPost - 삭제 처리"
        C1[POST 요청 받음]
        C2["code = parseInt<br/>(request.getParameter(code))"]
        C3["dao = ProductDAO<br/>.getInstance()"]
        C4["dao.deleteProduct(code)"]
        C5["redirect<br/>/productList.do"]
    end
    
    End([목록 화면])
    
    Start --> A1
    A1 --> A2
    A2 --> A3
    A3 --> A4
    A4 --> A5
    A5 --> A6
    A6 --> B1
    
    B1 --> B2
    B2 --> B3
    
    B3 -->|취소| B4
    B4 --> End
    
    B3 -->|확인| C1
    C1 --> C2
    C2 --> C3
    C3 --> C4
    C4 --> C5
    C5 --> End
```

### 2.3 ProductDAO.deleteProduct() 메소드 상세

```mermaid
flowchart TD
    Start([deleteProduct 시작<br/>매개변수: int code])
    
    A1[try 블록 시작]
    A2["con = DBManager<br/>.getConnection()"]
    A3["SQL 준비<br/>DELETE FROM product<br/>WHERE code = ?"]
    A4["pstmt = con<br/>.prepareStatement(sql)"]
    A5["pstmt.setInt<br/>(1, code)"]
    A6["pstmt.executeUpdate()"]
    
    B1[catch Exception]
    B2["e.printStackTrace()"]
    
    C1[finally 블록]
    C2["DBManager.close<br/>(con, pstmt)"]
    
    End([메소드 종료])
    
    Start --> A1
    A1 --> A2
    A2 --> A3
    A3 --> A4
    A4 --> A5
    A5 --> A6
    A6 --> C1
    
    A1 -.오류.-> B1
    B1 --> B2
    B2 --> C1
    
    C1 --> C2
    C2 --> End
```

### 2.4 삭제 확인 (Confirm) 패턴

```mermaid
sequenceDiagram
    participant User as 사용자
    participant JSP as productDelete.jsp
    participant Browser as 브라우저
    participant Servlet as ProductDeleteServlet
    
    Note over User,Servlet: 삭제 확인 프로세스
    
    User->>JSP: 삭제 버튼 클릭
    JSP->>JSP: onsubmit 이벤트 발생
    JSP->>Browser: confirm("정말 삭제하시겠습니까?")
    
    activate Browser
    Note over Browser: 확인 대화상자 표시
    Browser->>User: 대화상자 표시
    
    alt 사용자가 "취소" 클릭
        User->>Browser: 취소
        Browser-->>JSP: return false
        Note over JSP: 폼 제출 중단
        JSP->>User: 화면 유지
    else 사용자가 "확인" 클릭
        User->>Browser: 확인
        Browser-->>JSP: return true
        Note over JSP: 폼 제출 진행
        JSP->>Servlet: POST /productDelete.do
        Servlet->>Servlet: 삭제 처리
        Servlet->>User: redirect /productList.do
    end
    deactivate Browser
```

---

## 3. 전체 시스템 통합 흐름도

### 3.1 모든 기능 통합 시퀀스

```mermaid
sequenceDiagram
    participant U as 관리자
    participant I as index.jsp
    participant LS as ProductListServlet
    participant L as productList.jsp
    participant WS as ProductWriteServlet
    participant W as productWrite.jsp
    participant US as ProductUpdateServlet
    participant UP as productUpdate.jsp
    participant DS as ProductDeleteServlet
    participant D as productDelete.jsp
    participant DAO as ProductDAO
    participant DB as Database
    participant FS as File System
    
    Note over U,FS: 1. 시스템 시작
    U->>I: 웹사이트 접속
    I->>LS: 목록 요청
    
    Note over U,FS: 2. 상품 목록 조회
    LS->>DAO: selectAllProducts()
    DAO->>DB: SELECT * FROM product
    DB-->>LS: 전체 상품 목록
    LS->>L: forward (productList)
    L->>U: 목록 화면 표시
    
    Note over U,FS: 3. 상품 등록
    U->>L: "상품 등록" 클릭
    L->>WS: GET /productWrite.do
    WS->>W: forward
    U->>W: 상품 정보 입력
    W->>WS: POST (파일 포함)
    WS->>FS: 파일 업로드
    WS->>DAO: insertProduct(vo)
    DAO->>DB: INSERT
    WS->>LS: redirect /productList.do
    
    Note over U,FS: 4. 상품 수정
    U->>L: "상품 수정" 클릭
    L->>US: GET /productUpdate.do?code=X
    US->>DAO: selectProductByCode(code)
    DAO->>DB: SELECT WHERE code=?
    US->>UP: forward (product)
    U->>UP: 정보 수정
    UP->>US: POST (파일 포함)
    US->>FS: 파일 업로드 (선택)
    US->>DAO: updateProduct(vo)
    DAO->>DB: UPDATE
    US->>LS: redirect /productList.do
    
    Note over U,FS: 5. 상품 삭제
    U->>L: "상품 삭제" 클릭
    L->>DS: GET /productDelete.do?code=X
    DS->>DAO: selectProductByCode(code)
    DAO->>DB: SELECT WHERE code=?
    DS->>D: forward (product)
    U->>D: "삭제" 확인
    D->>DS: POST
    DS->>DAO: deleteProduct(code)
    DAO->>DB: DELETE
    DS->>LS: redirect /productList.do
```

### 3.2 페이지 간 네비게이션 맵

```mermaid
graph TB
    Start([웹사이트 접속])
    
    Index[index.jsp]
    List[productList.jsp<br/>상품 목록]
    Write[productWrite.jsp<br/>상품 등록]
    Update[productUpdate.jsp<br/>상품 수정]
    Delete[productDelete.jsp<br/>상품 삭제]
    
    LS[ProductListServlet<br/>/productList.do]
    WS[ProductWriteServlet<br/>/productWrite.do]
    US[ProductUpdateServlet<br/>/productUpdate.do]
    DS[ProductDeleteServlet<br/>/productDelete.do]
    
    Start --> Index
    Index --> LS
    
    LS -->|forward| List
    
    List -->|등록 버튼| WS
    List -->|수정 링크| US
    List -->|삭제 링크| DS
    
    WS -->|GET| Write
    Write -->|POST 제출| WS
    WS -->|redirect| LS
    
    US -->|GET| Update
    Update -->|POST 제출| US
    US -->|redirect| LS
    
    DS -->|GET| Delete
    Delete -->|POST 제출| DS
    DS -->|redirect| LS
    
    Write -.목록 버튼.-> LS
    Update -.목록 버튼.-> LS
    Delete -.목록 버튼.-> LS
    
    style List fill:#c8e6c9
    style Write fill:#fff9c4
    style Update fill:#ffeb3b
    style Delete fill:#ffcdd2
```

### 3.3 데이터베이스 연동 전체 흐름

```mermaid
graph TB
    subgraph "프레젠테이션 계층"
        JSP1[productList.jsp]
        JSP2[productWrite.jsp]
        JSP3[productUpdate.jsp]
        JSP4[productDelete.jsp]
    end
    
    subgraph "컨트롤 계층"
        S1[ProductListServlet]
        S2[ProductWriteServlet]
        S3[ProductUpdateServlet]
        S4[ProductDeleteServlet]
    end
    
    subgraph "비즈니스 계층"
        DAO["ProductDAO<br/>싱글톤"]
        
        M1["selectAllProducts()<br/>전체 조회"]
        M2["insertProduct()<br/>상품 등록"]
        M3["selectProductByCode()<br/>특정 상품 조회"]
        M4["updateProduct()<br/>상품 수정"]
        M5["deleteProduct()<br/>상품 삭제"]
    end
    
    subgraph "데이터 전송 객체"
        VO["ProductVO<br/>code, name, price,<br/>pictureUrl, description"]
    end
    
    subgraph "데이터베이스"
        DB[("MySQL DB<br/>product 테이블")]
        
        Q1["SELECT *<br/>ORDER BY code DESC"]
        Q2["INSERT INTO product<br/>VALUES (...)"]
        Q3["SELECT *<br/>WHERE code=?"]
        Q4["UPDATE product SET ...<br/>WHERE code=?"]
        Q5["DELETE FROM product<br/>WHERE code=?"]
    end
    
    subgraph "유틸리티"
        DBM["DBManager<br/>getConnection()<br/>close()"]
    end
    
    JSP1 --> S1
    JSP2 --> S2
    JSP3 --> S3
    JSP4 --> S4
    
    S1 --> M1
    S2 --> M2
    S3 --> M3
    S3 --> M4
    S4 --> M3
    S4 --> M5
    
    M1 --> DAO
    M2 --> DAO
    M3 --> DAO
    M4 --> DAO
    M5 --> DAO
    
    DAO --> DBM
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

---

## 4. POST-Redirect-GET 패턴

### 4.1 PRG 패턴 개념

```mermaid
graph TB
    subgraph "PRG 패턴 사용 - 이 프로젝트"
        A1[사용자: 상품 등록]
        A2["1. POST<br/>/productWrite.do"]
        A3[Servlet: 처리]
        A4[DB: INSERT]
        A5["2. Redirect<br/>/productList.do"]
        A6["3. GET<br/>/productList.do"]
        A7[목록 조회]
        A8[화면 표시]
        
        A1 --> A2
        A2 --> A3
        A3 --> A4
        A4 --> A5
        A5 --> A6
        A6 --> A7
        A7 --> A8
        
        A9[F5 새로고침]
        A8 --> A9
        A9 -.안전.-> A6
    end
    
    subgraph "PRG 패턴 미사용"
        B1[사용자: 상품 등록]
        B2["1. POST<br/>/productWrite.do"]
        B3[Servlet: 처리]
        B4[DB: INSERT]
        B5["2. Forward<br/>productList.jsp"]
        B6[화면 표시]
        
        B1 --> B2
        B2 --> B3
        B3 --> B4
        B4 --> B5
        B5 --> B6
        
        B7[F5 새로고침]
        B6 --> B7
        B7 -.위험!.-> B2
        B2 -.중복 등록.-> B4
    end
    
    style A5 fill:#c8e6c9
    style B5 fill:#ffcdd2
```

### 4.2 PRG 패턴 장점

```mermaid
mindmap
    root((POST-Redirect-GET))
        중복 제출 방지
            F5 새로고침 안전
            뒤로가기 안전
            북마크 안전
        URL 정리
            POST URL 숨김
            GET URL 표시
            의미있는 주소
        사용자 경험
            혼란 방지
            일관된 동작
            브라우저 히스토리
        RESTful
            적절한 HTTP 메소드
            상태 코드 활용
            리소스 지향
```

### 4.3 이 프로젝트의 PRG 패턴 적용

```mermaid
sequenceDiagram
    participant U as 사용자
    participant B as 브라우저
    participant WS as ProductWriteServlet
    participant DAO as DAO
    participant DB as DB
    participant LS as ProductListServlet
    
    Note over U,LS: POST-Redirect-GET 패턴
    
    U->>B: 상품 등록 폼 제출
    B->>WS: POST /productWrite.do
    
    activate WS
    WS->>DAO: insertProduct(vo)
    DAO->>DB: INSERT
    DB-->>DAO: 성공
    DAO-->>WS: 완료
    
    WS->>B: 302 Redirect: /productList.do
    Note over WS,B: Location: /productList.do
    deactivate WS
    
    activate B
    Note over B: 브라우저가 자동으로 새 요청
    B->>LS: GET /productList.do
    deactivate B
    
    activate LS
    LS->>DAO: selectAllProducts()
    DAO->>DB: SELECT
    DB-->>LS: 목록
    LS->>B: 200 OK (HTML)
    deactivate LS
    
    B->>U: 목록 화면 표시
    
    Note over U,LS: F5 새로고침
    U->>B: F5 키 누름
    
    activate B
    Note over B: 마지막 GET 요청 반복 (안전!)
    B->>LS: GET /productList.do (다시)
    deactivate B
    
    LS->>B: 200 OK (최신 목록)
    B->>U: 목록 화면 (중복 등록 없음!)
```

### 4.4 forward vs redirect 비교

```mermaid
graph TB
    subgraph "forward - 서버 내부 이동"
        F1[클라이언트 요청]
        F2[Servlet A]
        F3[JSP B]
        F4[응답]
        
        F1 -->|1번 요청| F2
        F2 -.내부 전달.-> F3
        F3 -->|1번 응답| F4
        
        F5["특징:<br/>- 빠름 (서버 내부)<br/>- request 공유<br/>- URL 안 바뀜<br/>- 조회에 적합"]
    end
    
    subgraph "redirect - 클라이언트 재요청"
        R1[클라이언트 요청]
        R2[Servlet A]
        R3[302 응답]
        R4[클라이언트 재요청]
        R5[Servlet B]
        R6[응답]
        
        R1 -->|1번 요청| R2
        R2 -->|302 Location| R3
        R3 --> R4
        R4 -->|2번 요청| R5
        R5 -->|2번 응답| R6
        
        R7["특징:<br/>- 느림 (2번 요청)<br/>- request 새로 생성<br/>- URL 바뀜<br/>- POST 후 적합"]
    end
```

| 구분 | forward | redirect |
|------|---------|----------|
| **요청 횟수** | 1번 | 2번 |
| **속도** | ⚡ 빠름 | 🐢 느림 |
| **URL 변경** | ❌ 안 바뀜 | ✅ 바뀜 |
| **request 객체** | ✅ 공유됨 | ❌ 새로 생성 |
| **데이터 전달** | setAttribute | 파라미터/세션 |
| **사용 예** | 조회 → JSP | 등록/수정/삭제 → 목록 |
| **HTTP 상태** | 200 OK | 302 Found |

### 4.5 이 프로젝트의 사용 패턴

```mermaid
flowchart TD
    Start([요청 시작])
    
    A{HTTP<br/>메소드?}
    
    B[GET 요청]
    C[POST 요청]
    
    D{작업 유형?}
    
    E[조회 작업]
    F["등록/수정/삭제<br/>작업"]
    
    G["forward 사용<br/>JSP로 바로 전달"]
    H["redirect 사용<br/>목록으로 이동"]
    
    I["예:<br/>- ProductListServlet → productList.jsp<br/>- ProductUpdateServlet(GET) → productUpdate.jsp<br/>- ProductDeleteServlet(GET) → productDelete.jsp"]
    
    J["예:<br/>- ProductWriteServlet(POST) → productList.do<br/>- ProductUpdateServlet(POST) → productList.do<br/>- ProductDeleteServlet(POST) → productList.do"]
    
    Start --> A
    
    A -->|GET| B
    A -->|POST| C
    
    B --> E
    E --> G
    G --> I
    
    C --> D
    D --> F
    F --> H
    H --> J
    
    style G fill:#c8e6c9
    style H fill:#ffcdd2
```

---

## 5. 에러 처리 및 보안

### 5.1 에러 처리 흐름

```mermaid
flowchart TD
    Start([사용자 요청])
    
    A[JSP에서 폼 제출]
    B{JavaScript<br/>유효성 검증}
    C[alert 에러 메시지]
    D[Servlet으로 전송]
    
    E{파일 업로드<br/>필요?}
    F[MultipartRequest 생성]
    G{파일 크기<br/>20MB 이하?}
    H["IOException<br/>파일 크기 초과"]
    
    I[비즈니스 로직 실행]
    J[DAO 메소드 호출]
    
    K{DB 연결<br/>성공?}
    L[try-catch 예외 처리]
    M["e.printStackTrace()<br/>로그 출력"]
    
    N[SQL 실행]
    O{SQL<br/>성공?}
    P[정상 처리]
    Q[Exception 발생]
    
    R[finally 블록]
    S["DBManager.close()<br/>리소스 정리"]
    
    T{조회<br/>작업?}
    U["forward<br/>데이터 포함"]
    V["redirect<br/>목록으로"]
    
    End([결과 표시])
    
    Start --> A
    A --> B
    
    B -->|실패| C
    C --> A
    B -->|성공| D
    
    D --> E
    E -->|Yes| F
    F --> G
    
    G -->|No| H
    H --> End
    
    G -->|Yes| I
    E -->|No| I
    
    I --> J
    J --> K
    
    K -->|No| L
    L --> M
    M --> R
    
    K -->|Yes| N
    N --> O
    
    O -->|No| Q
    Q --> R
    
    O -->|Yes| P
    P --> R
    
    R --> S
    S --> T
    
    T -->|Yes| U
    T -->|No| V
    
    U --> End
    V --> End
```

### 5.2 보안 체크포인트

```mermaid
mindmap
    root((보안))
        입력 검증
            JavaScript 클라이언트
                빈 값 체크
                타입 체크
                길이 제한
            서버 사이드
                null 체크
                타입 변환
                범위 검증
        SQL 인젝션 방지
            PreparedStatement
                ? 파라미터 바인딩
                자동 이스케이프
            문자열 연결 금지
                동적 쿼리 위험
        파일 업로드 보안
            크기 제한
                20MB 제한
            타입 검증
                accept 속성
                확장자 체크
            저장 경로
                서버 내부 경로
                직접 접근 불가
        직접 접근 방지
            JSP 체크
                Servlet 거쳤는지 확인
            리다이렉트
                비정상 접근 시
```

---

## 6. 주요 메소드 호출 체인

### 6.1 상품 등록 메소드 체인

```mermaid
graph LR
    A["사용자:<br/>등록 버튼 클릭"]
    B["productCheck()"]
    C["ProductWriteServlet<br/>.doPost()"]
    D["MultipartRequest<br/>생성"]
    E["파일 시스템<br/>저장"]
    F["ProductDAO<br/>.getInstance()"]
    G["ProductDAO<br/>.insertProduct()"]
    H["DBManager<br/>.getConnection()"]
    I["pstmt<br/>.executeUpdate()"]
    J["response<br/>.sendRedirect()"]
    
    A --> B
    B -->|return true| C
    C --> D
    D --> E
    E --> F
    F --> G
    G --> H
    H --> I
    I -->|완료| C
    C --> J
```

### 6.2 상품 수정 메소드 체인

```mermaid
graph TB
    subgraph "GET 요청 - 수정 화면"
        A1["ProductUpdateServlet<br/>.doGet()"]
        A2["ProductDAO<br/>.selectProductByCode()"]
        A3["DBManager<br/>.getConnection()"]
        A4["pstmt<br/>.executeQuery()"]
        A5["ProductVO<br/>생성 및 반환"]
        A6["request<br/>.setAttribute()"]
        A7["forward<br/>productUpdate.jsp"]
        
        A1 --> A2
        A2 --> A3
        A3 --> A4
        A4 --> A5
        A5 --> A6
        A6 --> A7
    end
    
    subgraph "POST 요청 - 수정 처리"
        B1["ProductUpdateServlet<br/>.doPost()"]
        B2["MultipartRequest<br/>생성"]
        B3["파일 업로드<br/>(선택)"]
        B4["ProductVO<br/>생성"]
        B5["ProductDAO<br/>.updateProduct()"]
        B6["pstmt<br/>.executeUpdate()"]
        B7["response<br/>.sendRedirect()"]
        
        B1 --> B2
        B2 --> B3
        B3 --> B4
        B4 --> B5
        B5 --> B6
        B6 --> B7
    end
    
    A7 -.사용자 수정.-> B1
```

### 6.3 상품 삭제 메소드 체인

```mermaid
graph TB
    subgraph "GET 요청 - 확인 화면"
        A1["ProductDeleteServlet<br/>.doGet()"]
        A2["ProductDAO<br/>.selectProductByCode()"]
        A3["forward<br/>productDelete.jsp"]
        
        A1 --> A2
        A2 --> A3
    end
    
    subgraph "POST 요청 - 삭제 처리"
        B1["ProductDeleteServlet<br/>.doPost()"]
        B2["Integer.parseInt<br/>(code)"]
        B3["ProductDAO<br/>.deleteProduct()"]
        B4["DBManager<br/>.getConnection()"]
        B5["pstmt<br/>.executeUpdate()"]
        B6["response<br/>.sendRedirect()"]
        
        B1 --> B2
        B2 --> B3
        B3 --> B4
        B4 --> B5
        B5 --> B6
    end
    
    A3 -.사용자 확인.-> B1
```

---

**끝! 🎉**

이 문서로 JSP 상품 관리 시스템의 전체 흐름을 완벽하게 이해할 수 있습니다.

**핵심 포인트:**
- ✅ CRUD 4가지 작업의 완전한 구현
- ✅ 파일 업로드 (MultipartRequest) 활용
- ✅ POST-Redirect-GET 패턴 적용
- ✅ MVC 패턴과 싱글톤 패턴
- ✅ PreparedStatement를 통한 안전한 DB 접근
- ✅ 적절한 forward와 redirect 사용

