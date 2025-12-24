# Controller Layer Context

## Module Context

Controller 계층은 HTTP 요청을 받아 Service 계층에 위임하고, 응답을 반환하는 역할을 담당합니다. Spring MVC의 DispatcherServlet이 요청을 라우팅합니다.

**의존성 관계:**
- Controller -> Service (의존)
- Controller -> DTO (사용)
- Controller는 Mapper를 직접 호출하지 않음

## Tech Stack & Constraints

- Spring WebMVC 6.2.14
- Jackson 2.20.0 (JSON 직렬화/역직렬화)
- JSP ViewResolver (일반 페이지)
- REST API는 JSON만 사용, XML 미지원

**제약사항:**
- `@RestController`는 JSON 응답만 반환
- `@Controller`는 View 이름 반환 (JSP 렌더링)
- REST API 경로는 `/replies/*` 형식, 일반 페이지는 `/board/*` 형식

## Implementation Patterns

**REST API 컨트롤러 패턴:**
```java
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/replies")
public class ReplyController {
    private final ReplyService replyService;
    
    @PostMapping("")
    public ResponseEntity<Map<String, Integer>> add(@RequestBody ReplyDTO dto) {
        replyService.add(dto);
        return ResponseEntity.ok(Map.of("result", dto.getRno()));
    }
}
```

**MVC 컨트롤러 패턴:**
```java
@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, 
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        BoardListPaginDTO dto = boardService.getList(page, size, types, keyword);
        model.addAttribute("dto", dto);
        return "board/list";
    }
}
```

**파일 네이밍:**
- `{Entity}Controller.java` (예: `BoardController.java`, `ReplyController.java`)

## Testing Strategy

Controller 테스트는 통합 테스트로 작성:
- `@SpringJUnitWebConfig` 또는 `@ExtendWith(SpringExtension.class)` 사용
- MockMvc 또는 실제 HTTP 요청으로 테스트

## Local Golden Rules

**Do's:**
- 항상 `@RequiredArgsConstructor`로 생성자 주입 사용
- REST API는 `ResponseEntity`로 응답 래핑
- 로깅은 `log.info()`, `log.error()` 사용
- 예외는 Service 계층에서 처리하도록 위임

**Don'ts:**
- Service 계층을 거치지 않고 Mapper 직접 호출 금지
- 비즈니스 로직을 Controller에 작성하지 말 것
- `@Autowired` 필드 주입 사용 금지
- REST API에서 HTML 반환하지 말 것

