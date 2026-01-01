# Module Context

Service Layer. 비즈니스 로직 구현 및 트랜잭션 관리.

## Role & Responsibilities

- 비즈니스 로직 실행
- 트랜잭션 경계 설정
- Mapper 호출 및 데이터 조합
- 비즈니스 예외 처리
- 도메인 규칙 검증

## Dependencies

- Mapper Layer (단방향 의존)
- DTO (데이터 전달)
- Spring Transaction Management

# Tech Stack & Constraints

## Required Annotations

- @Service: 구현 클래스에 적용
- @Transactional: 트랜잭션 필요 메서드에 적용
- @Autowired: 생성자 주입

## Design Pattern

- Interface + Implementation 구조 필수
- Interface: XxxService.java
- Implementation: XxxServiceImpl.java

# Implementation Patterns

## Service Interface

```java
package org.zerock.service;

import org.zerock.dto.XxxDTO;
import org.zerock.dto.XxxListPaginDTO;
import java.util.List;

public interface XxxService {
    List<XxxDTO> getList();
    XxxListPaginDTO getListWithPaging(int page, int size);
    XxxDTO get(Long id);
    void register(XxxDTO dto);
    void modify(XxxDTO dto);
    void remove(Long id);
}
```

## Service Implementation

```java
package org.zerock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.dto.XxxDTO;
import org.zerock.mapper.XxxMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class XxxServiceImpl implements XxxService {

    private final XxxMapper xxxMapper;

    @Override
    @Transactional(readOnly = true)
    public List<XxxDTO> getList() {
        log.info("getList()");
        return xxxMapper.selectAll();
    }

    @Override
    @Transactional(readOnly = true)
    public XxxListPaginDTO getListWithPaging(int page, int size) {
        log.info("getListWithPaging() - page: {}, size: {}", page, size);
        
        // 페이지 번호와 크기 유효성 검사
        page = page <= 0 ? 1 : page;
        size = (size <= 0 || size > 100) ? 10 : size;
        
        int skip = (page - 1) * size;
        
        List<XxxDTO> list = xxxMapper.selectWithPaging(skip, size);
        int total = xxxMapper.count();
        
        return new XxxListPaginDTO(list, total, page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public XxxDTO get(Long id) {
        log.info("get({})", id);
        XxxDTO dto = xxxMapper.selectById(id);
        if (dto == null) {
            throw new IllegalArgumentException("ID " + id + " not found");
        }
        return dto;
    }

    @Override
    @Transactional
    public void register(XxxDTO dto) {
        log.info("register({})", dto);
        validateDto(dto);
        xxxMapper.insert(dto);
    }

    @Override
    @Transactional
    public void modify(XxxDTO dto) {
        log.info("modify({})", dto);
        validateDto(dto);
        int count = xxxMapper.update(dto);
        if (count == 0) {
            throw new IllegalArgumentException("ID " + dto.getId() + " not found");
        }
    }

    @Override
    @Transactional
    public void remove(Long id) {
        log.info("remove({})", id);
        int count = xxxMapper.delete(id);
        if (count == 0) {
            throw new IllegalArgumentException("ID " + id + " not found");
        }
    }

    private void validateDto(XxxDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
    }
}
```

## Multiple Mapper Usage

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class ComplexServiceImpl implements ComplexService {

    private final MemberMapper memberMapper;
    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public void createBoardWithMemberCheck(BoardDTO boardDTO) {
        log.info("createBoardWithMemberCheck: {}", boardDTO);

        // 회원 존재 확인
        MemberDTO member = memberMapper.selectById(boardDTO.getMemberId());
        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }

        // 게시글 생성
        boardMapper.insert(boardDTO);

        log.info("Board created with ID: {}", boardDTO.getId());
    }

    @Override
    @Transactional
    public void deleteBoardWithComments(Long boardId) {
        log.info("deleteBoardWithComments: {}", boardId);

        // 댓글 먼저 삭제
        commentMapper.deleteByBoardId(boardId);

        // 게시글 삭제
        int count = boardMapper.delete(boardId);
        if (count == 0) {
            throw new IllegalArgumentException("Board not found");
        }
    }
}
```

# Local Golden Rules

## Transaction Management

### Do's
- 쓰기 작업에는 @Transactional 필수
- 읽기 전용 작업에는 @Transactional(readOnly = true)
- 트랜잭션은 Service 메서드 단위로 설정
- RuntimeException 발생 시 자동 롤백 활용

### Don'ts
- Controller나 Mapper에 @Transactional 금지
- checked Exception은 롤백되지 않으므로 주의
- 트랜잭션 안에서 외부 API 호출 지양 (성능 저하)

## Business Logic

### Do's
- 도메인 규칙 검증은 Service에서
- Mapper 호출 결과 검증 (null 체크 등)
- 비즈니스 예외는 명확한 메시지와 함께 throw
- 여러 Mapper를 조합한 복잡한 로직 처리

### Don'ts
- HTTP 관련 객체 (HttpServletRequest, HttpServletResponse) 사용 금지
- View 관련 로직 작성 금지
- SQL 작성 금지 (Mapper에 위임)
- static 변수 사용 금지

## Pagination Service Pattern (ReplyService 예제)

```java
@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    
    private final ReplyMapper replyMapper;
    
    @Override
    public void add(ReplyDTO replyDTO) {
        try {
            log.info("댓글 등록 시도 - bno: {}, replyer: {}, replyText: {}", 
                    replyDTO.getBno(), replyDTO.getReplyer(), replyDTO.getReplyText());
            
            // 유효성 검사
            if(replyDTO.getBno() == 0) {
                throw new ReplyException(400, "게시글 번호가 필요합니다.");
            }
            
            if(replyDTO.getReplyText() == null || replyDTO.getReplyText().trim().isEmpty()) {
                throw new ReplyException(400, "댓글 내용을 입력해주세요.");
            }
            
            replyMapper.insert(replyDTO);
            log.info("댓글 등록 성공 - rno: {}", replyDTO.getRno());
        } catch(ReplyException e) {
            throw e;
        } catch(Exception e) {
            log.error("댓글 등록 실패: {}", e.getMessage(), e);
            throw new ReplyException(500, "댓글 등록에 실패했습니다: " + e.getMessage());
        }
    }
    
    @Override
    public ReplyListPaginDTO listOfBoard(int bno, int page, int size) {
        try {
            int skip = (page - 1) * size;
            
            List<ReplyDTO> replyDTOList = replyMapper.listOfBoard(bno, skip, size);
            int count = replyMapper.countOfBoard(bno);
            
            return new ReplyListPaginDTO(replyDTOList, count, page, size);
            
        } catch(Exception e) {
            log.error("댓글 목록 조회 실패: {}", e.getMessage());
            throw new ReplyException(500, "댓글 목록 조회에 실패했습니다.");
        }
    }
}
```

## Exception Handling

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class XxxServiceImpl implements XxxService {

    private final XxxMapper xxxMapper;

    @Override
    @Transactional
    public void complexOperation(XxxDTO dto) {
        try {
            // 비즈니스 로직
            xxxMapper.insert(dto);
        } catch (DataAccessException e) {
            log.error("Database error: ", e);
            throw new RuntimeException("데이터 저장 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            throw new RuntimeException("처리 중 오류가 발생했습니다.", e);
        }
    }
}
```

# Testing Strategy

## Service Test Pattern

```java
@SpringJUnitConfig(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
@Transactional
class XxxServiceTest {

    @Autowired
    private XxxService xxxService;

    @Test
    void testRegister() {
        XxxDTO dto = XxxDTO.builder()
            .name("Test")
            .description("Test Description")
            .build();

        xxxService.register(dto);

        assertNotNull(dto.getId());
    }

    @Test
    void testGetList() {
        List<XxxDTO> list = xxxService.getList();
        assertNotNull(list);
    }

    @Test
    void testModify() {
        XxxDTO dto = xxxService.get(1L);
        dto.setName("Modified");

        xxxService.modify(dto);

        XxxDTO modified = xxxService.get(1L);
        assertEquals("Modified", modified.getName());
    }

    @Test
    void testRemove() {
        xxxService.remove(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            xxxService.get(1L);
        });
    }
}
```

## Mock Testing

```java
@ExtendWith(MockitoExtension.class)
class XxxServiceTest {

    @Mock
    private XxxMapper xxxMapper;

    @InjectMocks
    private XxxServiceImpl xxxService;

    @Test
    void testRegister() {
        XxxDTO dto = new XxxDTO();
        dto.setName("Test");

        xxxService.register(dto);

        verify(xxxMapper, times(1)).insert(dto);
    }

    @Test
    void testGet_NotFound() {
        when(xxxMapper.selectById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            xxxService.get(999L);
        });
    }
}
```

# Related Contexts

- Mapper Layer: ../mapper/AGENTS.md
- Controller Layer: ../controller/AGENTS.md
- DTO: ../dto/AGENTS.md
