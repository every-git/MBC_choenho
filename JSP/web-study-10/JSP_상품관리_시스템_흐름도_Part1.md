# JSP 상품 관리 시스템 흐름도 - Part 1

> 전체 시스템의 구조와 상품 조회/등록 프로세스 상세 도식화

---

## 📋 목차

1. [전체 시스템 아키텍처](#1-전체-시스템-아키텍처)
2. [상품 목록 조회 프로세스](#2-상품-목록-조회-프로세스)
3. [상품 등록 프로세스](#3-상품-등록-프로세스)
4. [파일 업로드 메커니즘](#4-파일-업로드-메커니즘)

---

## 1. 전체 시스템 아키텍처

### 1.1 MVC 패턴 구조

```mermaid
graph TB
    subgraph "브라우저 Client"
        A[관리자]
    end
    
    subgraph "View Layer - JSP"
        B1[index.jsp]
        B2[productList.jsp]
        B3[productWrite.jsp]
        B4[productUpdate.jsp]
        B5[productDelete.jsp]
    end
    
    subgraph "Controller Layer - Servlet"
        C1[ProductListServlet]
        C2[ProductWriteServlet]
        C3[ProductUpdateServlet]
        C4[ProductDeleteServlet]
    end
    
    subgraph "Model Layer - DAO/VO"
        D1[ProductDAO]
        D2[ProductVO]
        D3[DBManager]
    end
    
    subgraph "Data Layer"
        E[("MySQL Database (product 테이블)")]
    end
    
    subgraph "File System"
        F["Upload 폴더<br/>이미지 저장"]
    end
    
    A -->|HTTP Request| B1
    A -->|HTTP Request| B2
    B1 -->|목록 요청| C1
    B2 -->|등록| C2
    B2 -->|수정| C3
    B2 -->|삭제| C4
    B3 -->|등록 제출| C2
    B4 -->|수정 제출| C3
    B5 -->|삭제 확인| C4
    
    C1 --> D1
    C2 --> D1
    C3 --> D1
    C4 --> D1
    
    D1 --> D3
    D3 --> E
    D1 -.ProductVO 객체.-> D2
    
    C2 -.파일 저장.-> F
    C3 -.파일 저장.-> F
    
    C1 -->|forward| B2
    C2 -->|redirect| C1
    C3 -->|redirect| C1
    C4 -->|redirect| C1
```

### 1.2 파일 구조와 역할

```mermaid
graph LR
    subgraph "Java 파일 - 로직"
        A1["ProductVO.java<br/>상품 데이터 객체"]
        A2["ProductDAO.java<br/>DB 접근 전문가"]
        A3["DBManager.java<br/>DB 연결 관리"]
        A4["ProductListServlet.java<br/>목록 조회 처리"]
        A5["ProductWriteServlet.java<br/>상품 등록 처리"]
        A6["ProductUpdateServlet.java<br/>상품 수정 처리"]
        A7["ProductDeleteServlet.java<br/>상품 삭제 처리"]
    end
    
    subgraph "JSP 파일 - 화면"
        B1["index.jsp<br/>메인 화면"]
        B2["productList.jsp<br/>상품 목록 화면"]
        B3["productWrite.jsp<br/>상품 등록 화면"]
        B4["productUpdate.jsp<br/>상품 수정 화면"]
        B5["productDelete.jsp<br/>삭제 확인 화면"]
    end
    
    subgraph "JavaScript 파일"
        C1["product.js<br/>유효성 검증"]
    end
    
    A4 -.사용.-> A2
    A5 -.사용.-> A2
    A6 -.사용.-> A2
    A7 -.사용.-> A2
    
    A2 -.사용.-> A3
    A2 -.생성/반환.-> A1
    
    A4 -.forward.-> B2
    A5 -.redirect.-> A4
    A6 -.redirect.-> A4
    A7 -.redirect.-> A4
    
    B3 -.호출.-> C1
    B4 -.호출.-> C1
```

### 1.3 데이터 흐름 개요

```mermaid
sequenceDiagram
    participant U as 관리자
    participant B as 브라우저
    participant JSP as JSP 페이지
    participant JS as JavaScript
    participant S as Servlet
    participant DAO as ProductDAO
    participant DB as Database
    participant FS as File System
    
    Note over U,FS: 전체 데이터 흐름
    
    U->>B: 페이지 요청
    B->>JSP: HTTP Request
    JSP->>U: 화면 표시
    
    U->>JSP: 데이터 입력 & 제출
    JSP->>JS: 유효성 검증
    
    alt 검증 실패
        JS->>U: alert 메시지
    else 검증 성공
        JS->>S: POST 요청
        
        alt 파일 업로드 있음
            S->>FS: 파일 저장
            FS->>S: 저장된 파일명
        end
        
        S->>DAO: 비즈니스 로직 요청
        DAO->>DB: SQL 실행
        DB->>DAO: 결과 반환
        DAO->>S: 처리 결과
        
        alt 조회 (GET)
            S->>JSP: forward (데이터 전달)
        else 등록/수정/삭제 (POST)
            S->>B: redirect (새 요청)
            B->>S: productList.do
            S->>DAO: 목록 조회
            DAO->>DB: SELECT
            DB->>DAO: 목록 데이터
            S->>JSP: forward
        end
        
        JSP->>U: 결과 화면
    end
```

---

## 2. 상품 목록 조회 프로세스

### 2.1 상품 목록 조회 전체 시퀀스 다이어그램

```mermaid
sequenceDiagram
    autonumber
    participant User as "관리자"
    participant Browser as 브라우저
    participant IndexJSP as index.jsp
    participant ListServlet as ProductListServlet
    participant DAO as ProductDAO
    participant DB as MySQL DB
    participant ListJSP as productList.jsp
    
    Note over User,ListJSP: 상품 목록 조회 프로세스
    
    User->>Browser: 웹사이트 접속
    Browser->>IndexJSP: HTTP Request
    IndexJSP->>Browser: 메인 화면 표시
    
    User->>IndexJSP: "상품 목록 보기" 클릭
    IndexJSP->>ListServlet: GET /productList.do
    
    activate ListServlet
    Note over ListServlet: doGet() 실행
    
    ListServlet->>DAO: dao = ProductDAO.getInstance()
    activate DAO
    Note over DAO: 싱글톤 인스턴스 반환
    DAO-->>ListServlet: ProductDAO 객체
    deactivate DAO
    
    ListServlet->>DAO: list = dao.selectAllProducts()
    activate DAO
    Note over DAO: DB에서 전체 상품 조회
    
    DAO->>DAO: con = DBManager.getConnection()
    DAO->>DB: SELECT * FROM product ORDER BY code DESC
    activate DB
    DB-->>DAO: ResultSet (전체 상품 데이터)
    deactivate DB
    
    DAO->>DAO: List<ProductVO> list = new ArrayList<>()
    
    loop 각 행(row)마다
        DAO->>DAO: ProductVO vo = new ProductVO()
        DAO->>DAO: vo.setCode(rs.getInt("code"))
        DAO->>DAO: vo.setName(rs.getString("name"))
        DAO->>DAO: vo.setPrice(rs.getInt("price"))
        DAO->>DAO: vo.setPictureUrl(rs.getString("pictureurl"))
        DAO->>DAO: vo.setDescription(rs.getString("description"))
        DAO->>DAO: list.add(vo)
    end
    
    DAO-->>ListServlet: List<ProductVO> 반환
    deactivate DAO
    
    ListServlet->>ListServlet: request.setAttribute("productList", list)
    ListServlet->>ListJSP: forward("/product/productList.jsp")
    deactivate ListServlet
    
    activate ListJSP
    ListJSP->>ListJSP: 직접 접근 체크 (productList == null?)
    
    alt 직접 접근 (Servlet 거치지 않음)
        ListJSP->>Browser: redirect /productList.do
        Note over ListJSP: 보안: Servlet을 반드시 거치도록
    else 정상 접근
        ListJSP->>ListJSP: <c:forEach var="product" items="${productList}">
        
        loop 각 상품마다
            ListJSP->>ListJSP: 상품 번호, 이름, 가격 표시
            ListJSP->>ListJSP: 수정/삭제 링크 생성
        end
        
        ListJSP->>Browser: HTML 렌더링
    end
    deactivate ListJSP
    
    Browser->>User: 상품 목록 화면 표시
```

### 2.2 ProductListServlet 메소드 플로우차트

```mermaid
flowchart TD
    Start([사용자가 productList.do 요청])
    
    subgraph "doGet 메소드"
        A1[요청 받음]
        A2["DAO<br/>인스턴스 가져오기"]
        A3["전체 상품<br/>조회"]
        A4["request에<br/>저장"]
        A5["RequestDispatcher<br/>생성"]
        A6["forward<br/>productList.jsp"]
    end
    
    B1[productList.jsp 렌더링]
    
    subgraph "productList.jsp"
        C1{productList<br/>== null?}
        C2["redirect<br/>/productList.do"]
        C3["상품 목록 테이블 생성"]
        C4["forEach로 상품 반복"]
        C5["상품 정보 표시"]
        C6["수정/삭제 링크"]
    end
    
    End([화면 표시])
    
    Start --> A1
    A1 --> A2
    A2 --> A3
    A3 --> A4
    A4 --> A5
    A5 --> A6
    A6 --> B1
    B1 --> C1
    
    C1 -->|Yes<br/>직접 접근| C2
    C2 --> End
    
    C1 -->|No<br/>정상 접근| C3
    C3 --> C4
    C4 --> C5
    C5 --> C6
    C6 --> End
```

### 2.3 ProductDAO.selectAllProducts() 메소드 상세

```mermaid
flowchart TD
    Start([selectAllProducts 시작])
    
    A1["List<ProductVO> list<br/>= new ArrayList<>()"]
    A2[try 블록 시작]
    A3["con = DBManager<br/>.getConnection()"]
    A4["SQL 쿼리 준비<br/>SELECT * FROM product<br/>ORDER BY code DESC"]
    A5["pstmt = con<br/>.prepareStatement(sql)"]
    A6["rs = pstmt<br/>.executeQuery()"]
    
    B1{"rs.next()<br/>다음 데이터?"}
    B2["vo = new ProductVO()"]
    B3["vo.setCode<br/>(rs.getInt(code))"]
    B4["vo.setName<br/>(rs.getString(name))"]
    B5["vo.setPrice<br/>(rs.getInt(price))"]
    B6["vo.setPictureUrl<br/>(rs.getString(pictureurl))"]
    B7["vo.setDescription<br/>(rs.getString(description))"]
    B8["list.add(vo)"]
    
    C1[catch Exception]
    C2["e.printStackTrace()"]
    
    D1[finally 블록]
    D2["DBManager.close<br/>(con, pstmt, rs)"]
    
    End([return list])
    
    Start --> A1
    A1 --> A2
    A2 --> A3
    A3 --> A4
    A4 --> A5
    A5 --> A6
    A6 --> B1
    
    B1 -->|Yes| B2
    B2 --> B3
    B3 --> B4
    B4 --> B5
    B5 --> B6
    B6 --> B7
    B7 --> B8
    B8 --> B1
    
    B1 -->|No<br/>더 이상 없음| D1
    
    A2 -.오류 발생.-> C1
    C1 --> C2
    C2 --> D1
    
    D1 --> D2
    D2 --> End
```

### 2.4 productList.jsp 화면 구성

```mermaid
graph TB
    subgraph "productList.jsp 구조"
        A[제목: 상품 리스트 - 관리자 페이지]
        B["상품 등록 버튼<br/>우측 상단"]
        C[상품 목록 테이블]
        
        subgraph "테이블 헤더"
            D1[번호]
            D2[이름]
            D3[가격]
            D4[수정]
            D5[삭제]
        end
        
        subgraph "테이블 바디 - forEach 반복"
            E1["${product.code}"]
            E2["${product.name}"]
            E3["${product.price} 원"]
            E4["상품 수정 링크<br/>code 파라미터"]
            E5["상품 삭제 링크<br/>code 파라미터"]
        end
    end
    
    A --> B
    A --> C
    C --> D1
    C --> D2
    C --> D3
    C --> D4
    C --> D5
    D1 --> E1
    D2 --> E2
    D3 --> E3
    D4 --> E4
    D5 --> E5
    
    B -.클릭.-> F["/productWrite.do"]
    E4 -.클릭.-> G["/productUpdate.do?code=XXX"]
    E5 -.클릭.-> H["/productDelete.do?code=XXX"]
```

---

## 3. 상품 등록 프로세스

### 3.1 상품 등록 전체 시퀀스 다이어그램

```mermaid
sequenceDiagram
    autonumber
    participant User as "관리자"
    participant Browser as 브라우저
    participant ListJSP as productList.jsp
    participant WriteServlet as ProductWriteServlet
    participant WriteJSP as productWrite.jsp
    participant ProductJS as product.js
    participant DAO as ProductDAO
    participant DB as MySQL DB
    participant FS as File System
    
    Note over User,FS: 상품 등록 프로세스
    
    User->>ListJSP: "상품 등록" 버튼 클릭
    ListJSP->>WriteServlet: GET /productWrite.do
    
    activate WriteServlet
    Note over WriteServlet: doGet() 실행
    WriteServlet->>WriteJSP: forward("/product/productWrite.jsp")
    deactivate WriteServlet
    
    WriteJSP->>Browser: 등록 화면 렌더링
    Browser->>User: 등록 폼 표시
    
    User->>WriteJSP: 상품 정보 입력 상품명: "노트북" 가격: 1500000 이미지 파일 선택 설명: "고성능 노트북"
    User->>WriteJSP: "등록" 버튼 클릭
    
    WriteJSP->>ProductJS: onclick="return productCheck()"
    activate ProductJS
    
    ProductJS->>ProductJS: document.frm.name.value 체크
    
    alt 상품명 비어있음
        ProductJS->>User: alert("상품명을 써주세요")
        ProductJS-->>WriteJSP: return false
    end
    
    ProductJS->>ProductJS: document.frm.price.value 체크
    
    alt 가격 비어있음
        ProductJS->>User: alert("가격을 써주세요")
        ProductJS-->>WriteJSP: return false
    end
    
    ProductJS->>ProductJS: isNaN(price) 체크
    
    alt 가격이 숫자 아님
        ProductJS->>User: alert("숫자를 입력해야 합니다")
        ProductJS-->>WriteJSP: return false
    else 모든 검증 통과
        ProductJS-->>WriteJSP: return true
        deactivate ProductJS
        
        WriteJSP->>WriteServlet: " POST /productWrite.do enctype: multipart/form-data"
        
        activate WriteServlet
        Note over WriteServlet: doPost() 실행
        
        WriteServlet->>WriteServlet: request.setCharacterEncoding("utf-8")
        WriteServlet->>WriteServlet: ServletContext context = getServletContext()
        WriteServlet->>WriteServlet: contextPath = request.getContextPath()
        WriteServlet->>WriteServlet: path = context.getRealPath("upload")
        
        Note over WriteServlet: MultipartRequest 생성 (파일 업로드)
        WriteServlet->>FS: MultipartRequest multi = new MultipartRequest(...)
        activate FS
        FS->>FS: 파일 저장 (최대 20MB)
        FS->>FS: DefaultFileRenamePolicy 적용 (중복 시 파일명 변경)
        FS-->>WriteServlet: 업로드 완료
        deactivate FS
        
        WriteServlet->>WriteServlet: name = multi.getParameter("name")
        WriteServlet->>WriteServlet: price = Integer.parseInt(multi.getParameter("price"))
        WriteServlet->>WriteServlet: pictureUrl = multi.getFilesystemName("pictureUrl")
        WriteServlet->>WriteServlet: description = multi.getParameter("description")
        
        WriteServlet->>WriteServlet: ProductVO vo = new ProductVO()
        WriteServlet->>WriteServlet: vo.setName(name) vo.setPrice(price) vo.setPictureUrl(pictureUrl) vo.setDescription(description)
        
        WriteServlet->>DAO: dao = ProductDAO.getInstance()
        WriteServlet->>DAO: dao.insertProduct(vo)
        
        activate DAO
        DAO->>DAO: con = DBManager.getConnection()
        DAO->>DB: INSERT INTO product(name, price, pictureurl, description) VALUES(?, ?, ?, ?)
        activate DB
        DB-->>DAO: 1 row inserted
        deactivate DB
        DAO-->>WriteServlet: void (완료)
        deactivate DAO
        
        WriteServlet->>Browser: response.sendRedirect(contextPath + "/productList.do")
        deactivate WriteServlet
        
        Note over Browser: 브라우저가 새로운 GET 요청
        Browser->>ListServlet: GET /productList.do
        ListServlet->>DAO: 전체 상품 조회
        DAO->>DB: SELECT
        DB->>DAO: 최신 목록 (새 상품 포함)
        ListServlet->>ListJSP: forward
        
        ListJSP->>User: 상품 목록 화면 (새 상품 추가됨)
    end
```

### 3.2 ProductWriteServlet 메소드 플로우차트

```mermaid
flowchart TD
    Start([사용자 요청])
    
    subgraph "doGet - 등록 화면 표시"
        A1[GET 요청 받음]
        A2["forward<br/>/product/productWrite.jsp"]
    end
    
    subgraph "사용자 입력"
        B1[상품 정보 입력]
        B2[이미지 파일 선택]
        B3[등록 버튼 클릭]
        B4{"JavaScript<br/>검증 함수"}
        B5[alert 메시지]
    end
    
    subgraph "doPost - 등록 처리"
        C1[POST 요청 받음]
        C2["request 인코딩 설정<br/>utf-8"]
        C3["contextPath 저장"]
        C4["path = context<br/>.getRealPath(upload)"]
        C5["MultipartRequest<br/>multi 생성"]
        C6["파일 시스템에 업로드<br/>최대 20MB"]
        
        D1["name = multi<br/>.getParameter(name)"]
        D2["price = parseInt<br/>(multi.getParameter(price))"]
        D3["pictureUrl = multi<br/>.getFilesystemName(pictureUrl)"]
        D4["description = multi<br/>.getParameter(description)"]
        
        E1["ProductVO vo =<br/>new ProductVO()"]
        E2[vo에 모든 값 설정]
        E3["dao = ProductDAO<br/>.getInstance()"]
        E4["dao.insertProduct(vo)"]
        
        F1["response.sendRedirect<br/>(contextPath + /productList.do)"]
    end
    
    End([목록 화면으로 이동])
    
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
    C6 --> D1
    
    D1 --> D2
    D2 --> D3
    D3 --> D4
    D4 --> E1
    E1 --> E2
    E2 --> E3
    E3 --> E4
    E4 --> F1
    F1 --> End
```

### 3.3 ProductDAO.insertProduct() 메소드 상세

```mermaid
flowchart TD
    Start([insertProduct 시작<br/>매개변수: ProductVO vo])
    
    A1[try 블록 시작]
    A2["con = DBManager<br/>.getConnection()"]
    A3["SQL 준비<br/>INSERT INTO product<br/>(name, price, pictureurl, description)<br/>VALUES(?, ?, ?, ?)"]
    A4["pstmt = con<br/>.prepareStatement(sql)"]
    A5["pstmt.setString<br/>(1, vo.getName())"]
    A6["pstmt.setInt<br/>(2, vo.getPrice())"]
    A7["pstmt.setString<br/>(3, vo.getPictureUrl())"]
    A8["pstmt.setString<br/>(4, vo.getDescription())"]
    A9["pstmt.executeUpdate()"]
    
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
    A9 --> C1
    
    A1 -.오류.-> B1
    B1 --> B2
    B2 --> C1
    
    C1 --> C2
    C2 --> End
```

---

## 4. 파일 업로드 메커니즘

### 4.1 MultipartRequest 동작 원리

```mermaid
sequenceDiagram
    autonumber
    participant Form as HTML Form
    participant Browser as 브라우저
    participant Servlet as Servlet
    participant Multi as MultipartRequest
    participant FS as File System
    
    Note over Form,FS: 파일 업로드 전체 프로세스
    
    Form->>Form: enctype="multipart/form-data" 설정
    Form->>Browser: 폼 제출 (파일 포함)
    
    Browser->>Servlet: POST 요청 (multipart 데이터)
    
    activate Servlet
    Servlet->>Servlet: ServletContext context = getServletContext()
    Servlet->>Servlet: path = context.getRealPath("upload")
    
    Note over Servlet: /upload 폴더의 실제 경로 예: /var/tomcat/webapps/app/upload
    
    Servlet->>Multi: new MultipartRequest(request, path, 20MB, "utf-8", policy)
    
    activate Multi
    Note over Multi: 요청 파싱 시작
    
    Multi->>Multi: 요청 헤더 분석
    Multi->>Multi: boundary 추출
    Multi->>Multi: 각 part 분리
    
    loop 각 파트마다
        Multi->>Multi: Content-Disposition 헤더 읽기
        
        alt 일반 파라미터
            Multi->>Multi: 메모리에 값 저장
        else 파일 파라미터
            Multi->>Multi: 파일명 추출
            Multi->>Multi: Content-Type 확인
            
            Multi->>FS: 파일 저장 (path + 파일명)
            activate FS
            
            alt 파일명 중복
                FS->>FS: DefaultFileRenamePolicy 적용 예: image.jpg -> image1.jpg
            end
            
            FS-->>Multi: 저장 완료 (실제 파일명)
            deactivate FS
        end
    end
    
    Multi-->>Servlet: MultipartRequest 객체 생성 완료
    deactivate Multi
    
    Servlet->>Multi: multi.getParameter("name") - 일반 파라미터
    Multi-->>Servlet: "노트북"
    
    Servlet->>Multi: multi.getFilesystemName("pictureUrl") - 파일명
    Multi-->>Servlet: "laptop.jpg" (또는 "laptop1.jpg")
    
    Servlet->>Servlet: ProductVO 생성 및 데이터 설정
    Servlet->>Servlet: DAO 호출하여 DB 저장
    
    deactivate Servlet
```

### 4.2 파일 업로드 설정 상세

```mermaid
graph TB
    subgraph "MultipartRequest 생성자 파라미터"
        A["1. request<br/>(HttpServletRequest)"]
        B["2. path<br/>(저장 경로)"]
        C["3. sizeLimit<br/>(최대 크기)"]
        D["4. encType<br/>(인코딩)"]
        E["5. policy<br/>(파일명 정책)"]
    end
    
    subgraph "실제 설정 값"
        A1["request 객체"]
        B1["context.getRealPath('upload')<br/>예: /webapp/upload"]
        C1["20*1024*1024<br/>(20MB)"]
        D1["utf-8"]
        E1["DefaultFileRenamePolicy<br/>(중복 시 번호 추가)"]
    end
    
    A --> A1
    B --> B1
    C --> C1
    D --> D1
    E --> E1
    
    B1 -.저장 위치.-> F["서버의 실제 디렉토리<br/>(톰캣 webapps 내부)"]
    C1 -.초과 시.-> G["IOException 발생<br/>업로드 실패"]
    E1 -.예시.-> H["laptop.jpg<br/>→ laptop1.jpg<br/>→ laptop2.jpg"]
```

### 4.3 파일 업로드 폼 구조

```mermaid
graph TB
    subgraph "HTML Form 필수 속성"
        A[method='post']
        B["enctype='multipart/form-data'"]
        C["action='productWrite.do'"]
    end
    
    subgraph "입력 필드"
        D["<input type='text' name='name'>"]
        E["<input type='text' name='price'>"]
        F["<input type='file' name='pictureUrl' accept='image/*'>"]
        G["<textarea name='description'>"]
    end
    
    subgraph "버튼"
        H["<input type='submit' onclick='return productCheck()'>"]
        I["<input type='reset'>"]
        J["<input type='button' value='목록'>"]
    end
    
    A --> K[Form 태그]
    B --> K
    C --> K
    
    K --> D
    K --> E
    K --> F
    K --> G
    K --> H
    K --> I
    K --> J
    
    F -.accept 속성.-> L["이미지 파일만 선택 가능<br/>(브라우저 필터)"]
    H -.클릭 시.-> M["productCheck() 실행<br/>유효성 검증"]
```

### 4.4 파일 저장 경로 이해

```mermaid
graph LR
    subgraph "개발 환경"
        A1[프로젝트 폴더]
        A2[src/main/webapp/upload]
        A3[이미지 파일들]
    end
    
    subgraph "실행 환경 - 톰캣"
        B1[톰캣 webapps 폴더]
        B2[프로젝트명/upload]
        B3[업로드된 파일들]
    end
    
    subgraph "코드에서 경로 가져오기"
        C1["ServletContext context<br/>= getServletContext()"]
        C2["String path<br/>= context.getRealPath('upload')"]
        C3["실제 경로 반환<br/>예: /var/tomcat/webapps/myapp/upload"]
    end
    
    A1 --> A2
    A2 --> A3
    
    B1 --> B2
    B2 --> B3
    
    C1 --> C2
    C2 --> C3
    C3 -.가리킴.-> B2
    
    A2 -.빌드/배포.-> B2
```

### 4.5 파일 업로드 에러 처리

```mermaid
flowchart TD
    Start([파일 업로드 시작])
    
    A[폼 제출]
    B{enctype 올바른가?}
    C[업로드 실패<br/>일반 request로 처리됨]
    
    D[MultipartRequest 생성 시도]
    E{파일 크기<br/>20MB 이하?}
    F["IOException 발생<br/>파일 크기 초과"]
    
    G{upload 폴더<br/>존재?}
    H["IOException 발생<br/>디렉토리 없음"]
    
    I{파일 타입<br/>적절?}
    J[경고 (선택사항)]
    
    K[파일 저장 성공]
    L[파일명 반환]
    
    End([업로드 완료])
    
    Start --> A
    A --> B
    
    B -->|No| C
    C --> End
    
    B -->|Yes| D
    D --> E
    
    E -->|No| F
    F --> End
    
    E -->|Yes| G
    
    G -->|No| H
    H --> End
    
    G -->|Yes| I
    
    I -->|부적절| J
    J --> K
    
    I -->|적절| K
    K --> L
    L --> End
```

---

## 5. JavaScript 유효성 검증

### 5.1 productCheck() 함수 흐름

```mermaid
flowchart TD
    Start([productCheck 호출])
    
    A[frm.name.value 가져오기]
    B{name.length<br/>== 0?}
    C["alert: 상품명을 써주세요"]
    C1[frm.name에 포커스]
    C2[return false]
    
    D[frm.price.value 가져오기]
    E{price.length<br/>== 0?}
    F["alert: 가격을 써주세요"]
    F1[frm.price에 포커스]
    F2[return false]
    
    G{isNaN 함수로<br/>숫자 체크}
    H["alert: 숫자를 입력해야 합니다"]
    H1[frm.price에 포커스]
    H2[return false]
    
    I[return true<br/>폼 제출 진행]
    
    End([폼 제출])
    
    Start --> A
    A --> B
    
    B -->|Yes<br/>비어있음| C
    C --> C1
    C1 --> C2
    C2 --> End
    
    B -->|No| D
    D --> E
    
    E -->|Yes<br/>비어있음| F
    F --> F1
    F1 --> F2
    F2 --> End
    
    E -->|No| G
    
    G -->|Yes<br/>숫자 아님| H
    H --> H1
    H1 --> H2
    H2 --> End
    
    G -->|No<br/>숫자임| I
    I --> End
```

---

**Part 2로 계속됩니다...**

