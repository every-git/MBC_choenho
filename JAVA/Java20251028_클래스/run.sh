#!/bin/bash
cd "$(dirname "$0")"
javac -d bin src/ex01/ClassEx01.java
java -cp bin ex01.ClassEx01

