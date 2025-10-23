package ex01;

/*
 * ++A : 선위 연산자(증감)
 * B++ : 후위 연산자
 * --A : 감소
 * B--
 */

public class IncDec {

	public static void main(String[] args) {

		int iNum = 10;
		System.out.println(iNum);
		
		System.out.println("iNum 1증가 - 1");
		iNum = iNum+1;
		System.out.println(iNum);
		
		System.out.println("iNum 1증가 - 2");
		iNum += 1;
		System.out.println(iNum);
		
		System.out.println("iNum 1증가 - 3");
		++iNum;
		System.out.println(iNum);
		
		System.out.println("iNum 1증가 - 4");
		iNum++;
		System.out.println(iNum);
		
		System.out.println("------------------");
		System.out.println("선위 연산 1증가 - 5");
		int iNum2 = 1;
		int iA = ++iNum2;
		/*
		 * iNum += 1;
		 * int iA = iNum;
		 */
		System.out.println(iA);
		System.out.println(iNum2);
		
		System.out.println("후위 연산 1증가 - 6");
		iNum2 = 1;
		int iB = iNum2++;
		System.out.println(iB);
		System.out.println(iNum2);
	}

}
