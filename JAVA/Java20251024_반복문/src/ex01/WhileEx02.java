package ex01;
//20251024
public class WhileEx02 {
        // 1부터 100까지 짝수의 합만 구하는 코드를 while문을 사용해서 작성.        
    public static void main(String[] args) {

        int sum = 0; // 합계를 저장할 변수
        int i = 1; // 1부터 100까지 반복할 변수
        while (i <= 100) { // i가 100보다 작거나 같을 때
            if (i % 2 == 0) { // i가 짝수일 때
                sum += i; // 합계를 더한다.
            }
            i++; // i를 1씩 증가시킨다.
        }
        System.out.println("1부터 100까지의 짝수의 합 : " + sum); // 합계를 출력한다.
    }
}

