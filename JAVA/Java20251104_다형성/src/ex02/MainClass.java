package ex02;

abstract class Player{ //추상클래스는 다형성을 안전하게 사용하기 위해 쓰인다.
	abstract void play(int pos); //추상메소드
	abstract void stop(); //추상메소드

	void func(){
		System.out.println("--------------------------------");
	}
}
//추상클래스를 상속받은 클래스는 추상메소드를 반드시 구현해야 한다.
class AudioPlayer extends Player{
	@Override
	void play(int pos){
		System.out.println("AudioPlayer 가 " + pos + "번 곡을 재생합니다.");
	}
	@Override
	void stop(){
		System.out.println("AudioPlayer 가 멈춥니다.");
	}
}

class CdPlayer extends Player{
	@Override
	void play(int pos){
		System.out.println("CdPlayer 가 " + pos + "번 곡을 재생합니다.");
	}
	@Override
	void stop(){
		System.out.println("CdPlayer 가 멈춥니다.");
	}
}

public class MainClass {

	public static void main(String[] args) {
		System.out.println("--------------------------------");
		Player p = new AudioPlayer();
		p.play(10);
		p.stop();
		p.func();

		p = new CdPlayer();
		p.play(20);
		p.stop();
		p.func();

	}

}
