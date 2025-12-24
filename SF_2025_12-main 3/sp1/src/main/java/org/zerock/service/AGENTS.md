# Service Layer Context

## Module Context

Service 계층은 비즈니스 로직을 처리하고, 트랜잭션을 관리합니다. Controller와 Mapper 사이의 중간 계층으로 데이터 검증, 변환, 예외 처리를 담당합니다.

**의존성 관계:**
- Service -> Mapper (의존)
- Service -> DTO (사용)
- Service -> Exception (사용)

## Tech Stack & Constraints

- Spring Framework 6.2.14
- Spring TX (트랜잭션 관리, 현재는 명시적 트랜잭션 설정 없음)
- Lombok `@RequiredArgsConstructor` (생성자 주입)

**제약사항:**
- Service 메서드는 void 또는 DTO 반환
- 예외는 커스텀 Exception으로 변환하여 던지기
- 트랜잭션은 향후 `@Transactional` 추가 고려

## Implementation Patterns

**Service 클래스 패턴:**
```java
@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyMapper replyMapper;
    
    public void add(ReplyDTO dto) {
        try {
            replyMapper.insert(dto);
        } catch(Exception e) {
            throw new ReplyException(500, "Insert Error");
        }
    }
    
    public ReplyDTO getOne(int rno) {
        try {
            return replyMapper.read(rno);
        } catch(Exception e) {
            throw new ReplyException(404, "Not Found");
        }
    }
}
```

**예외 처리 패턴:**
- 커스텀 Exception 클래스 생성 (`ReplyException`)
- HTTP 상태 코드와 메시지를 포함
- Controller의 `@ExceptionHandler`에서 처리

**파일 네이밍:**
- `{Entity}Service.java` (예: `BoardService.java`, `ReplyService.java`)
- 예외는 `service/exception/{Entity}Exception.java`

## Testing Strategy

Service 테스트는 단위 테스트로 작성:
- `@ExtendWith(SpringExtension.class)` 사용
- `@ContextConfiguration`으로 Spring 컨텍스트 로드
- Mapper는 실제 데이터베이스 연결 또는 Mock 사용

## Local Golden Rules

**Do's:**
- 항상 try-catch로 예외를 커스텀 Exception으로 변환
- 로깅은 `log.info()`, `log.error()` 사용
- Mapper 호출 결과를 검증 (count == 0 체크 등)

**Don'ts:**
- Controller나 View 계층에 직접 의존하지 말 것
- SQL 쿼리를 Service에 작성하지 말 것
- 비즈니스 로직 없이 단순 위임만 하는 메서드 지양

