# ActionFactory 사용 여부 비교

## 현재 구조 (ActionFactory 사용)

### 현재 코드 구조
```
BoardServlet
  └─→ ActionFactory.getAction(command)
        └─→ BoardListAction (생성)
```

### 코드 예시

**BoardServlet.java**
```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    String command = request.getParameter("command");
    ActionFactory af = ActionFactory.getInstance();
    Action action = af.getAction(command);  // ActionFactory에 위임
    if(action != null) {
        action.execute(request, response);
    }
}
```

**ActionFactory.java**
```java
public Action getAction(String command) {
    Action action = null;
    if(command.equals("board_list")) {
        action = new BoardListAction();
    } else if(command.equals("board_write")) {
        action = new BoardWriteAction();
    } else if(command.equals("board_view")) {
        action = new BoardViewAction();
    }
    // ... 더 많은 command 처리
    return action;
}
```

---

## ActionFactory를 사용하지 않는 경우

### 대안 구조
```
BoardServlet
  └─→ 직접 if-else 또는 switch로 처리
```

### 코드 예시

**BoardServlet.java (ActionFactory 없이)**
```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    String command = request.getParameter("command");
    Action action = null;
    
    // BoardServlet에서 직접 처리
    if(command.equals("board_list")) {
        action = new BoardListAction();
    } else if(command.equals("board_write")) {
        action = new BoardWriteAction();
    } else if(command.equals("board_view")) {
        action = new BoardViewAction();
    } else if(command.equals("board_update")) {
        action = new BoardUpdateAction();
    } else if(command.equals("board_delete")) {
        action = new BoardDeleteAction();
    }
    // ... 더 많은 command 처리
    
    if(action != null) {
        action.execute(request, response);
    }
}
```

---

## 비교 분석

### 1. 코드 확장성 (Scalability)

#### ✅ ActionFactory 사용 시
- **장점**: 새로운 Action 추가 시 `BoardServlet`은 수정 불필요
  - `ActionFactory`에만 `else if` 추가
  - `BoardServlet`은 변경 없음
- **예시**: `board_reply` 기능 추가 시
  ```java
  // ActionFactory.java에만 추가
  else if(command.equals("board_reply")) {
      action = new BoardReplyAction();
  }
  ```

#### ❌ ActionFactory 미사용 시
- **단점**: 새로운 Action 추가 시 `BoardServlet` 수정 필요
  - `BoardServlet`의 `doGet()` 메서드에 `else if` 추가
  - `BoardServlet`이 비대해짐

---

### 2. 유지보수성 (Maintainability)

#### ✅ ActionFactory 사용 시
- **장점**: Command와 Action의 매핑이 한 곳에 집중
  - 모든 command 처리가 `ActionFactory`에 모여있음
  - 매핑 관계를 한눈에 파악 가능
- **예시**: command 이름 변경 시 `ActionFactory`만 수정

#### ❌ ActionFactory 미사용 시
- **단점**: Command와 Action의 매핑이 `BoardServlet`에 분산
  - `BoardServlet`이 비즈니스 로직과 매핑 로직을 모두 처리
  - 코드가 복잡해짐

---

### 3. 단일 책임 원칙 (Single Responsibility Principle)

#### ✅ ActionFactory 사용 시
- **BoardServlet**: HTTP 요청 처리에만 집중
- **ActionFactory**: Action 인스턴스 생성에만 집중
- **각 클래스가 하나의 책임만 가짐**

#### ❌ ActionFactory 미사용 시
- **BoardServlet**: HTTP 요청 처리 + Action 생성 로직 모두 처리
- **단일 책임 원칙 위반**

---

### 4. 코드 가독성

#### ✅ ActionFactory 사용 시
```java
// BoardServlet - 간결하고 명확
ActionFactory af = ActionFactory.getInstance();
Action action = af.getAction(command);
```

#### ❌ ActionFactory 미사용 시
```java
// BoardServlet - 길고 복잡
Action action = null;
if(command.equals("board_list")) {
    action = new BoardListAction();
} else if(command.equals("board_write")) {
    action = new BoardWriteAction();
} else if(command.equals("board_view")) {
    action = new BoardViewAction();
} else if(command.equals("board_update")) {
    action = new BoardUpdateAction();
} else if(command.equals("board_delete")) {
    action = new BoardDeleteAction();
} else if(command.equals("board_reply")) {
    action = new BoardReplyAction();
}
// ... 계속 늘어남
```

---

### 5. 테스트 용이성

#### ✅ ActionFactory 사용 시
- **장점**: ActionFactory를 모킹하거나 교체하기 쉬움
  ```java
  // 테스트에서 ActionFactory를 모킹 가능
  ActionFactory mockFactory = mock(ActionFactory.class);
  when(mockFactory.getAction("board_list")).thenReturn(new BoardListAction());
  ```

#### ❌ ActionFactory 미사용 시
- **단점**: BoardServlet 전체를 테스트해야 함
  - Action 생성 로직과 요청 처리 로직이 섞여있어 테스트 복잡

---

### 6. 성능 비교

#### 성능 차이
- **ActionFactory 사용**: 메서드 호출 1회 추가 (`getAction()`)
- **ActionFactory 미사용**: 직접 처리
- **결론**: 성능 차이는 **거의 없음** (메서드 호출 1회는 무시할 수준)

---

## 결론

### ActionFactory를 사용하는 것이 더 효율적인 이유

1. **확장성**: 새로운 기능 추가 시 `BoardServlet` 수정 불필요
2. **유지보수성**: Command-Action 매핑이 한 곳에 집중
3. **단일 책임 원칙**: 각 클래스가 명확한 역할을 가짐
4. **가독성**: `BoardServlet`이 간결하고 이해하기 쉬움
5. **테스트 용이성**: 각 컴포넌트를 독립적으로 테스트 가능

### ActionFactory를 사용하지 않아도 되는 경우

- **프로젝트 규모가 매우 작고 확장 계획이 없는 경우**
- **Command가 1~2개만 있는 간단한 프로젝트**
- **성능이 극도로 중요한 경우** (하지만 실제로는 차이 없음)

### 현재 프로젝트에서의 권장사항

**✅ ActionFactory 사용을 권장합니다**

이유:
- 게시판 기능은 계속 확장될 가능성이 높음 (목록, 작성, 수정, 삭제, 댓글 등)
- 유지보수성을 고려하면 ActionFactory 패턴이 유리
- 성능 차이는 거의 없음
- 코드 구조가 더 깔끔하고 확장 가능함

---

## 실제 예시: 기능 추가 시나리오

### 시나리오: 게시글 수정 기능 추가

#### ✅ ActionFactory 사용 시
```java
// 1. BoardUpdateAction.java 생성
// 2. ActionFactory.java에만 추가
else if(command.equals("board_update")) {
    action = new BoardUpdateAction();
}
// 3. BoardServlet.java는 수정 불필요 ✅
```

#### ❌ ActionFactory 미사용 시
```java
// 1. BoardUpdateAction.java 생성
// 2. BoardServlet.java의 doGet() 메서드 수정 필요
else if(command.equals("board_update")) {
    action = new BoardUpdateAction();
}
// 3. BoardServlet.java가 계속 비대해짐 ❌
```

---

## 설계 패턴 관점

### Factory Pattern (팩토리 패턴)
- **목적**: 객체 생성 로직을 캡슐화
- **장점**: 객체 생성과 사용을 분리하여 유연성 향상
- **적용**: ActionFactory는 Factory Pattern의 구현

### 현재 프로젝트에서의 적용
- **Factory**: `ActionFactory`
- **Product**: `Action` 인터페이스 구현체들 (`BoardListAction`, `BoardWriteAction` 등)
- **Client**: `BoardServlet`

이 패턴을 통해 `BoardServlet`은 구체적인 Action 클래스를 알 필요 없이 `Action` 인터페이스만 사용할 수 있습니다.

