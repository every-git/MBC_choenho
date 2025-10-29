package ex12;
    /*
 * 은행 계좌 객체인 Account 객체는 잔고(balance) 필드를 가지고 있습니다.
 * balance 필드는 음수값이 될 수 없고, 최대 백만원 까지 저장할 수 있습니다.
 * 외부에서 balance 필드를 마음대로 변경하지 못하고, 
 * 0<=balance<=1000000 범위의 값만 가질 수 있도록 Account 클래스에 적절한 생성자와 setter 메서드를 추가하세요.
 */
public class Account {
	
	private int balance;
	private static final int MIN_BALANCE = 0;
	private static final int MAX_BALANCE = 1000000;
	/* 
	// 기본 생성자 (잔고 0으로 초기화)
	public Account() {
		this.balance = MIN_BALANCE;
	}
	
	// 잔고를 받는 생성자
	public Account(int balance) {
		this.balance = balance;
	} */
	
	// balance getter
	public int getBalance() {
		return balance;
	}
	// balance setter (검증 로직 포함)
	public void setBalance(int balance) {
		/* if (balance < MIN_BALANCE) {
			this.balance = MIN_BALANCE;
			System.out.println("경고: 잔고는 음수값이 될 수 없습니다. 0원으로 설정됩니다.");
		} else if (balance > MAX_BALANCE) {
			this.balance = MAX_BALANCE;
			System.out.println("경고: 잔고는 최대 1,000,000원까지 가능합니다. 1,000,000원으로 설정됩니다.");
		} else {
			this.balance = balance;
		} */
        if (balance >= MIN_BALANCE && balance <= MAX_BALANCE) {
            this.balance = balance;
        }
	}
}
