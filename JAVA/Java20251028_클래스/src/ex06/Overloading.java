package ex06;

public class Overloading {
    public static void main(String[] args) {
        func();
        func(10);
        func("홍길동");
        func(10, 20);
    }

    static void func() {
        System.out.println("-------------------");
    }
    static void func(int n) {
        System.out.println("---------" + n + "----------");
    }
    static void func(String s) {
        System.out.println("---------" + s + "----------");
    }
    static void func(int n1, int n2) {
        System.out.println("---------" + n1 + " + " + n2 + " = " + (n1 + n2) + "----------");
    }
}

// 오버로딩은 실제로 이런 경우에 사용된다.
// 웹페이지에서 검색어를 입력하면 검색어를 받아서 검색 결과를 출력하는 경우
// 이 경우에는 숫자, 문자, 숫자와 문자 등 다양한 타입의 데이터를 받을 수 있다.
// 이 경우에는 오버로딩을 사용하여 다양한 타입의 데이터를 받을 수 있도록 한다.
// 오버로딩은 함수명을 동일하게 하고, 매개변수의 타입과 개수를 다르게 하여 다양한 타입의 데이터를 받을 수 있도록 한다.
