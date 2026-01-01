# Module Context

MVC 패턴의 Controller Layer. HTTP 요청을 받아 Service 호출 후 View로 응답.

## Role & Responsibilities

- HTTP 요청 매핑 (@RequestMapping, @GetMapping, @PostMapping)
- 요청 파라미터 바인딩 및 검증
- Service 계층 호출
- Model에 데이터 담아 View로 전달
- Redirect/Forward 처리
- Exception Handling

## Dependencies

- Service Layer (단방향 의존)
- DTO (데이터 전달)
- Spring MVC (DispatcherServlet)

# Tech Stack & Constraints

## Required Annotations

- @Controller: 클래스 레벨 (일반 페이지 컨트롤러)
- @RestController: 클래스 레벨 (REST API 컨트롤러, JSON 응답)
- @RequestMapping: 클래스 또는 메서드 레벨
- @GetMapping, @PostMapping, @PutMapping, @DeleteMapping: 메서드 레벨
- @RequiredArgsConstructor: 생성자 주입 (Lombok)
- @RequestBody: JSON 요청 바인딩 (REST API)
- @ModelAttribute: 폼 데이터 바인딩
- @RequestParam: 쿼리 파라미터 바인딩
- @PathVariable: URL 경로 변수 바인딩
- @ExceptionHandler: 예외 처리

## Forbidden Patterns

- Mapper 직접 호출 금지
- 비즈니스 로직 작성 금지
- DB 연결 코드 작성 금지
- static 변수 사용 금지 (Controller는 싱글톤)
- HttpSession에 대량 데이터 저장 금지

# Implementation Patterns

## Basic Controller Structure

```java
package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.dto.XxxDTO;
import org.zerock.service.XxxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/xxx")
@RequiredArgsConstructor
@Slf4j
public class XxxController {

    private final XxxService xxxService;

    @GetMapping("/list")
    public String list(Model model) {
        log.info("GET /xxx/list");
        List<XxxDTO> list = xxxService.getList();
        model.addAttribute("list", list);
        return "xxx/list";
    }

    @GetMapping("/register")
    public String registerForm() {
        log.info("GET /xxx/register");
        return "xxx/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute XxxDTO dto, RedirectAttributes rttr) {
        log.info("POST /xxx/register: {}", dto);
        xxxService.register(dto);
        rttr.addFlashAttribute("message", "등록되었습니다.");
        return "redirect:/xxx/list";
    }

    @GetMapping("/read")
    public String read(@RequestParam("id") Long id, Model model) {
        log.info("GET /xxx/read?id={}", id);
        XxxDTO dto = xxxService.get(id);
        model.addAttribute("dto", dto);
        return "xxx/read";
    }

    @PostMapping("/modify")
    public String modify(@ModelAttribute XxxDTO dto, RedirectAttributes rttr) {
        log.info("POST /xxx/modify: {}", dto);
        xxxService.modify(dto);
        rttr.addFlashAttribute("message", "수정되었습니다.");
        return "redirect:/xxx/read?id=" + dto.getId();
    }

    @PostMapping("/remove")
    public String remove(@RequestParam("id") Long id, RedirectAttributes rttr) {
        log.info("POST /xxx/remove?id={}", id);
        xxxService.remove(id);
        rttr.addFlashAttribute("message", "삭제되었습니다.");
        return "redirect:/xxx/list";
    }
}
```

## RESTful API Controller

```java
@RestController
@RequestMapping("/api/xxx")
@RequiredArgsConstructor
@Slf4j
public class XxxApiController {

    private final XxxService xxxService;

    @GetMapping
    public ResponseEntity<List<XxxDTO>> getList() {
        log.info("GET /api/xxx");
        List<XxxDTO> list = xxxService.getList();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<XxxDTO> create(@RequestBody XxxDTO dto) {
        log.info("POST /api/xxx: {}", dto);
        xxxService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<XxxDTO> get(@PathVariable Long id) {
        log.info("GET /api/xxx/{}", id);
        XxxDTO dto = xxxService.get(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody XxxDTO dto) {
        log.info("PUT /api/xxx/{}: {}", id, dto);
        dto.setId(id);
        xxxService.modify(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/xxx/{}", id);
        xxxService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
```

## REST API with Pagination (ReplyController 예제)

```java
@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
@Log4j2
public class ReplyController {
    
    private final ReplyService replyService;
    
    @ExceptionHandler(ReplyException.class)
    public ResponseEntity<String> handleReplyError(ReplyException ex){
        log.error("댓글 처리 오류: {}", ex.getMessage());
        return ResponseEntity.status(ex.getCode()).body(ex.getMsg());
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Integer>> add(@RequestBody ReplyDTO replyDTO){
        log.info("댓글 등록 요청: {}", replyDTO);
        replyService.add(replyDTO);
        return ResponseEntity.ok(Map.of("result", replyDTO.getRno()));
    }
    
    @GetMapping("/{bno}/list")
    public ResponseEntity<ReplyListPaginDTO> listOfBoard(
                @PathVariable("bno") int bno, 
                @RequestParam(name="page", defaultValue = "1") int page,
                @RequestParam(name="size", defaultValue = "10") int size
            ){
        log.info("댓글 목록 조회 요청 - bno: {}, page: {}, size: {}", bno, page, size);
        ReplyListPaginDTO listOfBoard = replyService.listOfBoard(bno, page, size);
        return ResponseEntity.ok(listOfBoard);
    }

    @GetMapping("/{rno}")
    public ResponseEntity<ReplyDTO> read(@PathVariable("rno") int rno){
        ReplyDTO replyDTO = replyService.getOne(rno);
        return ResponseEntity.ok(replyDTO);
    }
    
    @DeleteMapping("/{rno}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("rno") int rno){
        replyService.remove(rno);
        return ResponseEntity.ok(Map.of("result", "deleted"));
    }

    @PutMapping("/{rno}")
    public ResponseEntity<Map<String, String>> modify(@PathVariable("rno") int rno, @RequestBody ReplyDTO replyDTO){
        replyDTO.setRno(rno);
        replyService.modify(replyDTO);
        return ResponseEntity.ok(Map.of("result", "modified"));
    }
}
```

## Pagination Controller Pattern

```java
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Log4j2
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public String list(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {
        log.info("board list... page: {}, size: {}", page, size);
        BoardListPaginDTO paginDTO = boardService.getBoardsWithPaging(page, size);
        model.addAttribute("dto", paginDTO);
        return "board/list";
    }
}
```

## Exception Handling

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        log.error("Exception occurred: ", e);
        model.addAttribute("error", "처리 중 오류가 발생했습니다.");
        return "error/error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, Model model) {
        log.warn("IllegalArgumentException: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "error/error";
    }
}
```

# Local Golden Rules

## Do's

- 생성자 주입 사용 (@RequiredArgsConstructor 또는 @Autowired 생성자)
- 로깅 활용 (log.info, log.error)
- RedirectAttributes로 일회성 메시지 전달
- Model에 데이터 담아 View로 전달
- POST-Redirect-GET 패턴 준수
- @RequestParam으로 쿼리 파라미터 명시적 바인딩
- @ModelAttribute로 폼 데이터 바인딩
- @PathVariable로 URL 경로 변수 바인딩

## Don'ts

- 필드 주입 금지 (@Autowired 필드 변수에 직접 사용 금지)
- 비즈니스 로직 작성 금지
- Mapper 직접 호출 금지
- static 변수 사용 금지
- HttpSession에 큰 객체 저장 금지
- Exception을 catch만 하고 무시 금지
- 사용자에게 Stack Trace 노출 금지
- REST API에서 일반적인 예외를 그대로 노출 금지 (커스텀 예외 사용)
- 페이징 파라미터 검증 누락 금지 (page > 0, size > 0 체크)

## Validation

```java
@PostMapping("/register")
public String register(@Valid @ModelAttribute XxxDTO dto,
                       BindingResult result,
                       RedirectAttributes rttr) {
    if (result.hasErrors()) {
        log.warn("Validation errors: {}", result.getAllErrors());
        return "xxx/register";  // 다시 폼으로
    }

    xxxService.register(dto);
    rttr.addFlashAttribute("message", "등록되었습니다.");
    return "redirect:/xxx/list";
}
```

# Testing Strategy

## Controller Test Pattern

```java
@WebMvcTest(XxxController.class)
class XxxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private XxxService xxxService;

    @Test
    void testList() throws Exception {
        List<XxxDTO> list = Arrays.asList(new XxxDTO());
        when(xxxService.getList()).thenReturn(list);

        mockMvc.perform(get("/xxx/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("xxx/list"))
            .andExpect(model().attributeExists("list"));
    }

    @Test
    void testRegister() throws Exception {
        mockMvc.perform(post("/xxx/register")
                .param("name", "Test")
                .param("description", "Test Description"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/xxx/list"));

        verify(xxxService, times(1)).register(any(XxxDTO.class));
    }
}
```

# Related Contexts

- Service Layer: ../service/AGENTS.md
- DTO: ../dto/AGENTS.md
- Views: ../../../webapp/WEB-INF/views/AGENTS.md
