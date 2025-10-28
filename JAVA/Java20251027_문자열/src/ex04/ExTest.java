package ex04;
import java.util.Arrays;
/*
 * 1. 배열 10개 방에 랜덤하게 1~100 사이의 정수를 저장
 * 2. 배열에서 최대값과 최소값을 출력
 * 3. 10개 값의 총점 / 평균 구하기
 * 4. 10개 값을 정렬(오름차순)하여 출력
 */
public class ExTest {
    public static void main(String[] args) {
        // 1. 배열 10개 방에 랜덤하게 1~100 사이의 정수를 저장
        System.out.println("------배열 10개 방에 랜덤하게 1~100 사이의 정수를 저장------");

        int[] iArr = new int[10]; // 배열 10개 방 생성
        for (int i = 0; i < iArr.length; i++) { // 배열 10개 방에 랜덤하게 1~100 사이의 정수를 저장
            iArr[i] = (int) (Math.random() * 100) + 1;
        }
        System.out.println(Arrays.toString(iArr)); // 배열을 문자열로 변환하여 출력

        // 2. 배열에서 최대값과 최소값을 출력
        System.out.println("------배열에서 최대값과 최소값을 출력------");

        int max = iArr[0]; // 최대값 초기화
        int min = iArr[0]; // 최소값 초기화
        for (int i = 0; i < iArr.length; i++) { // 배열 10개 방에 랜덤하게 1~100 사이의 정수를 저장
            if (iArr[i] > max)
                max = iArr[i]; // 최대값 갱신
            if (iArr[i] < min)
                min = iArr[i]; // 최소값 갱신
        }
        System.out.println("최대값 : " + max);
        System.out.println("최소값 : " + min);

        // 3. 10개 값의 총점과 평균 구하기
        System.out.println("------10개 값의 총점과 평균 구하기------");
        int sum = 0; // 총점 초기화
        double avg = 0; // 평균 초기화
        for (int i = 0; i < iArr.length; i++) {
            sum += iArr[i]; // 총점 누적
        }
        avg = (double) sum / iArr.length; // 평균 계산
        System.out.println("총점 : " + sum);
        System.out.println("평균 : " + avg);

        // 4. 10개 값을 정렬(오름차순)하여 출력, 버블정렬을 이중 for문으로 작성
        System.out.println("------10개 값을 버블정렬(오름차순)하여 출력------");
        for (int i = 0; i < iArr.length; i++) { //배열의 길이만큼 반복
            for (int j = 0; j < iArr.length - i - 1; j++) { //배열의 길이 - i - 1만큼 반복
                if (iArr[j] > iArr[j + 1]) { // 현재 인덱스의 값이 다음 인덱스의 값보다 크면
                    int tmp = iArr[j]; // 현재 인덱스의 값을 임시 변수에 저장
                    iArr[j] = iArr[j + 1]; // 현재 인덱스의 값을 다음 인덱스의 값으로 변경
                    iArr[j + 1] = tmp; // 임시 변수의 값을 다음 인덱스의 값으로 변경
                }
            }
        }   
        // Arrays.sort(iArr); sort 메서드를 사용하여 정렬하는 방법도 있다.
        System.out.println(Arrays.toString(iArr)); // 배열을 문자열로 변환하여 출력
    }
}
/*
 * 버블정렬 알고리즘
 * 1. 배열의 첫 번째 요소와 두 번째 요소를 비교하여 첫 번째 요소가 더 크면 두 요소를 교환
 * 2. 두 번째 요소와 세 번째 요소를 비교하여 두 번째 요소가 더 크면 두 요소를 교환
 * 3. 이러한 과정을 배열의 마지막 요소까지 반복
 */
