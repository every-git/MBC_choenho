package Test;
//20251031 싱글톤 패턴
public class Singleton {
/*
 * 싱글톤 용어의 뜻은 하나의 인스턴스를 만들어서 공유하는 패턴
 */
    private static Singleton singleton = new Singleton();
    //외부에서 객체 생성 불가.
    private Singleton() {} 
    //생성된 Singleton 객체를 사용(참조)할 수 있는 유일한 통로.
    public static Singleton getInstance() {
        return singleton;
    }
}
// 정적 메소드, 정적 변수는 왜 다른 클래스에서 사용할 수 있는가?
// 정적 메소드, 정적 변수는 클래스 로딩 시 메모리에 할당되므로 다른 클래스에서 사용할 수 있다.
// 메모리에 올라가기 때문에 정적 메소드라고 한다.
class Sample {
    public Sample() {

    }
    // 객체 생성 없이 메소드를 호출할 수 없다.
    static void func() {
        System.out.println("Sample 클래스의 func 메소드 호출");
    }
    
}
