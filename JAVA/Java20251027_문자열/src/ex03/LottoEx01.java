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

public class LottoEx01 {
    public static void main(String[] args) {
        // 로또 번호를 저장할 배열: 크기 6개
        int[] lotto = new int[6];

        // 프로그램 시작 헤더 출력
        System.out.println("--------------------------------");
        System.out.println("------로또 번호 추출 시작------");
        System.out.println("--------------------------------");
        
        // ========== 5게임 반복 실행 ==========
        for (int game = 0; game < 5; game++) {
            // ========== 각 게임당 6개의 로또 번호 생성 ==========
            for (int i = 0; i < lotto.length; i++) {
                // 중복이 없는 번호가 나올 때까지 반복 생성
                while (true) {
                    // Math.random(): 0.0 ~ 1.0 사이의 랜덤 값 생성
                    lotto[i] = (int) (Math.random() * 45) + 1;
                    
                    // 현재 생성된 번호가 기존 번호들과 중복되는지 검사
                    // 초기값: false (중복 없음)
                    boolean isDuplicate = false;
                    
                    // ========== 중복 검사 시작 ==========
                    // 현재 인덱스(i) 이전의 모든 번호들과 비교
                    for (int j = 0; j < i; j++) {
                        // 현재 생성된 번호 lotto[i]가 
                        // 이전에 생성된 번호 lotto[j]와 같은지 비교
                        if (lotto[i] == lotto[j]) {
                            isDuplicate = true; // 중복 발생!
                            break; // 더 이상 비교할 필요 없으므로 중단
                        }
                    }
                    // ========== 중복 검사 종료 ==========
                    
                    // 중복이 없으면 (isDuplicate == false)
                    // while 루프를 빠져나가서 다음 번호 생성으로 진행
                    if (!isDuplicate) break;
                    
                    // 만약 중복이 있으면 (isDuplicate == true)
                    // while 루프를 계속 돌아서 새로운 번호를 다시 생성
                }
            }
            // ========== 6개의 번호 생성 완료 ==========
            
            // 생성된 로또 번호를 오름차순으로 정렬
            Arrays.sort(lotto);
            
            // 게임 번호와 생성된 번호들을 출력
            System.out.print("게임 " + (game + 1) + " : ");
            System.out.println(Arrays.toString(lotto));
            System.out.println("--------------------------------");
        }
        // ========== 5게임 모두 완료 ==========
    }
}
