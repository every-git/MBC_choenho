package ex03;

/*
 * 로또 번호 추출 프로그램 작성
 * 배열 : 6칸 짜리 int 배열 생성
 * 로또는 1~45 랜덤하게 저장
 * 단, 중복 불가
 * 출력은 오름차순으로 정렬하여 출력
 * 5게임을 반복하여 출력하는 프로그램 작성
 */
//20251028
import java.util.Arrays;

public class LottoEx04 {
    public static void main(String[] args) {
        int[] lotto = new int[6];
        for (int game = 0; game < 5; game++) {
            System.out.println("게임 " + (game + 1) + " : ");
            for (int i = 0; i < lotto.length; i++) {
                int tmp = (int) (Math.random() * 45) + 1;
                lotto[i] = tmp; // 랜덤한 번호를 배열에 저장
                // 중복 체크
                for (int j = 0; j < i; j++) { // 현재 인덱스 i 이전의 모든 번호들과 비교
                    if (lotto[i] == lotto[j]) {
                        i--; // 중복이 있으면 현재 인덱스 i를 감소시켜 다시 시도
                        break; // 중복이 있으면 더 이상 비교할 필요 없으므로 중단
                    }
                } 
            }
            Arrays.sort(lotto); // 배열을 오름차순으로 정렬
            System.out.println(Arrays.toString(lotto));
        }
    }
}
