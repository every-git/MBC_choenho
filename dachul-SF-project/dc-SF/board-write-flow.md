# 게시판 글 작성 프로세스 플로우

## 전체 흐름 다이어그램

```mermaid
sequenceDiagram
    participant User as 사용자 (브라우저)
    participant JSP as JSP View<br/>(write.jsp)
    participant Controller as BoardController<br/>(write 메서드)
    participant Service as BoardServiceImpl<br/>(addBoard 메서드)
    participant Mapper as BoardMapper<br/>(Interface)
    participant MyBatis as MyBatis<br/>(BoardMapper.xml)
    participant HikariCP as HikariCP<br/>(Connection Pool)
    participant MySQL as MySQL Database<br/>(board 테이블)

    Note over User,MySQL: 게시글 작성 프로세스 시작

    User->>JSP: 1. GET /board/write 요청
    JSP-->>User: 2. 게시글 작성 폼 화면 반환

    User->>JSP: 3. 폼 작성 후<br/>POST /board/write 제출<br/>(title, content)

    JSP->>Controller: 4. HTTP POST 요청 수신<br/>@PostMapping("/write")
    
    Note over Controller: 5. Principal에서<br/>로그인 사용자 정보 추출
    
    Controller->>Controller: 6. dto.setWriter<br/>(principal.getName())
    
    Controller->>Service: 7. boardService.addBoard(dto)<br/>BoardDTO 전달

    Note over Service: 8. @Transactional<br/>트랜잭션 시작

    Service->>Mapper: 9. boardMapper.insertBoard(dto)<br/>Mapper Interface 호출

    Mapper->>MyBatis: 10. insertBoard 쿼리 매핑<br/>(BoardMapper.xml)

    Note over MyBatis: 11. SQL 파라미터 바인딩<br/>#{writer}, #{title}, #{content}

    MyBatis->>HikariCP: 12. PreparedStatement 생성<br/>Connection 획득

    HikariCP->>MySQL: 13. INSERT INTO board<br/>(writer, title, content)<br/>VALUES (?, ?, ?)

    Note over MySQL: 14. AUTO_INCREMENT로<br/>seq 자동 생성

    MySQL->>MySQL: 15. 레코드 삽입<br/>regdate: CURRENT_TIMESTAMP<br/>hit: 0<br/>delflag: false

    MySQL-->>HikariCP: 16. INSERT 성공<br/>(affected rows: 1)

    HikariCP-->>MyBatis: 17. 결과 반환

    MyBatis-->>Mapper: 18. void 반환<br/>(insert 결과)

    Mapper-->>Service: 19. 메서드 완료

    Note over Service: 20. @Transactional<br/>트랜잭션 커밋

    Service-->>Controller: 21. addBoard() 완료

    Controller->>Controller: 22. rttr.addFlashAttribute<br/>("msg", "게시글이 등록되었습니다.")

    Controller-->>JSP: 23. redirect:/board/list<br/>HTTP 302 Redirect

    JSP->>User: 24. Location: /board/list<br/>리다이렉트 응답

    User->>User: 25. 자동으로 /board/list 요청
    Note over User,MySQL: 게시글 목록 페이지로 이동
```

## 레이어별 역할 및 데이터 흐름

```mermaid
graph TB
    subgraph "Presentation Layer"
        A[사용자 브라우저]
        B[JSP View<br/>write.jsp]
    end

    subgraph "Controller Layer"
        C[BoardController<br/>@PostMapping write 메서드]
        D[Principal<br/>Spring Security]
    end

    subgraph "Service Layer"
        E[BoardServiceImpl<br/>@Transactional addBoard]
    end

    subgraph "Data Access Layer"
        F[BoardMapper<br/>Interface]
        G[BoardMapper.xml<br/>MyBatis Mapper]
    end

    subgraph "Connection Layer"
        H[HikariCP<br/>Connection Pool]
    end

    subgraph "Database Layer"
        I[(MySQL Database<br/>board 테이블)]
    end

    A -->|1. GET /board/write| B
    B -->|2. 폼 화면| A
    A -->|3. POST /board/write<br/>title, content| B
    B -->|4. HTTP POST| C
    C -->|5. 인증 정보 조회| D
    D -->|6. username 반환| C
    C -->|7. BoardDTO<br/>writer, title, content| E
    E -->|8. 트랜잭션 시작| E
    E -->|9. BoardDTO 전달| F
    F -->|10. insertBoard 메서드 호출| G
    G -->|11. SQL 생성<br/>INSERT INTO board| H
    H -->|12. PreparedStatement<br/>파라미터 바인딩| I
    I -->|13. INSERT 실행<br/>seq AUTO_INCREMENT| I
    I -->|14. 결과 반환| H
    H -->|15. 성공 결과| G
    G -->|16. void 반환| F
    F -->|17. 완료| E
    E -->|18. 트랜잭션 커밋| E
    E -->|19. 완료| C
    C -->|20. redirect:/board/list| A

    style A fill:#e1f5ff
    style B fill:#fff4e1
    style C fill:#ffe1f5
    style D fill:#ffe1f5
    style E fill:#e1ffe1
    style F fill:#f0e1ff
    style G fill:#f0e1ff
    style H fill:#ffe1e1
    style I fill:#e1e1ff
```

## 데이터베이스 실행 상세

```mermaid
graph LR
    subgraph "BoardDTO 객체"
        A1[seq: 0<br/>초기값]
        A2[writer: '사용자ID'<br/>Principal에서 설정]
        A3[title: '제목'<br/>사용자 입력]
        A4[content: '내용'<br/>사용자 입력]
    end

    subgraph "SQL INSERT 쿼리"
        B1[INSERT INTO board<br/>writer, title, content<br/>VALUES ?, ?, ?]
    end

    subgraph "MySQL board 테이블"
        C1[seq: AUTO_INCREMENT<br/>자동 생성]
        C2[writer: 저장됨]
        C3[title: 저장됨]
        C4[content: 저장됨]
        C5[hit: 0<br/>기본값]
        C6[regdate: CURRENT_TIMESTAMP<br/>자동 설정]
        C7[updatedate: NULL<br/>초기값]
        C8[delflag: false<br/>기본값]
    end

    A1 -.->|seq 미사용| B1
    A2 -->|파라미터 바인딩| B1
    A3 -->|파라미터 바인딩| B1
    A4 -->|파라미터 바인딩| B1

    B1 -->|INSERT 실행| C1
    B1 -->|INSERT 실행| C2
    B1 -->|INSERT 실행| C3
    B1 -->|INSERT 실행| C4

    style A1 fill:#ffe1e1
    style A2 fill:#e1ffe1
    style A3 fill:#e1ffe1
    style A4 fill:#e1ffe1
    style B1 fill:#fff4e1
    style C1 fill:#e1f5ff
    style C2 fill:#e1f5ff
    style C3 fill:#e1f5ff
    style C4 fill:#e1f5ff
```

## 주요 코드 참조

### 1. Controller Layer
```68:79:dc-SF/src/main/java/org/zerock/controller/BoardController.java
    @PostMapping("/write")
    public String write(BoardDTO dto, Principal principal, RedirectAttributes rttr) {
        log.info("board write... dto: {}", dto);

        // 로그인한 사용자를 작성자로 설정
        if (principal != null) {
            dto.setWriter(principal.getName());
        }

        boardService.addBoard(dto);
        rttr.addFlashAttribute("msg", "게시글이 등록되었습니다.");
        return "redirect:/board/list";
    }
```

### 2. Service Layer
```55:60:dc-SF/src/main/java/org/zerock/service/BoardServiceImpl.java
    @Override
    @Transactional
    public void addBoard(BoardDTO dto) {
        log.info("addBoard... dto: {}", dto);
        boardMapper.insertBoard(dto);
    }
```

### 3. Mapper Interface
```48:53:dc-SF/src/main/java/org/zerock/mapper/BoardMapper.java
    /**
     * 게시글 등록
     * 
     * @param dto 게시글 정보
     */
    void insertBoard(BoardDTO dto);
```

### 4. MyBatis Mapper XML
```20:24:dc-SF/src/main/resources/mapper/BoardMapper.xml
    <!-- 게시글 등록 -->
    <insert id="insertBoard" parameterType="BoardDTO">
        INSERT INTO board (writer, title, content)
        VALUES (#{writer}, #{title}, #{content})
    </insert>
```

## 특징 요약

1. **보안**: Spring Security의 Principal을 통한 작성자 자동 설정
2. **트랜잭션**: Service 계층에서 @Transactional로 데이터 무결성 보장
3. **영속성**: MyBatis를 통한 SQL 매핑 및 파라미터 바인딩
4. **연결 풀**: HikariCP를 통한 효율적인 DB 연결 관리
5. **응답 처리**: RedirectAttributes를 사용한 Flash 메시지 전달
