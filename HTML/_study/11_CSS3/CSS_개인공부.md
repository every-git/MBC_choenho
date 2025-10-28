# CSS3 개인 공부 노트

> **CSS** = Cascading Style Sheets (계단식 스타일 시트)  
> **HTML** = HyperText Markup Language (하이퍼텍스트 마크업 언어)

## 목차

### 🔰 CSS 기초 개념 (필수)
0. [CSS 기초 - 시작하기](#0-css-기초---시작하기)
1. [선택자(Selector)](#1-선택자selector)
2. [선택자 우선순위(Specificity)](#2-선택자-우선순위specificity)
3. [상속(Inheritance)](#3-상속inheritance)
4. [박스 모델 기초](#4-박스-모델-기초)

### 📦 CSS3 주요 기능
5. [Margin 음수값 활용](#5-margin-음수값-활용)
6. [Display 속성 - Block 요소](#6-display-속성---block-요소)
7. [Display 속성 - Inline 요소](#7-display-속성---inline-요소)
8. [Opacity (투명도)](#8-opacity-투명도)
9. [Box-sizing 속성](#9-box-sizing-속성)
10. [Box-shadow와 Text-shadow](#10-box-shadow와-text-shadow)
11. [RGBA 색상 표현](#11-rgba-색상-표현)
12. [Gradient (그라데이션)](#12-gradient-그라데이션)
13. [배경 이미지 제어](#13-배경-이미지-제어)
14. [멀티 배경 이미지](#14-멀티-배경-이미지)
15. [Hover 효과와 Transition](#15-hover-효과와-transition)
16. [네비게이션 메뉴 스타일링](#16-네비게이션-메뉴-스타일링)
17. [Background-size 속성](#17-background-size-속성)

---

## 0. CSS 기초 - 시작하기

### CSS란?

**CSS (Cascading Style Sheets)** = 계단식 스타일 시트
- **HTML** 문서의 **스타일과 레이아웃을 정의**하는 언어
- "Cascading" = 계단식/폭포수처럼 위에서 아래로 흐르듯 스타일이 적용됨 (우선순위에 따라)

#### CSS의 역할
- **디자인**: 색상, 폰트, 크기 등
- **레이아웃**: 요소의 배치와 정렬
- **반응형**: 다양한 화면 크기에 대응
- **애니메이션**: 동적 효과

### CSS 기본 문법

```css
선택자 {
  속성: 값;
  속성: 값;
}
```

**예제**
```css
h1 {
  color: blue;        /* color = 색상 */
  font-size: 24px;    /* font-size = 글자 크기, px = pixel(픽셀) */
}
```

### CSS 적용 방법 (3가지)

#### 1. 인라인 스타일 (Inline Style)
**HTML** 태그에 직접 `style` 속성으로 작성

```html
<p style="color: red; font-size: 16px;">빨간색 텍스트</p>
```

**장점**
- 빠르게 테스트 가능
- 특정 요소에만 즉시 적용

**단점**
- 재사용 불가
- 유지보수 어려움
- **HTML**과 **CSS**가 섞여 코드 가독성 저하
- 우선순위가 너무 높음

**사용 시기**: 임시 테스트, 긴급 수정

#### 2. 내부 스타일 (Internal/Embedded Style)
**HTML** 문서의 `<head>` 태그 안에 `<style>` 태그로 작성

```html
<!DOCTYPE html>
<html>
<head>
  <style>
    p {
      color: blue;
      font-size: 16px;
    }
    
    .highlight {  /* class = 클래스 */
      background-color: yellow;
    }
  </style>
</head>
<body>
  <p>파란색 텍스트</p>
  <p class="highlight">형광펜 효과</p>
</body>
</html>
```

**장점**
- **HTML** 파일 하나로 관리 가능
- 선택자를 사용해 여러 요소에 적용
- 파일 요청 횟수 감소 (외부 **CSS** 불필요)

**단점**
- 다른 페이지에서 재사용 불가
- 페이지가 많을 경우 중복 코드 발생

**사용 시기**: 단일 페이지, 특정 페이지만의 스타일

#### 3. 외부 스타일 (External Style Sheet) ⭐ 권장
별도의 `.css` 파일로 작성하고 **HTML**에서 링크

**style.css**
```css
p {
  color: green;
  font-size: 16px;
}

.highlight {
  background-color: yellow;
}
```

**index.html**
```html
<!DOCTYPE html>
<html>
<head>
  <!-- rel = relationship (관계), href = hypertext reference (참조) -->
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <p>초록색 텍스트</p>
  <p class="highlight">형광펜 효과</p>
</body>
</html>
```

**장점**
- 여러 페이지에서 재사용 가능 ⭐
- **HTML**과 **CSS** 분리로 유지보수 용이
- 캐싱(caching)으로 로딩 속도 향상
- 협업에 유리

**단점**
- 초기 파일 요청 필요
- 파일 관리 필요

**사용 시기**: 실무에서 기본적으로 사용 ⭐

### 3가지 방법 우선순위

```
인라인 스타일 > 내부 스타일 = 외부 스타일
(더 구체적인 선택자가 우선)
```

---

## 1. 선택자(Selector)

> **Selector** = 선택자 (선택하는 도구)

### 선택자란?
**스타일을 적용할 HTML 요소를 선택**하는 패턴

### 1.1 기본 선택자

#### 1) 전체 선택자 (Universal Selector)
모든 요소를 선택

```css
* {  /* * = 모든 것(asterisk) */
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
```

**활용**: **CSS** 초기화(Reset)

#### 2) 태그 선택자 (Type Selector 또는 Element Selector)
특정 **HTML** 태그를 모두 선택

```css
p {
  color: blue;
}

h1 {
  font-size: 32px;  /* px = pixel (픽셀) */
}

div {
  background-color: yellow;
}
```

#### 3) 클래스 선택자 (Class Selector) ⭐ 가장 많이 사용
`.클래스명`으로 선택 (여러 요소에 재사용 가능)

```html
<p class="highlight">형광펜 효과</p>
<div class="highlight">노란 배경</div>
```

```css
.highlight {  /* . = class를 의미 */
  background-color: yellow;
}

.text-red {
  color: red;
}
```

**특징**
- 재사용 가능
- 하나의 요소에 여러 클래스 적용 가능
```html
<p class="highlight text-red bold">여러 클래스</p>
```

#### 4) ID 선택자 (ID Selector)
`#아이디명`으로 선택 (페이지에 하나만 존재)

> **ID** = Identifier (식별자) - 고유한 이름

```html
<div id="header">헤더</div>
```

```css
#header {  /* # = id를 의미 */
  background-color: navy;
  color: white;
}
```

**특징**
- 페이지에서 유일해야 함
- 우선순위가 클래스보다 높음
- JavaScript에서 자주 사용

**클래스 vs ID**
```css
/* 클래스: 재사용 가능 */
.button { padding: 10px; }

/* ID: 유일한 요소 */
#main-header { height: 100px; }
```

#### 5) 그룹 선택자 (Group Selector)
여러 선택자에 같은 스타일 적용 (쉼표로 구분)

```css
h1, h2, h3 {
  font-family: "맑은 고딕";
  color: navy;
}

.title, .subtitle, #header {
  margin-bottom: 20px;
}
```

### 1.2 복합 선택자 (Combinator Selector)

> **Combinator** = 결합자 (여러 것을 결합하는 것)

#### 1) 자손 선택자 (Descendant Selector)
**공백**으로 구분, 모든 하위 요소 선택

> **Descendant** = 자손, 후손

```css
/* div 안의 모든 p (자식, 손자, 증손자 모두) */
div p {
  color: red;
}
```

```html
<div>
  <p>빨간색 ✅</p>
  <section>
    <p>빨간색 ✅ (손자도 적용)</p>
  </section>
</div>
```

#### 2) 자식 선택자 (Child Selector)
`>`로 구분, **직계 자식만** 선택

```css
/* div의 직접 자식 p만 */
div > p {
  color: blue;
}
```

```html
<div>
  <p>파란색 ✅</p>
  <section>
    <p>적용 안됨 ❌ (손자)</p>
  </section>
</div>
```

**자손 vs 자식 비교**
```css
/* 자손: 모든 하위 요소 */
.parent p { color: red; }

/* 자식: 직계만 */
.parent > p { color: blue; }
```

#### 3) 인접 형제 선택자 (Adjacent Sibling Selector)
`+`로 구분, **바로 다음** 형제 요소 하나만 선택

> **Adjacent** = 인접한, 바로 옆의  
> **Sibling** = 형제

```css
h1 + p {
  font-weight: bold;  /* weight = 무게, 굵기 */
}
```

```html
<h1>제목</h1>
<p>굵게 표시 ✅ (바로 다음)</p>
<p>적용 안됨 ❌</p>
```

#### 4) 일반 형제 선택자 (General Sibling Selector)
`~`로 구분, **이후 모든** 형제 요소 선택

> **General** = 일반적인, 전체의

```css
h1 ~ p {
  color: gray;
}
```

```html
<h1>제목</h1>
<p>회색 ✅</p>
<div>div</div>
<p>회색 ✅</p>
```

#### 5) 복합 선택 (Multiple Class/Tag Selector)
**공백 없이** 연결 - 동일한 요소에 여러 조건 적용

```css
/* p 태그이면서 highlight 클래스 */
p.highlight {
  background-color: yellow;
}

/* div이면서 box 클래스이면서 red 클래스 */
div.box.red {
  border: 2px solid red;
}
```

```html
<p class="highlight">노란 배경 ✅</p>
<div class="highlight">적용 안됨 ❌ (p가 아님)</div>

<div class="box red">빨간 테두리 ✅</div>
<div class="box">적용 안됨 ❌ (red 없음)</div>
```

### 1.3 가상 클래스 (Pseudo-class)

> **Pseudo** = 가짜의, 유사한 (실제 클래스는 아니지만 클래스처럼 동작)  
> **Pseudo-class** = 가상 클래스

요소의 **특정 상태**를 선택 (`:` 하나 사용)

#### 1) 링크/동적 가상 클래스

```css
/* 방문하지 않은 링크 */
a:link {
  color: blue;
}

/* 방문한 링크 */
a:visited {
  color: purple;
}

/* 마우스 올렸을 때 ⭐ 가장 많이 사용 */
a:hover {  /* hover = (새가) 공중에 떠 있다 */
  color: red;
  text-decoration: underline;
}

/* 클릭하는 순간 */
a:active {  /* active = 활성 상태 */
  color: orange;
}

/* 포커스 받았을 때 (키보드 탭) */
input:focus {  /* focus = 초점 */
  border: 2px solid blue;
  outline: none;  /* outline = 외곽선 */
}
```

**순서 중요! (LVHA 순서 기억)**
```css
a:link { }     /* L = Link */
a:visited { }  /* V = Visited */
a:hover { }    /* H = Hover */
a:active { }   /* A = Active */
```

#### 2) 구조 가상 클래스 (Structural Pseudo-class)

```css
/* 첫 번째 자식 */
li:first-child {
  font-weight: bold;
}

/* 마지막 자식 */
li:last-child {
  border-bottom: none;
}

/* n번째 자식 */
li:nth-child(2) {  /* nth = n번째 (n-th) */
  color: red;
}

/* 홀수 번째 (1, 3, 5...) */
li:nth-child(odd) {  /* odd = 홀수 */
  background-color: #f0f0f0;
}

/* 짝수 번째 (2, 4, 6...) */
li:nth-child(even) {  /* even = 짝수 */
  background-color: white;
}

/* 3n (3, 6, 9...) */
li:nth-child(3n) {
  color: blue;
}

/* 특정 타입의 n번째 */
p:nth-of-type(2) {  /* of-type = 특정 타입 중에서 */
  font-weight: bold;
}
```

**:nth-child vs :nth-of-type 차이**
```html
<div>
  <h2>제목</h2>
  <p>첫 번째 p</p>
  <p>두 번째 p</p>
</div>
```

```css
/* p:nth-child(1) - 적용 안됨 (첫 자식은 h2) */
/* p:nth-child(2) - 첫 번째 p 선택 */

/* p:nth-of-type(1) - 첫 번째 p 선택 (p 타입 중에서) */
/* p:nth-of-type(2) - 두 번째 p 선택 */
```

#### 3) 부정 가상 클래스 (Negation Pseudo-class)

> **Negation** = 부정

```css
/* active가 아닌 모든 li */
li:not(.active) {  /* not = ~이 아닌 */
  opacity: 0.5;
}

/* 첫 번째가 아닌 모든 p */
p:not(:first-child) {
  margin-top: 20px;
}
```

### 1.4 가상 요소 (Pseudo-element)

> **Pseudo-element** = 가상 요소 (실제 **HTML**에 없지만 있는 것처럼 선택)

요소의 **특정 부분**을 선택 (`::` 두 개 사용)

```css
/* 첫 글자 */
p::first-letter {
  font-size: 2em;  /* em = 상대 단위 */
  color: red;
}

/* 첫 줄 */
p::first-line {
  font-weight: bold;
}

/* 요소 앞에 내용 추가 */
.icon::before {  /* before = 앞에 */
  content: "★ ";  /* content = 내용 */
  color: gold;
}

/* 요소 뒤에 내용 추가 */
.label::after {  /* after = 뒤에 */
  content: " (필수)";
  color: red;
}

/* 선택된 텍스트 */
::selection {  /* selection = 선택 */
  background-color: yellow;
  color: black;
}
```

**실용 예제**
```css
/* 말줄임표 */
.text-ellipsis::after {  /* ellipsis = 줄임표(...) */
  content: "...";
}

/* 외부 링크 아이콘 추가 */
.external-link::after {  /* external = 외부의 */
  content: " ↗";
}

/* 클리어픽스 (Clearfix) */
.clearfix::after {  /* clear = 지우다, fix = 고정 */
  content: "";
  display: table;
  clear: both;
}
```

---

## 2. 선택자 우선순위(Specificity)

> **Specificity** = 명시도, 구체성 (얼마나 구체적인가)  
> **Priority** = 우선순위

### 우선순위란?
같은 요소에 **여러 CSS 규칙이 충돌할 때** 어떤 스타일을 적용할지 결정하는 규칙

### 우선순위 계산법

#### 점수 체계 (Scoring System)
```
!important     > 10000점 (절대 우선)
인라인 스타일    > 1000점
ID 선택자      > 100점
클래스/가상클래스/속성 > 10점
태그/가상요소   > 1점
전체 선택자(*)  > 0점
```

#### 계산 예제

```css
/* (0, 0, 0, 1) = 1점 */
p { color: black; }

/* (0, 0, 1, 0) = 10점 */
.text { color: red; }

/* (0, 0, 1, 1) = 11점 */
p.text { color: blue; }

/* (0, 1, 0, 0) = 100점 */
#header { color: green; }

/* (0, 1, 1, 1) = 111점 */
#header .text p { color: purple; }

/* (0, 0, 2, 1) = 21점 */
p.text.highlight { color: orange; }
```

### 우선순위 규칙

#### 규칙 1: 점수가 높은 것이 우선

```html
<p class="text" id="intro">텍스트</p>
```

```css
p { color: black; }           /* 1점 */
.text { color: red; }         /* 10점 ✅ 적용 */
```

```css
.text { color: red; }         /* 10점 */
#intro { color: blue; }       /* 100점 ✅ 적용 */
```

#### 규칙 2: 점수가 같으면 나중에 선언된 것이 우선 (Cascade)

> **Cascade** = 계단식, 폭포수 (위에서 아래로 흐름)

```css
.text { color: red; }
.text { color: blue; }        /* ✅ 적용 (나중에 선언) */
```

#### 규칙 3: 더 구체적인 선택자가 우선

```css
p { color: black; }           /* 1점 */
div p { color: red; }         /* 2점 */
div.container p { color: blue; } /* 12점 ✅ 적용 */
```

### !important (강제 우선순위)

> **important** = 중요한

**모든 우선순위를 무시**하고 최우선 적용

```css
p { color: red !important; }  /* ✅ 강제 적용 */

#intro { color: blue; }       /* ID여도 적용 안됨 */
```

```html
<p id="intro" style="color: green;">
  빨간색 출력 (인라인 스타일보다도 우선)
</p>
```

### !important 사용 시 주의사항

#### ❌ 나쁜 예 (유지보수 어려움)
```css
.button {
  color: blue !important;
  background: red !important;  /* 과도한 사용 */
}
```

#### ✅ 좋은 예 (정말 필요한 경우만)
```css
/* 외부 라이브러리 스타일 덮어쓰기 */
.bootstrap-button {
  color: red !important;  /* 부득이한 경우 */
}

/* 유틸리티 클래스 */
.hide {
  display: none !important;  /* 항상 숨김 */
}
```

### 우선순위 전체 순서

```
1. !important 선언
2. 인라인 스타일 (style="")
3. ID 선택자 (#header)
4. 클래스/속성/가상클래스 (.class, [type="text"], :hover)
5. 태그/가상요소 (p, ::before)
6. 전체 선택자 (*)
7. 상속된 스타일 (부모로부터)
```

---

## 3. 상속(Inheritance)

> **Inheritance** = 상속 (부모의 것을 물려받음)

### 상속이란?
**부모 요소의 CSS 속성이 자식 요소에게 자동으로 전달**되는 것

```html
<div style="color: blue;">
  <p>파란색 텍스트 (상속받음)</p>
  <span>파란색 텍스트 (상속받음)</span>
</div>
```

### 3.1 상속되는 속성 ✅

주로 **텍스트 관련 속성**이 상속됨

#### 폰트 관련
```css
font-family  /* family = 가족 (서체 집합) */
font-size    /* size = 크기 */
font-weight  /* weight = 무게, 굵기 */
font-style   /* style = 스타일 (기울임 등) */
line-height  /* line-height = 줄 높이 */
```

#### 텍스트 관련
```css
color              /* color = 색상 */
text-align         /* align = 정렬 */
text-indent        /* indent = 들여쓰기 */
text-transform     /* transform = 변환 (대소문자 등) */
letter-spacing     /* letter = 글자, spacing = 간격 */
word-spacing       /* word = 단어 */
white-space        /* white-space = 공백 처리 */
```

#### 리스트 관련
```css
list-style          /* list = 목록, style = 스타일 */
list-style-type     /* type = 유형 */
list-style-position /* position = 위치 */
```

#### 기타
```css
cursor      /* cursor = 커서 (마우스 포인터) */
visibility  /* visibility = 가시성 */
```

### 3.2 상속되지 않는 속성 ❌

주로 **레이아웃 관련 속성**은 상속되지 않음

> **Layout** = 배치, 레이아웃

#### 박스 모델 (Box Model)
```css
width, height       /* 너비, 높이 */
margin, padding     /* 여백 */
border             /* 테두리 */
```

#### 배경 (Background)
```css
background         /* background = 배경 */
background-color   /* color = 색상 */
background-image   /* image = 이미지 */
```

#### 위치/레이아웃 (Position/Layout)
```css
position   /* position = 위치 */
display    /* display = 표시 방식 */
float      /* float = 떠 있다 */
top, right, bottom, left  /* 상하좌우 위치 */
```

#### 기타
```css
overflow  /* overflow = 넘침 */
z-index   /* z-index = z축 순서 (깊이) */
opacity   /* opacity = 불투명도 (상속 안되지만 시각적 영향) */
```

### 3.3 강제 상속과 제어

#### inherit (강제 상속)

> **inherit** = 상속하다

상속되지 않는 속성도 부모 값을 **강제로 상속**

```css
.parent {
  border: 2px solid black;
}

.child {
  border: inherit;  /* 부모의 border 상속 */
}
```

#### initial (초기값)

> **initial** = 초기의, 처음의

브라우저 **기본값으로 리셋**

```css
.element {
  color: initial;  /* 브라우저 기본 색상 (보통 검정) */
}
```

#### unset (해제)

> **unset** = 해제하다

상속 가능하면 `inherit`, 아니면 `initial`

```css
.element {
  color: unset;  /* color는 상속 가능 → inherit */
  border: unset; /* border는 상속 불가 → initial */
}
```

#### revert (되돌리기)

> **revert** = 되돌리다

사용자/브라우저 스타일로 되돌림

```css
.element {
  all: revert;  /* 모든 스타일을 되돌림 */
}
```

### 3.4 all 속성

**모든 속성을 한 번에 제어**

```css
.reset {
  all: unset;     /* 모든 속성을 unset */
}

.inherit-all {
  all: inherit;   /* 모든 속성을 상속 */
}

.initial-all {
  all: initial;   /* 모든 속성을 초기화 */
}
```

---

## 4. 박스 모델 기초

> **Box Model** = 박스 모델 (상자 모형)

### 박스 모델이란?
모든 **HTML** 요소는 **사각형 박스**로 표현되며, 4개 영역으로 구성됩니다.

```
┌─────────────── margin (외부 여백) ──────────────┐
│ ┌─────────── border (테두리) ───────────────┐   │
│ │ ┌───────── padding (내부 여백) ────────┐  │   │
│ │ │                                     │  │   │
│ │ │       Content (내용)                │  │   │
│ │ │      (width × height)               │  │   │
│ │ │                                     │  │   │
│ │ └─────────────────────────────────────┘  │   │
│ └─────────────────────────────────────────┘   │
└───────────────────────────────────────────────┘
```

### 4.1 박스 모델 구성 요소

#### 1. Content (내용 영역)

> **Content** = 내용, 콘텐츠

- 실제 콘텐츠가 표시되는 영역
- `width`, `height`로 크기 설정

```css
.box {
  width: 200px;   /* width = 너비 */
  height: 100px;  /* height = 높이 */
}
```

#### 2. Padding (안쪽 여백)

> **Padding** = 패딩, 완충재, 채워 넣기

- 콘텐츠와 테두리 사이의 **내부 여백**
- 배경색이 **padding** 영역까지 표시됨
- **음수 값 불가능**

```css
/* 4방향 동일 */
padding: 20px;

/* 상하 / 좌우 */
padding: 10px 20px;

/* 상 / 좌우 / 하 */
padding: 10px 20px 30px;

/* 상 / 우 / 하 / 좌 (시계방향) */
padding: 10px 20px 30px 40px;

/* 개별 지정 */
padding-top: 10px;     /* top = 위 */
padding-right: 20px;   /* right = 오른쪽 */
padding-bottom: 30px;  /* bottom = 아래 */
padding-left: 40px;    /* left = 왼쪽 */
```

#### 3. Border (테두리)

> **Border** = 경계, 테두리

- 요소의 경계선
- 굵기, 스타일, 색상 지정

```css
/* 단축 속성 */
border: 2px solid black;   /* solid = 실선 */
border: 1px dashed red;    /* dashed = 파선 */

/* 개별 속성 */
border-width: 2px;         /* width = 너비, 굵기 */
border-style: solid;       /* style = 스타일 */
border-color: black;       /* color = 색상 */

/* 개별 방향 */
border-top: 1px solid red;
border-right: 2px dashed blue;
border-bottom: 3px dotted green;  /* dotted = 점선 */
border-left: 4px double orange;   /* double = 이중선 */

/* 모서리 둥글게 */
border-radius: 10px;  /* radius = 반지름 (둥근 정도) */
border-radius: 50%;   /* 원형 */
```

**Border 스타일 종류**
```css
border-style: solid;   /* solid = 실선 ─── */
border-style: dashed;  /* dashed = 파선 - - - */
border-style: dotted;  /* dotted = 점선 · · · */
border-style: double;  /* double = 이중선 ═══ */
border-style: groove;  /* groove = 홈 파인 효과 */
border-style: ridge;   /* ridge = 산등성이, 튀어나온 효과 */
border-style: inset;   /* inset = 안쪽으로, 들어간 효과 */
border-style: outset;  /* outset = 바깥쪽으로, 나온 효과 */
border-style: none;    /* none = 없음 */
```

#### 4. Margin (바깥 여백)

> **Margin** = 마진, 여백

- 요소와 다른 요소 사이의 **외부 여백**
- 배경색이 표시되지 않음 (투명)
- **음수 값 가능** (요소 겹치기)

```css
/* 사용법은 padding과 동일 */
margin: 20px;
margin: 10px 20px;
margin: 10px 20px 30px;
margin: 10px 20px 30px 40px;

/* 개별 지정 */
margin-top: 10px;
margin-right: 20px;
margin-bottom: 30px;
margin-left: 40px;

/* 중앙 정렬 */
margin: 0 auto;  /* auto = 자동 (좌우 자동 → 가운데) */

/* 음수 여백 */
margin: -10px;  /* 요소를 위로/왼쪽으로 당김 */
```

### 4.2 전체 크기 계산

#### content-box (기본값)

> **content-box** = 콘텐츠 박스 (내용 영역만 계산)

```
전체 너비 = width + padding-left + padding-right + border-left + border-right
전체 높이 = height + padding-top + padding-bottom + border-top + border-bottom
```

**예제**
```css
.box {
  width: 200px;
  height: 100px;
  padding: 20px;
  border: 5px solid black;
  margin: 10px;
}

/* 계산 */
/* 전체 너비 = 200 + 20×2 + 5×2 = 250px */
/* 전체 높이 = 100 + 20×2 + 5×2 = 150px */
/* margin은 외부 여백이므로 크기에 포함 안됨 */
```

#### border-box (권장) ⭐

> **border-box** = 테두리 박스 (테두리까지 포함해서 계산)

```
전체 너비 = width (padding, border 포함)
전체 높이 = height (padding, border 포함)
```

```css
.box {
  box-sizing: border-box;  /* box-sizing = 박스 크기 계산 방식 */
  width: 200px;
  height: 100px;
  padding: 20px;
  border: 5px solid black;
}

/* 전체 너비 = 200px (설정한 대로) */
/* 전체 높이 = 100px (설정한 대로) */
/* 콘텐츠 영역이 자동으로 줄어듦: 150px × 50px */
```

### 4.3 Margin 특수 동작

#### 1. Margin 중첩 (Margin Collapse)

> **Collapse** = 붕괴하다, 합쳐지다

**수직 방향**에서 두 **margin**이 만나면 **큰 값 하나만 적용**

```html
<div class="box1">Box 1</div>
<div class="box2">Box 2</div>
```

```css
.box1 {
  margin-bottom: 30px;
}

.box2 {
  margin-top: 20px;
}

/* 실제 간격: 50px (30+20)이 아니라 30px (큰 값만) */
```

**중첩이 일어나는 경우**
- 인접한 형제 요소
- 부모와 첫 번째/마지막 자식
- 빈 블록

**중첩을 방지하려면**
```css
/* 부모에 padding 또는 border 추가 */
.parent {
  padding: 1px;
}

/* 또는 overflow 사용 */
.parent {
  overflow: hidden;  /* overflow = 넘침 */
}

/* 또는 flexbox 사용 */
.parent {
  display: flex;  /* flex = 유연한 */
  flex-direction: column;  /* direction = 방향, column = 세로 */
}
```

#### 2. 수평 중앙 정렬

> **horizontal** = 수평의  
> **center** = 중앙  
> **alignment** = 정렬

```css
.center {
  width: 500px;
  margin: 0 auto;  /* 좌우 auto → 가운데 */
}
```

**주의**: **block** 요소이고 **width**가 지정되어야 함

#### 3. 음수 Margin

> **negative** = 음수의, 부정적인

```css
.overlap {  /* overlap = 겹치다 */
  margin-top: -20px;  /* 위로 20px 이동 (겹침) */
  margin-left: -10px; /* 왼쪽으로 10px 이동 */
}
```

---

## 5. Margin 음수값 활용

### 핵심 개념
- **Margin은 음수값을 가질 수 있지만, Padding은 음수값을 가질 수 없다**
- 음수 **margin**을 사용하면 요소를 겹치게 배치할 수 있다

### 예제 코드
```css
#box1 {
  width: 100px;
  height: 100px;
  background-color: gold;
}

#box2 {
  width: 100px;
  height: 100px;
  background-color: red;
  opacity: 0.5;  /* opacity = 불투명도 */
  margin: -50px 0 0 50px; /* 위로 -50px, 왼쪽으로 50px */
}
```

### Margin 값 개수에 따른 적용 순서
- **1개**: 전체 (상하좌우)
- **2개**: 상하, 좌우
- **3개**: 위, 좌우, 아래
- **4개**: 위, 오른쪽, 아래, 왼쪽 (시계방향)

### 활용 사례
- 요소 겹침 효과
- 레이아웃 미세 조정
- 타이포그래피 간격 조정

---

## 6. Display 속성 - Block 요소

> **Display** = 표시, 디스플레이 (화면에 보이는 방식)  
> **Block** = 블록, 덩어리

### Block 요소의 특징
1. **새로운 블록을 만든다** - 앞뒤로 줄바꿈 발생
2. **전체 너비를 차지한다** - **width**를 지정하지 않으면 100%
3. **width, height 지정 가능**
4. **margin, padding 모두 적용 가능**

### Block 요소 태그들
```html
<!-- 제목 태그 (heading) -->
<h1>, <h2>, <h3>, <h4>, <h5>, <h6>

<!-- 콘텐츠 그룹화 -->
<div>, <p>, <section>, <article>, <aside>, <header>, <footer>, <nav>
<!-- div = division (구역), p = paragraph (문단) -->
<!-- section = 섹션(구역), article = 기사 -->
<!-- aside = 사이드바, header = 헤더, footer = 푸터 -->
<!-- nav = navigation (내비게이션, 탐색 메뉴) -->

<!-- 리스트 (list) -->
<ul>, <ol>, <li>, <dl>, <dt>, <dd>
<!-- ul = unordered list (순서 없는 목록) -->
<!-- ol = ordered list (순서 있는 목록) -->
<!-- li = list item (목록 항목) -->
<!-- dl = description list (설명 목록) -->
<!-- dt = description term (설명 용어) -->
<!-- dd = description detail (설명 세부사항) -->

<!-- 테이블 (table) -->
<table>, <tr>, <th>, <td>
<!-- table = 표, tr = table row (행) -->
<!-- th = table header (제목 셀), td = table data (데이터 셀) -->

<!-- 기타 -->
<hr>, <form>, <fieldset>
<!-- hr = horizontal rule (수평선) -->
<!-- form = 폼(양식), fieldset = 필드 세트(그룹) -->
```

### 스타일링 예제
```css
h1 { background-color: red; }
h2 { background-color: orange; }
div {
  background-color: gold;
  width: 100px;
  height: 100px;
  margin: 10px;
}
```

### 주의사항
- **Block** 요소는 기본적으로 세로로 쌓인다
- **width**를 설정하더라도 다음 요소는 아래로 내려간다

---

## 7. Display 속성 - Inline 요소

> **Inline** = 인라인 (줄 안에, 한 줄에)

### Inline 요소의 특징
1. **줄바꿈이 생기지 않는다** - 옆으로 나란히 배치
2. **콘텐츠 크기만큼만 영역을 차지한다**
3. **width, height 지정 불가** (무시됨)
4. **좌우 margin, padding만 적용** (상하는 적용되지만 레이아웃에 영향 없음)

### Inline 요소 태그들
```html
<!-- 텍스트 스타일 -->
<span>, <a>, <strong>, <em>, <b>, <i>, <u>
<!-- span = 범위, a = anchor (링크) -->
<!-- strong = 강조(굵게), em = emphasis (강조, 기울임) -->
<!-- b = bold (굵게), i = italic (기울임), u = underline (밑줄) -->

<!-- 위첨자/아래첨자 -->
<sub>, <sup>
<!-- sub = subscript (아래첨자), sup = superscript (위첨자) -->

<!-- 폼 요소 (form element - 대부분) -->
<input>, <select>, <button>, <label>
<!-- input = 입력, select = 선택 -->
<!-- button = 버튼, label = 레이블(설명) -->

<!-- 이미지 -->
<img>
<!-- img = image (이미지) -->
```

### 예제
```html
<span>오늘은 수요일</span>
<sub>내일은 목요일</sub>
<sup>어제는 화요일</sup>
<!-- 위 요소들은 모두 한 줄에 표시됨 -->
```

### Fieldset의 특별한 경우
```html
<!-- fieldset은 주로 <input>, <textarea>, <select> 등 form 관련 요소들을 그룹화할 때 사용 -->
<!-- 
     * fieldset 자체는 block 요소로 동작하므로, 내부에 inline 요소나 폼 요소를 넣어도 그룹 전체가 블록처럼 줄바꿈됨
     * legend 태그는 fieldset의 제목을 표현할 때 사용 (기본적으로 테두리와 함께 상단에 제목이 표시됨)
     * 아래 예시에서는 textarea도 block 요소이기 때문에 단독으로 써도 줄바꿈이 되지만, 
       보통 여러 개의 폼 컨트롤과 함께 fieldset으로 묶어 폼의 구역을 나눈다 
-->
<!-- textarea = text area (텍스트 영역, 여러 줄 입력) -->
<!-- placeholder = 자리 표시자 (입력 전 힌트 텍스트) -->

<fieldset>
  <legend>이메일 입력란</legend>
  <label for="email">이메일 주소:</label>
  <input type="email" id="email" name="email" placeholder="example@email.com"><br>
  <label for="message">내용:</label>
  <textarea id="message" rows="5" cols="30" placeholder="내용을 입력하세요"></textarea>
  <!-- rows = 행 수, cols = columns (열 수) -->
</fieldset>
```

---

## 8. Opacity (투명도)

> **Opacity** = 불투명도 (0=투명, 1=불투명)

### 기본 개념
- **opacity**: 요소의 투명도를 설정 (0.0 ~ 1.0)
- **0**: 완전 투명 (**transparent**)
- **1**: 완전 불투명 (**opaque**)

### 문법
```css
div {
  opacity: 0.5; /* 50% 투명 (50% 불투명) */
}
```

### 예제
```css
div.a { opacity: 1; }    /* 100% 불투명 */
div.b { opacity: 0.5; }  /* 50% 투명 */
div.c { opacity: 0.2; }  /* 20% 불투명, 80% 투명 */
```

### 주의사항
1. **자식 요소도 함께 투명해진다**
   ```css
   .parent {
     opacity: 0.5;
   }
   /* .parent 안의 모든 자식도 0.5 투명도 적용 */
   ```

2. **배경만 투명하게 하려면 rgba 사용**
   ```css
   /* RGBA = Red Green Blue Alpha (빨강 초록 파랑 투명도) */
   background-color: rgba(255, 0, 0, 0.5); /* 배경만 투명 */
   ```

### 활용 사례
- 이미지 **hover** 효과
- 오버레이(overlay) 효과
- 모달(modal) 배경
- 버튼 비활성화 표현

---

## 9. Box-sizing 속성

> **Box-sizing** = 박스 크기 계산 방식  
> **sizing** = 크기 지정

### 핵심 개념
박스 모델의 크기 계산 방식을 결정하는 속성

### Box-sizing 값

#### 1. content-box (기본값)
- **width/height** = 콘텐츠 영역만 포함
- 전체 크기 = **width** + **padding** + **border**

```css
.box {
  width: 100px;
  height: 100px;
  padding: 20px;
  border: 10px solid black;
  box-sizing: content-box;
}
/* 실제 렌더링 크기: 160px × 160px */
/* (100 + 20×2 + 10×2) */
```

#### 2. border-box (권장) ⭐
- **width/height** = 콘텐츠 + **padding** + **border** 포함
- 지정한 크기가 실제 렌더링 크기

> **render** = 렌더링 (화면에 그리기)

```css
.box {
  width: 100px;
  height: 100px;
  padding: 20px;
  border: 10px solid black;
  box-sizing: border-box;
}
/* 실제 렌더링 크기: 100px × 100px */
/* 콘텐츠 영역이 자동으로 줄어듦 */
```

### 전역 설정 권장사항

> **global** = 전역, 전체의

```css
* {
  box-sizing: border-box;
}
/* 모든 요소에 border-box 적용 - 크기 계산이 직관적 */
```

### 장점
- 레이아웃 계산이 쉬워진다
- 반응형 디자인에 유리

> **responsive** = 반응형 (화면 크기에 반응하는)

- **padding**/**border** 변경 시 레이아웃 안정적

---

## 10. Box-shadow와 Text-shadow

> **shadow** = 그림자

### Box-shadow (박스 그림자)

#### 기본 문법
```css
/* offset = 오프셋, 이동 거리 */
/* blur = 흐림, 번짐 */
/* radius = 반지름, 범위 */
box-shadow: offset-x offset-y blur-radius color;
box-shadow: 수평이동 수직이동 흐림정도 색상;
```

#### 예제
```css
/* 기본 그림자 */
.sbox1 {
  box-shadow: 4px 4px 4px #eee;
  width: 100px;
  padding: 20px;
  border: 1px solid #ddd;
}

/* RGBA로 투명도 조절 */
.sbox2 {
  box-shadow: 5px 5px 5px rgba(68, 68, 68, 0.1);
}
```

#### 고급 문법
```css
/* spread = 확산, 퍼짐 */
/* inset = 내부의 (안쪽 그림자) */
box-shadow: offset-x offset-y blur-radius spread-radius color inset;

/* spread-radius: 그림자 확산 정도 */
.box {
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2),
              0 6px 20px 0 rgba(0, 0, 0, 0.19);
  /* 쉼표로 구분하여 여러 그림자 적용 가능 */
}

/* inset: 내부 그림자 */
.inner-shadow {
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.5);
}
```

### Text-shadow (텍스트 그림자)

#### 기본 문법
```css
text-shadow: offset-x offset-y blur-radius color;
```

#### 예제
```css
.tbox1 {
  text-shadow: 4px 4px 4px #000;
}

.tbox2 {
  text-shadow: 5px 5px 5px rgba(255, 55, 55, 1);
}
```

---

## 11. RGBA 색상 표현

> **RGBA** = Red Green Blue Alpha (빨강 초록 파랑 투명도)  
> **RGB** = Red Green Blue (빨강 초록 파랑)  
> **Alpha** = 알파 (투명도 채널)

### 기본 개념
**RGBA** = Red, Green, Blue, Alpha (투명도)

### 문법
```css
rgba(red, green, blue, alpha)
```

- **red, green, blue**: 0~255
- **alpha**: 0.0~1.0 (0=완전투명, 1=완전불투명)

### 예제
```css
#p1 { background-color: rgba(255, 0, 0, 0.3); }    /* 빨강 30% */
#p2 { background-color: rgba(0, 255, 0, 0.3); }    /* 초록 30% */
#p3 { background-color: rgba(0, 0, 255, 0.3); }    /* 파랑 30% */
#p4 { background-color: rgba(192, 192, 192, 0.3); } /* 회색 30% */
#p5 { background-color: rgba(255, 255, 0, 0.3); }   /* 노랑 30% */
#p6 { background-color: rgba(255, 0, 255, 0.3); }   /* 자홍 30% */
```

### RGBA vs Opacity 차이

#### RGBA
```css
.element {
  background-color: rgba(255, 0, 0, 0.5);
  color: black;
}
/* 배경만 투명, 텍스트는 불투명 */
```

#### Opacity
```css
.element {
  background-color: red;
  color: black;
  opacity: 0.5;
}
/* 배경과 텍스트 모두 투명 */
```

---

## 12. Gradient (그라데이션)

> **Gradient** = 그라데이션, 점진적 변화 (색상이 서서히 변함)

### Linear Gradient (선형 그라데이션)

> **Linear** = 선형의, 직선의

#### 기본 문법
```css
/* direction = 방향 */
background-image: linear-gradient(direction, color1, color2, ...);
```

#### 예제 1: 위에서 아래로
```css
#grad1 {
  height: 200px;
  background-color: red; /* 그라데이션 미지원 브라우저용 폴백 */
  background-image: linear-gradient(red, yellow);
}
/* fallback = 폴백 (대체 수단) */
```

#### 방향 지정
```css
/* 각도로 지정 (degree = 각도) */
background-image: linear-gradient(45deg, red, blue);

/* 키워드로 지정 */
background-image: linear-gradient(to right, red, blue);
background-image: linear-gradient(to bottom right, red, blue);
```

### Radial Gradient (방사형 그라데이션)

> **Radial** = 방사형, 원형 (중심에서 바깥으로)

```css
background-image: radial-gradient(circle, red, yellow, green);

/* 위치 지정 */
background-image: radial-gradient(circle at top left, red, yellow);
```

---

## 13. 배경 이미지 제어

> **Background** = 배경

### Background 관련 속성

#### 1. background-image
```css
.element {
  background-image: url(img/green.png);
  /* url = Uniform Resource Locator (자원 위치 지정자, 주소) */
}
```

#### 2. background-position (위치)

> **position** = 위치

```css
/* 키워드 */
background-position: left top;
background-position: center center;
background-position: right bottom;

/* 백분율 (percentage) */
background-position: 50% 50%; /* 가로 세로 중앙 */

/* 픽셀 */
background-position: 10px 20px;
```

#### 3. background-repeat (반복)

> **repeat** = 반복하다

```css
background-repeat: repeat;    /* 가로세로 반복 (기본) */
background-repeat: repeat-x;  /* x축(가로)만 반복 */
background-repeat: repeat-y;  /* y축(세로)만 반복 */
background-repeat: no-repeat; /* 반복 없음 */
```

### 단축 속성

> **shorthand** = 단축, 줄임

```css
/* 개별 속성 */
background-image: url(image.jpg);
background-position: center center;
background-repeat: no-repeat;

/* 단축 속성 */
background: url(image.jpg) center center no-repeat;
```

---

## 14. 멀티 배경 이미지

> **Multi** = 다중의, 여러 개의  
> **Multiple** = 복수의

### 기본 개념
**CSS3**에서는 하나의 요소에 **여러 개의 배경 이미지**를 적용할 수 있다.

### 문법
```css
.element {
  /* 쉼표로 구분하여 여러 이미지 지정 */
  background-image: url(image1.png), url(image2.png), url(image3.png);
  background-position: top left, center center, bottom right;
  background-repeat: no-repeat, repeat, no-repeat;
}
```

### 레이어 순서

> **Layer** = 레이어, 층

```css
.element {
  /* 첫 번째가 가장 위 레이어 */
  background-image: 
    url(top-layer.png),    /* 맨 위 */
    url(middle-layer.png), /* 중간 */
    url(bottom-layer.png); /* 맨 아래 */
}
```

---

## 15. Hover 효과와 Transition

> **Transition** = 전환, 변화 (한 상태에서 다른 상태로)

### Transition (전환 효과)

#### 기본 문법
```css
/* property = 속성 */
/* duration = 지속 시간 */
/* timing-function = 타이밍 함수 (변화 속도 곡선) */
/* delay = 지연 (시작 전 대기 시간) */
transition: property duration timing-function delay;
```

- **property**: 효과를 적용할 속성 (**all**, **opacity**, **transform** 등)
- **duration**: 지속 시간 (0.3s, 500ms 등)
  - **s** = seconds (초)
  - **ms** = milliseconds (밀리초, 1/1000초)
- **timing-function**: 타이밍 함수 (**ease**, **linear**, **ease-in-out** 등)
- **delay**: 지연 시간

### 버튼 효과

#### 1. Fade In/Out

> **Fade** = 서서히 나타나거나 사라지다

```css
.btn1 {
  background-color: #f4511e;
  color: white;
  padding: 16px 32px;
  opacity: 0.6;
  transition: 0.3s;
  border: 0;
}

.btn1:hover {
  opacity: 1;
}
```

### Transform 효과

> **Transform** = 변형하다, 변환하다

#### Scale (크기 변환)

> **Scale** = 크기, 비율 (크기 변화)

```css
.apple1:hover {
  cursor: move;
  transform: scale(1.2); /* 120% 확대 */
}
```

### 타이밍 함수

> **ease** = 편안한, 부드러운  
> **linear** = 선형의, 일직선의  
> **cubic-bezier** = 3차 베지어 곡선 (사용자 정의 곡선)

```css
/* ease: 기본값, 시작과 끝이 부드러움 */
transition: all 0.3s ease;

/* linear: 일정한 속도 */
transition: all 0.3s linear;

/* ease-in: 천천히 시작 */
transition: all 0.3s ease-in;

/* ease-out: 천천히 끝 */
transition: all 0.3s ease-out;

/* ease-in-out: 시작과 끝 모두 천천히 */
transition: all 0.3s ease-in-out;

/* cubic-bezier: 사용자 정의 */
transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
```

---

## 16. 네비게이션 메뉴 스타일링

> **Navigation** = 내비게이션, 탐색 (사이트 이동 메뉴)

### 기본 수평 메뉴 구조

> **horizontal** = 수평의

#### HTML 구조
```html
<ul>
  <li><a href="#home">Home</a></li>
  <li><a href="#news">News</a></li>
  <li><a href="#contact">Contact</a></li>
  <li><a href="#about">About</a></li>
</ul>
```

### 1. 다크 테마 메뉴

> **theme** = 테마, 주제

```css
ul {
  list-style-type: none; /* 리스트 마커 제거 */
  margin: 0;
  padding: 0;
  overflow: hidden;  /* overflow = 넘침 */
  background-color: #333; /* 어두운 배경 */
}

li {
  float: left; /* 가로 정렬 - float = 떠 있다 */
}

li a {
  display: block; /* 클릭 영역 확대 */
  color: white;
  text-align: center;  /* align = 정렬 */
  padding: 14px 16px;
  text-decoration: none; /* 밑줄 제거 - decoration = 장식 */
}

li a:hover {
  background-color: #111; /* hover 시 더 어둡게 */
}
```

### 2. 라이트 테마 메뉴
```css
/* active가 아닌 것만 hover 효과 */
li a:hover:not(.active) {
  background-color: #ddd;
}

/* active 메뉴 스타일 */
li a.active {  /* active = 활성화된 */
  color: white;
  background-color: #4CAF50; /* 강조 색상 */
}
```

### 3. 드롭다운 메뉴

> **Dropdown** = 드롭다운 (아래로 펼쳐지는)

```css
/* 서브메뉴 (기본 숨김) */
ul li ul {
  position: absolute; /* 절대 위치 - absolute = 절대적인 */
  width: 200px;
  display: none; /* 기본 숨김 */
}

/* hover 시 서브메뉴 표시 */
ul li:hover ul {
  display: block;
}
```

---

## 17. Background-size 속성

> **size** = 크기

### 기본 개념
배경 이미지의 크기를 제어하는 **CSS3** 속성

### 주요 값

#### 1. contain

> **contain** = 포함하다, 담다 (이미지 전체가 보이도록)

- **이미지 전체가 보이도록** 크기 조정
- 요소보다 작으면 확대, 크면 축소
- **비율 유지**
- 빈 공간이 생길 수 있음

```css
.div1 {
  background: url(img/img_flwr.gif);
  background-repeat: no-repeat;
  background-size: contain;
}
```

#### 2. cover

> **cover** = 덮다, 가리다 (요소를 완전히 덮도록)

- **요소를 완전히 덮도록** 크기 조정
- **비율 유지**
- 이미지 일부가 잘릴 수 있음
- 배경이 빈 공간 없이 가득 참

```css
.div2 {
  background: url(img/img_flwr.gif);
  background-repeat: no-repeat;
  background-size: cover;
}
```

### 비교표

| 속성 | 이미지 전체 표시 | 요소 전체 덮음 | 비율 유지 | 잘림 가능 |
|------|----------------|--------------|---------|---------|
| **contain** | ✅ | ❌ | ✅ | ❌ |
| **cover** | ❌ | ✅ | ✅ | ✅ |
| 기본값 | 부분 | ❌ | ✅ | ❌ |

### 실용 예제

#### 1. 풀스크린 배경

> **fullscreen** = 전체 화면

```css
body {
  background: url(hero-image.jpg) no-repeat center center;
  background-size: cover;
  background-attachment: fixed; /* 스크롤 시 고정 */
  /* attachment = 부착, 고정 */
  /* fixed = 고정된 */
}
```

#### 2. 썸네일 이미지

> **thumbnail** = 썸네일, 작은 미리보기 이미지

```css
.thumbnail {
  width: 200px;
  height: 200px;
  background: url(image.jpg) center center no-repeat;
  background-size: cover;
  border-radius: 8px;
}
```

---

## 추가 학습 자료

### CSS3 주요 기능 정리

#### 1. 선택자 (Selectors)
- `:hover`, `:focus`, `:active`
- `:nth-child()`, `:nth-of-type()`
- `:not()`, `:first-child`, `:last-child`

#### 2. 박스 모델 (Box Model)
- `box-sizing: border-box`
- `margin`, `padding`, `border`
- `width`, `height`

#### 3. 레이아웃 (Layout)
- `display: flex`, `display: grid`
  - **flex** = flexible (유연한)
  - **grid** = 그리드 (격자)
- `position: relative/absolute/fixed/sticky`
  - **relative** = 상대적인
  - **absolute** = 절대적인
  - **fixed** = 고정된
  - **sticky** = 끈적끈적한 (스크롤 시 고정)
- `float`, `clear`

#### 4. 색상 & 배경 (Colors & Backgrounds)
- `rgba()`, `hsla()`
  - **HSL** = Hue Saturation Lightness (색상 채도 명도)
  - **HSLA** = HSL + Alpha (투명도)
- `linear-gradient()`, `radial-gradient()`
- `background-size: cover/contain`
- 멀티 배경 이미지

#### 5. 효과 (Effects)
- `opacity`
- `box-shadow`, `text-shadow`
- `border-radius`
- `transform: scale/rotate/translate`
  - **rotate** = 회전하다
  - **translate** = 이동하다
- `transition`

#### 6. 타이포그래피 (Typography)

> **Typography** = 타이포그래피 (글꼴 및 문자 배치 기술)

- `@font-face`
- `text-shadow`
- `text-overflow: ellipsis`

### 실전 팁

#### 1. 브라우저 호환성

> **vendor prefix** = 벤더 프리픽스 (제조사별 접두사)  
> **compatibility** = 호환성

```css
/* 벤더 프리픽스 */
-webkit-transform: scale(1.2);  /* webkit = 웹킷 (크롬, 사파리) */
-moz-transform: scale(1.2);     /* moz = 모질라 (파이어폭스) */
-ms-transform: scale(1.2);      /* ms = 마이크로소프트 (IE, 엣지) */
-o-transform: scale(1.2);       /* o = 오페라 */
transform: scale(1.2);
```

#### 2. 성능 최적화

> **GPU** = Graphics Processing Unit (그래픽 처리 장치)  
> **acceleration** = 가속

```css
/* GPU 가속 활용 */
transform: translateZ(0);  /* Z축 이동 (3D 효과 활성화) */
will-change: transform;    /* will-change = 변경될 것임 (미리 알림) */
```

#### 3. 접근성 고려

> **accessibility** = 접근성 (장애인도 사용 가능하도록)  
> **outline** = 외곽선

```css
/* 키보드 포커스 표시 */
a:focus {
  outline: 2px solid blue;
  outline-offset: 2px;  /* offset = 간격 */
}

/* 고대비 모드 */
/* prefers-contrast = 대비 선호 */
/* high = 높은 */
@media (prefers-contrast: high) {
  button {
    border: 2px solid currentColor;
    /* currentColor = 현재 텍스트 색상 */
  }
}
```

---

## 연습 프로젝트 아이디어

### 1. 카드 컴포넌트

> **component** = 컴포넌트 (구성 요소)

- **box-shadow** 활용
- **hover** 시 **transform scale**
- **transition** 효과

### 2. 네비게이션 바
- 수평 메뉴
- **hover** 효과
- 드롭다운 서브메뉴

### 3. 이미지 갤러리

> **gallery** = 갤러리, 전시관

- **grid** 레이아웃
- **hover** 시 **opacity** 효과
- 멀티 배경 활용

### 4. 버튼 라이브러리

> **library** = 라이브러리 (재사용 가능한 코드 모음)

- 다양한 **hover** 효과
- **gradient** 배경
- **box-shadow** 활용

### 5. 풀스크린 히어로 섹션

> **hero section** = 히어로 섹션 (메인 배너, 첫 화면)

- `background-size: cover`
- 그라데이션 오버레이
- 중앙 정렬 텍스트

---

## 마무리

이 문서는 **CSS3**의 주요 기능들을 실습 예제와 함께 정리한 학습 자료입니다.

### 학습 순서 추천
1. ✅ **CSS** 기초 개념 (선택자, 우선순위, 상속, 박스 모델)
2. ✅ **Display** 속성 이해 (**Block**, **Inline**)
3. ✅ 박스 모델 완전 정복 (**box-sizing**)
4. ✅ 색상과 투명도 (**RGBA**, **Opacity**)
5. ✅ 배경 이미지 제어
6. ✅ 그림자 효과 (**Box-shadow**, **Text-shadow**)
7. ✅ **Hover** & **Transition** 효과
8. ✅ 실전 네비게이션 메뉴

### 다음 단계
- **Flexbox** 레이아웃 학습
- **Grid** 레이아웃 학습
- **Animation** (@keyframes) 학습
  - **@keyframes** = 키 프레임 (애니메이션 단계 정의)
- **반응형 디자인** (**Media Queries**)
  - **Media Query** = 미디어 쿼리 (화면 크기별 스타일)
- **CSS 변수** (**Custom Properties**)
  - **Custom Properties** = 사용자 정의 속성 (변수)

### 참고 자료
- [MDN Web Docs - CSS](https://developer.mozilla.org/ko/docs/Web/CSS)
  - **MDN** = Mozilla Developer Network (모질라 개발자 네트워크)
- [CSS-Tricks](https://css-tricks.com/)
- [Can I Use](https://caniuse.com/) - 브라우저 호환성 확인

---

**작성일**: 2025-10-28  
**버전**: 1.0  
**학습 완료**: ⬜ (완료 시 체크)
