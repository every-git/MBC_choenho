package ex01;

//20251024
public class If8 {

    public static void main(String[] args) {
        /*
         * 난수를 이용해서 
         * 정수값 1~45값이 출력되게 
         * 이를 이용해서 로또 번호 추출 프로그램 작성
         */
       int[] lotto = new int[6];
       for (int i = 0; i < 6; i++) {
        lotto[i] = (int)(Math.random() * 45) + 1;
        System.out.print(lotto[i] + " ");
       }
       System.out.println();
       System.out.println("--------로또 번호 추출 완료--------");
    }
}
