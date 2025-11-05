package ex03;

public interface Action {
	
	public abstract void attack();
	void defend();

}

class Warrior implements Action{
	@Override
	public void attack(){
		System.out.println("Warrior 가 검으로 공격합니다.");
	}
	@Override
	public void defend(){
		System.out.println("Warrior 가 방패로 방어합니다.");
	}
}

class Archer implements Action{
	@Override
	public void attack(){
		System.out.println("Archer 가 활로 공격합니다.");
	}
	@Override
	public void defend(){
		System.out.println("Archer 가 물러나 방어합니다.");
	}
}