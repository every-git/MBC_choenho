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

public class LottoEx03 {
    public static void main(String[] args) {
        // ========== 방법 2: 셔플(섞기) 방식 ==========
        // 1부터 45까지의 숫자를 배열에 저장한 후 섞어서 앞에서 6개를 추출
        
        // 로또 번호를 저장할 배열: 크기 45개
        // 인덱스 0 ~ 44에 1 ~ 45 숫자를 저장
        int[] lotto = new int[45];
        
        // ========== 1단계: 1부터 45까지의 숫자를 배열에 저장 ==========
        for (int i = 0; i < 45; i++) {
            // lotto[0] = 1, lotto[1] = 2, ..., lotto[44] = 45
            // 인덱스 i에 i+1 값 저장 (0→1, 1→2, ..., 44→45)
            lotto[i] = i + 1;
        }
        
        // ========== 2단계: 배열 섞기 (로또 번호 랜덤화) ==========
        // 6번 반복하여 배열의 값들을 무작위로 섞음
        for (int i = 0; i < 6; i++) {
            // 랜덤한 인덱스 선택 (0 ~ 44 범위)
            int index = (int) (Math.random() * 45);
            
            // swap 알고리즘: 두 값을 서로 바꾸기
            // 임시 변수 tmp에 lotto[i] 값을 저장.
            int tmp = lotto[i];
            // lotto[i]에 lotto[index] 값을 저장
            lotto[i] = lotto[index];
            // lotto[index]에 원래 lotto[i] 값 (tmp에 저장된 값) 저장
            lotto[index] = tmp;
        }
        
        // ========== 3단계: 섞인 배열의 앞에서 6개를 출력 ==========
        // 배열의 앞에서 6개의 숫자만 섞었으므로, 출력하면 랜덤한 6개의 로또 번호가 나옴
        for (int i = 0; i < 6; i++) {
            System.out.print(lotto[i] + " ");
        }
        System.out.println();
        
        System.out.println("--------로또 번호 추출 완료--------");
    }
}
