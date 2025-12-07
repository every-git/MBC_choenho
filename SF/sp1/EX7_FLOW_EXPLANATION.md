# ex7 요청과 응답 흐름 상세 설명

## 📋 개요

`ex7`은 **리다이렉트(Redirect)**를 사용하여 다른 URL로 요청을 전달하는 엔드포인트입니다. `RedirectAttributes`를 사용하여 리다이렉트 시 데이터를 전달합니다.

---

## 🔍 코드 분석

```java
@GetMapping("/ex7")
public String ex7(RedirectAttributes rttr) {
    rttr.addAttribute("name", "Hong Gil Dong");
    rttr.addAttribute("age", 25);
    return "redirect:/sample/ex8";
}
```

### 주요 구성 요소

1. **`@GetMapping("/ex7")`**: GET 요청을 `/sample/ex7`로 매핑
2. **`RedirectAttributes rttr`**: 리다이렉트 시 데이터를 전달하는 객체
3. **`rttr.addAttribute()`**: URL 쿼리 파라미터로 데이터 추가
4. **`return "redirect:/sample/ex8"`**: `/sample/ex8`로 리다이렉트

---

## 🔄 전체 요청-응답 흐름

### 단계별 상세 흐름

```
[1단계] 브라우저 요청
    ↓
    GET http://localhost:8080/sample/ex7
    ↓
[2단계] DispatcherServlet
    ↓
    요청 수신 및 처리 시작
    ↓
[3단계] HandlerMapping
    ↓
    @GetMapping("/ex7") 매핑 찾기
    ↓
    HelloController.ex7() 메서드 발견
    ↓
[4단계] HandlerAdapter
    ↓
    ex7() 메서드 실행
    ↓
[5단계] Controller 실행
    ↓
    RedirectAttributes rttr 객체 생성
    ↓
    rttr.addAttribute("name", "Hong Gil Dong")
    rttr.addAttribute("age", 25)
    ↓
    return "redirect:/sample/ex8"
    ↓
[6단계] Redirect 처리
    ↓
    Spring이 "redirect:" 접두사 인식
    ↓
    RedirectAttributes의 데이터를 URL 쿼리 파라미터로 변환
    ↓
    최종 리다이렉트 URL 생성:
    /sample/ex8?name=Hong+Gil+Dong&age=25
    ↓
[7단계] HTTP 응답
    ↓
    HTTP 302 Found (또는 303 See Other)
    Location: /sample/ex8?name=Hong+Gil+Dong&age=25
    ↓
[8단계] 브라우저 자동 리다이렉트
    ↓
    브라우저가 Location 헤더를 읽고 자동으로 새 요청 전송
    ↓
    GET http://localhost:8080/sample/ex8?name=Hong+Gil+Dong&age=25
    ↓
[9단계] ex8 처리 (ex8이 존재하는 경우)
    ↓
    ex8 컨트롤러 메서드 실행
    ↓
    쿼리 파라미터로 전달된 데이터 사용
```

---

## 📊 상세 흐름도

```
┌─────────────┐
│  브라우저   │
└──────┬──────┘
       │ 1. GET /sample/ex7
       ↓
┌──────────────────────┐
│  DispatcherServlet   │
└──────┬───────────────┘
       │ 2. 요청 처리 시작
       ↓
┌──────────────────────┐
│   HandlerMapping      │
│   @GetMapping("/ex7") │
└──────┬───────────────┘
       │ 3. ex7() 메서드 찾기
       ↓
┌──────────────────────┐
│   HandlerAdapter      │
└──────┬───────────────┘
       │ 4. 메서드 실행
       ↓
┌─────────────────────────────────┐
│   HelloController.ex7()          │
│                                  │
│   RedirectAttributes rttr 생성   │
│   rttr.addAttribute("name", ...) │
│   rttr.addAttribute("age", 25)   │
│   return "redirect:/sample/ex8" │
└──────┬──────────────────────────┘
       │ 5. 리다이렉트 반환
       ↓
┌─────────────────────────────────┐
│   Redirect 처리                  │
│                                  │
│   "redirect:" 접두사 인식        │
│   데이터를 쿼리 파라미터로 변환  │
│   URL: /sample/ex8?name=...&age=25
└──────┬──────────────────────────┘
       │ 6. HTTP 302 응답
       ↓
┌─────────────┐
│  브라우저   │
│  Location 헤더 읽기
└──────┬──────┘
       │ 7. 자동 리다이렉트
       │ GET /sample/ex8?name=...&age=25
       ↓
┌──────────────────────┐
│  DispatcherServlet   │
│  (새 요청 처리)       │
└──────┬───────────────┘
       │ 8. ex8 처리
       ↓
┌──────────────────────┐
│   HelloController.ex8()│
│   (존재하는 경우)     │
└──────────────────────┘
```

---

## 🔑 핵심 개념

### 1. RedirectAttributes

**역할**: 리다이렉트 시 데이터를 전달하는 Spring의 특수 객체

**주요 메서드**:
- `addAttribute(String name, Object value)`: URL 쿼리 파라미터로 추가
- `addFlashAttribute(String name, Object value)`: 세션에 임시 저장 (다음 요청에서만 사용 가능)

**ex7에서의 사용**:
```java
rttr.addAttribute("name", "Hong Gil Dong");
rttr.addAttribute("age", 25);
```

**결과**: 
- URL에 쿼리 파라미터로 추가됨
- 최종 URL: `/sample/ex8?name=Hong+Gil+Dong&age=25`

### 2. 리다이렉트 반환값

**형식**: `"redirect:/경로"`

**특징**:
- `"redirect:"` 접두사가 있으면 Spring이 리다이렉트로 인식
- 절대 경로: `"redirect:/sample/ex8"` (컨텍스트 루트 기준)
- 상대 경로: `"redirect:ex8"` (현재 경로 기준)

**ex7에서의 사용**:
```java
return "redirect:/sample/ex8";
```

### 3. HTTP 리다이렉트 상태 코드

**302 Found** (또는 **303 See Other**):
- 브라우저가 `Location` 헤더의 URL로 자동 이동
- GET 요청으로 리다이렉트됨

**응답 헤더 예시**:
```
HTTP/1.1 302 Found
Location: /sample/ex8?name=Hong+Gil+Dong&age=25
```

---

## 📝 실제 실행 예시

### 요청 1: ex7 접속

```
요청:
GET http://localhost:8080/sample/ex7

처리:
1. DispatcherServlet이 요청 수신
2. HandlerMapping이 @GetMapping("/ex7") 찾기
3. HelloController.ex7() 실행
4. RedirectAttributes에 데이터 추가
5. "redirect:/sample/ex8" 반환

응답:
HTTP/1.1 302 Found
Location: /sample/ex8?name=Hong+Gil+Dong&age=25
```

### 요청 2: 브라우저 자동 리다이렉트

```
요청:
GET http://localhost:8080/sample/ex8?name=Hong+Gil+Dong&age=25

처리:
1. DispatcherServlet이 새 요청 수신
2. HandlerMapping이 @GetMapping("/ex8") 찾기
3. HelloController.ex8() 실행 (존재하는 경우)
4. 쿼리 파라미터로 전달된 데이터 사용

응답:
HTTP/1.1 200 OK
(뷰 렌더링 결과)
```

---

## ⚠️ 주의사항

### 1. ex8이 존재하지 않는 경우

현재 코드에서 `ex8` 메서드가 정의되어 있지 않으면:
- **404 Not Found** 오류 발생
- 리다이렉트는 성공하지만, 최종 목적지에서 오류 발생

**해결 방법**:
```java
@GetMapping("/ex8")
public String ex8(@RequestParam String name, 
                  @RequestParam int age, 
                  Model model) {
    model.addAttribute("name", name);
    model.addAttribute("age", age);
    return "sample/ex8";  // ex8.jsp 필요
}
```

### 2. RedirectAttributes vs Model

| 구분 | Model | RedirectAttributes |
|------|-------|-------------------|
| **사용 시점** | 일반 뷰 렌더링 | 리다이렉트 시 |
| **데이터 전달 방식** | Request Scope | URL 쿼리 파라미터 |
| **데이터 유지** | 한 요청 동안만 | 리다이렉트된 URL에 포함 |
| **사용 예시** | `model.addAttribute()` | `rttr.addAttribute()` |

### 3. addAttribute vs addFlashAttribute

**addAttribute**:
- URL 쿼리 파라미터로 추가
- 브라우저 주소창에 표시됨
- 예: `/sample/ex8?name=Hong+Gil+Dong&age=25`

**addFlashAttribute**:
- 세션에 임시 저장
- 다음 요청에서만 사용 가능
- 브라우저 주소창에 표시되지 않음
- 예: `rttr.addFlashAttribute("message", "성공!");`

---

## 🔄 전체 흐름 요약

1. **요청**: `GET /sample/ex7`
2. **컨트롤러 실행**: `ex7()` 메서드
3. **데이터 추가**: RedirectAttributes에 name, age 추가
4. **리다이렉트 반환**: `"redirect:/sample/ex8"`
5. **URL 생성**: `/sample/ex8?name=Hong+Gil+Dong&age=25`
6. **HTTP 응답**: `302 Found` + `Location` 헤더
7. **브라우저 자동 이동**: 새 URL로 자동 요청
8. **최종 처리**: ex8 컨트롤러 메서드 실행 (존재하는 경우)

---

## 💡 활용 예시

### 리다이렉트를 사용하는 경우

1. **폼 제출 후 결과 페이지로 이동**
   ```java
   @PostMapping("/register")
   public String register(User user, RedirectAttributes rttr) {
       userService.save(user);
       rttr.addFlashAttribute("message", "회원가입 성공!");
       return "redirect:/sample/success";
   }
   ```

2. **삭제 후 목록 페이지로 이동**
   ```java
   @PostMapping("/delete")
   public String delete(Long id, RedirectAttributes rttr) {
       service.delete(id);
       rttr.addFlashAttribute("message", "삭제되었습니다.");
       return "redirect:/sample/list";
   }
   ```

3. **검색 조건 유지하며 리다이렉트**
   ```java
   @GetMapping("/search")
   public String search(String keyword, RedirectAttributes rttr) {
       rttr.addAttribute("keyword", keyword);
       return "redirect:/sample/results";
   }
   ```

---

## 📚 관련 개념

- **Forward vs Redirect**: 
  - Forward: 서버 내부에서 요청 전달 (URL 변경 없음)
  - Redirect: 브라우저에게 새 URL로 이동하라고 지시 (URL 변경)

- **PRG 패턴 (Post-Redirect-Get)**:
  - POST 요청 후 GET으로 리다이렉트
  - 브라우저 새로고침 시 중복 제출 방지

---

**작성일**: 2025-12-03  
**관련 파일**: `HelloController.java`의 `ex7()` 메서드

