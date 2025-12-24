# Lombok 설치 및 설정 가이드

## 1. pom.xml 설정 완료
- ✅ Lombok 의존성이 `provided` scope로 설정되었습니다.
- ✅ Annotation Processing이 활성화되어 있습니다.

## 2. 이클립스 IDE에 Lombok 플러그인 설치

### 방법 1: 자동 설치 (권장)

1. 이클립스 Marketplace에서 설치:
   - `Help` → `Eclipse Marketplace...`
   - 검색창에 "Lombok" 입력
   - "Lombok" 플러그인 찾기
   - `Install` 클릭
   - 설치 완료 후 이클립스 재시작

### 방법 2: 수동 설치

1. Lombok JAR 다운로드:
   - https://projectlombok.org/download 에서 `lombok.jar` 다운로드

2. Lombok 설치 프로그램 실행:
   ```bash
   java -jar lombok.jar
   ```

3. 이클립스 실행 파일 선택:
   - 설치 프로그램에서 이클립스 실행 파일(`eclipse.app` 또는 `eclipse`)을 찾아 선택
   - `Install/Update` 클릭

4. 이클립스 재시작:
   - 설치 완료 후 이클립스 재시작

## 3. Annotation Processing 확인

1. 프로젝트 설정 확인:
   - 프로젝트 우클릭 → `Properties`
   - `Java Compiler` → `Annotation Processing`
   - `Enable annotation processing` 체크 확인

2. 전역 설정 확인:
   - `Window` → `Preferences`
   - `Java` → `Compiler` → `Annotation Processing`
   - `Enable annotation processing` 체크 확인

## 4. 프로젝트 업데이트

1. Maven 프로젝트 업데이트:
   - 프로젝트 우클릭 → `Maven` → `Update Project...`
   - `Force Update of Snapshots/Releases` 체크 (선택사항)
   - `OK` 클릭

2. 프로젝트 클린 빌드:
   - `Project` → `Clean...`
   - 프로젝트 선택 후 `Clean` 클릭

## 5. 설치 확인

Lombok이 제대로 설치되었는지 확인:
- 코드에서 `@Getter`, `@Setter`, `@Log4j2` 등의 어노테이션이 정상 작동하는지 확인
- 빌드 시 오류가 없는지 확인
- 생성된 getter/setter 메서드가 Outline 뷰에 표시되는지 확인

