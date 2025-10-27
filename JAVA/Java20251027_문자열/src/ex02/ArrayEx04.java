package ex02;
/*
 * 정수 배열 5개 생성 후
 * 각 배열 초기화 이후, 각 배열 합을 구해서 출력
 * 
 * -- 배열 길이 구하기 --
 */
//20251027
import java.util.Arrays;

public class ArrayEx04 {
    public static void main(String[] args) {
    
    int[] scores = {90, 80, 70, 60, 50};

    System.out.println("기존 scores 길이: " + scores.length);
    System.out.println(Arrays.toString(scores));

    //배열을 10개로 확장
    int[] scores2 = new int[10]; // 새로운 10개의 배열 생성
    // 기존 배열의 요소를 새로운 배열에 복사하는 for문
    for (int i = 0; i < scores.length; i++) {
        scores2[i] = scores[i];
    }
    scores = scores2; // 기존 배열을 새로운 배열로 덮어씌움
    System.out.println("scores 길이: " + scores.length);
    System.out.println(Arrays.toString(scores));

    /*
     * 배열 복사를 간단하게 System.arraycopy() 메서드를 사용하여 복사
     * System.arraycopy(src, srcPos, dest, destPos, length);
     * src: 복사할 배열
     * srcPos: 복사할 배열의 시작 인덱스
     * dest: 복사 받을 배열
     * destPos: 복사 받을 배열의 시작 인덱스
     * length: 복사할 배열의 요소 개수
     */
    int[] scores3 = new int[15]; // 새로운 15개의 배열 생성
    System.arraycopy(scores, 0, scores3, 0, scores.length); // 기존 배열의 요소를 새로운 배열에 복사
    scores = scores3; // 기존 배열을 새로운 배열로 덮어씌움
    System.out.println("scores3 길이: " + scores.length);
    System.out.println(Arrays.toString(scores));
    }
}