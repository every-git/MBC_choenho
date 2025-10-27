package ex02;
/*
 * 정수 배열 5개 생성 후
 * 각 배열 초기화 이후, 각 배열 합을 구해서 출력
 */
//20251027
public class ArrayEx02 {
    public static void main(String[] args) {
    
    int[] scores = {90, 80, 70, 60, 50};
    int sum = 0;
    //for-each문을 사용하여 배열의 요소를 하나씩 가져오는 반복문
    for (int score : scores) {
        sum += score;
    }
    System.out.println("배열 합 : " + sum);
    System.out.println("배열 평균 : " + sum / scores.length);

    }
}
/*
 * for문과 for-each문의 차이점
 * for문은 배열의 인덱스를 사용하여 배열의 요소를 가져오는 반복문
 * for-each문은 배열의 요소를 하나씩 가져오는 반복문. 구조는 for (int score : scores) {
 *     sum += score;
 * }
 * for-each문은 배열의 요소를 하나씩 가져오는 반복문이므로, 배열의 요소를 하나씩 가져오는 반복문이다. 
 * 배열의 이름은 복수형, 요소는 단수형으로 사용한다.
 */