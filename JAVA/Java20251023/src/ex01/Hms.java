package ex01;
//20251023
import java.util.Scanner;

public class Hms {
/*
 * 키보드 통해서 4000을 입력 받는다.
 * 
 * 입력받은 4000을 시 분 초로 계산한다.
 * 
 * 출력 결과는
 * 1시간 6분 40초
 */
public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    System.out.println("초를 입력하세요 : ");

    int input = sc.nextInt();

    int hour = input / 3600;
    int minute = (input % 3600) / 60;
    int second = input % 60;
    
    System.out.println(hour + "시간 " + minute + "분 " + second + "초");
    sc.close();
    }
}
