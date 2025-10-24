package ex01;

//20251024
public class If10 {

    public static void main(String[] args) {
        int number = (int)(Math.random() * 100) + 1;
        System.out.println("점수 : " + number);
        System.out.println("--------------------------------");
        if (number >= 90) {
            System.out.println("A학점");
        } else if (number >= 80) {
            System.out.println("B학점");
        } else if (number >= 70) {
            System.out.println("C학점");
        } else if (number >= 60) {
            System.out.println("D학점");
        } else {
            System.out.println("F학점");
        }
        System.out.println("--------------------------------");
        // switch 문을 이용해서 위의 if문을 더 간결하게 만들기
        switch (number / 10) {
            case 10: // 100점
            case 9:
                System.out.println("A학점");
                break;
            case 8:
                System.out.println("B학점");
                break;
            case 7:
                System.out.println("C학점");
                break;
            case 6:
                System.out.println("D학점");
                break;
            default:
                System.out.println("F학점");
                break;
        }
        System.out.println("-----스위치문 프로그램 종료-----");
        }
    }
