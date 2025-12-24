# Test Layer Context

## Module Context

테스트 계층은 JUnit 5를 사용하여 단위 테스트와 통합 테스트를 작성합니다. Spring Test Context를 활용하여 실제 Spring 컨텍스트를 로드합니다.

**의존성 관계:**
- Test -> Service, Mapper (테스트 대상)
- Test -> Spring Test Context (의존)
- Test -> 실제 데이터베이스 연결

## Tech Stack & Constraints

- JUnit 5.10.1 (Jupiter API, Engine)
- Spring Test 6.2.14
- Spring Extension (JUnit 5 통합)

**제약사항:**
- 모든 테스트는 `src/test/java/` 디렉토리에 위치
- 테스트 클래스는 `{Target}Tests.java` 또는 `{Target}Test.java` 네이밍
- Spring 컨텍스트는 `@ContextConfiguration`으로 로드

## Implementation Patterns

**통합 테스트 패턴:**
```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class ReplyMapperTests {
    @Autowired
    private ReplyMapper replyMapper;
    
    @Test
    public void testInsert() {
        ReplyDTO dto = ReplyDTO.builder()
            .bno(1L)
            .replyText("테스트 댓글")
            .replyer("테스트작성자")
            .build();
        
        replyMapper.insert(dto);
        log.info("생성된 rno: {}", dto.getRno());
    }
}
```

**대량 데이터 생성 패턴:**
```java
@Test
public void generateBulkData() {
    for(int i = 1; i <= 50000; i++) {
        BoardDTO board = BoardDTO.builder()
            .title("제목 " + i)
            .content("내용 " + i)
            .writer("작성자" + (i % 100 + 1))
            .build();
        boardMapper.insert(board);
    }
}
```

**파일 네이밍:**
- `{Target}Tests.java` (예: `BoardMapperTests.java`)
- 패키지 구조는 대상과 동일 (예: `org.zerock.mapper.BoardMapperTests`)

## Testing Strategy

**단위 테스트:**
- Service 계층: 실제 Mapper 주입하여 테스트
- Mapper 계층: 실제 데이터베이스 연결하여 테스트

**통합 테스트:**
- 전체 흐름 테스트 (Controller -> Service -> Mapper)
- 실제 HTTP 요청으로 테스트 (MockMvc 사용 가능)

**테스트 실행:**
- IDE에서 개별 테스트 메서드 실행
- Maven: `mvn test` (전체 테스트)
- 특정 테스트: `mvn test -Dtest=ClassName#methodName`

## Local Golden Rules

**Do's:**
- `@Log4j2` 사용하여 테스트 로그 출력
- 테스트 데이터는 테스트 후 정리 (또는 별도 테스트 DB 사용)
- 실제 데이터베이스 연결 사용 (통합 테스트)
- 대량 데이터 생성은 별도 테스트 클래스로 분리

**Don'ts:**
- 프로덕션 데이터베이스에 직접 테스트하지 말 것
- 테스트 간 의존성 만들지 말 것 (독립적 실행 가능해야 함)
- 하드코딩된 테스트 데이터 대신 Builder 패턴 사용
- 테스트 메서드명은 `test{Action}` 형식 사용

