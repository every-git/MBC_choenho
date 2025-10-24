# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

Repository overview
- Purpose: Teaching materials for a digital academy curriculum. Includes static HTML/CSS examples and small Java console programs grouped by course/date.
- High-level structure:
  - HTML/: Topic-based HTML/CSS examples (pure static assets: .html, .css, images). Open in a browser or serve locally.
  - JAVA/: Date-named folders (e.g., Java20251023/, Java20251024_반복문/) containing src/ with simple packages like ex01 and main-class examples. Some folders include module-info.java; there is no Maven/Gradle project here.
- There is no centralized build system or automated tests in this repo.

Common commands

HTML/CSS (view examples)
- macOS: open a specific example in the default browser
```bash
open HTML/HTML_CSS_JS-main/index.html
```
- Linux: open in the default browser
```bash
xdg-open HTML/HTML_CSS_JS-main/index.html
```
- Serve a folder over HTTP (useful for relative paths/assets)
```bash
python3 -m http.server --directory HTML/HTML_CSS_JS-main 8000
# then visit http://localhost:8000
```

Java (compile/run examples)
- Quick classpath build/run for a non-modular folder (e.g., Java20251023)
```bash
# compile all sources to an out directory
mkdir -p JAVA/Java20251023/out
javac -d JAVA/Java20251023/out $(find JAVA/Java20251023/src -name "*.java")

# run a specific main class (change class name as needed)
java -cp JAVA/Java20251023/out ex01.If
```
- Folder with module-info.java (e.g., Java20251024_반복문)
  - Option A: Ignore modules (simpler for quick runs)
```bash
mkdir -p "JAVA/Java20251024_반복문/out"
javac -d "JAVA/Java20251024_반복문/out" $(find "JAVA/Java20251024_반복문/src" -name "*.java" ! -name "module-info.java")
java -cp "JAVA/Java20251024_반복문/out" ex01.ForEx01
```
  - Option B: Use Java modules
```bash
mkdir -p "JAVA/Java20251024_반복문/out"
javac --module-source-path "JAVA/Java20251024_반복문/src" -d "JAVA/Java20251024_반복문/out" $(find "JAVA/Java20251024_반복문/src" -name "*.java")
java --module-path "JAVA/Java20251024_반복문/out" -m Java20251024_반복문/ex01.ForEx01
```
- Clean compiled outputs
```bash
rm -rf JAVA/**/out
```

Linting, tests, and tooling
- No linters or formatters are configured in this repo.
- No unit/integration test framework or scripts are present. Running a “single test” is not applicable.

Key context from README.md
- The repository aggregates course materials across 14 subjects (total 1,140 hours). For day-to-day development here, the relevant parts are the HTML/CSS examples under HTML/ and Java basics under JAVA/.
- The curriculum mentions tools like JDK and build systems (Maven/Gradle), but this repository’s Java samples are standalone and not set up with those build tools.

Architecture notes (big picture)
- HTML examples are self-contained pages grouped by topic; asset paths are relative within each topic folder. There is no bundler or npm-based pipeline.
- Java examples are minimal console programs organized by lesson/date. Package naming is flat (e.g., ex01), making it straightforward to compile and run specific examples without a project system.
