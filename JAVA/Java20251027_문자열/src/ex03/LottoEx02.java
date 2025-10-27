package ex03;
/*
 * 로또 번호 추출 프로그램 작성
 * 배열 : 6칸 짜리 int 배열 생성
 * 로또는 1~45 랜덤하게 저장
 * 단, 중복 불가
 * 출력은 오름차순으로 정렬하여 출력
 * 5게임을 반복하여 출력하는 프로그램 작성
 */
//20251027
import java.util.Arrays;

public class LottoEx02 {
    public static void main(String[] args) {
        int[] lotto = new int[6];
        
        System.out.println("--------------------------------");
        System.out.println("------로또 번호 추출 시작------");
        System.out.println("--------------------------------");
        
        // for문 1: 5게임 반복
        for (int game = 0; game < 5; game++) {
            // for문 2: 6개의 번호 생성
            for (int i = 0; i < lotto.length; i++) {
                // while문: 중복 없는 번호 생성 (무한 반복)
                while (true) {
                    lotto[i] = (int) (Math.random() * 45) + 1;
                    
                    // for문 4: 중복 검사
                    boolean isDuplicate = false;
                    for (int j = 0; j < i; j++) {
                        if (lotto[i] == lotto[j]) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) break;
                }
            }
            
            Arrays.sort(lotto);
            System.out.print("게임 " + (game + 1) + " : ");
            System.out.println(Arrays.toString(lotto));
            System.out.println("--------------------------------");
        }
    }
}
