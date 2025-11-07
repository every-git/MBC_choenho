package ex06;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class HashLotto {
	public static void main(String[] args) {

		Set<Integer> set = new HashSet<Integer>();
		//lotto 5게임
		System.out.println("--------로또 번호 5게임 생성기--------");
		// for문을 이용해서 5게임 생성
		for(int i = 0; i < 5; i++) {
			System.out.println("--------로또 번호 " + (i + 1) + "게임--------");
			// while문을 이용해서 6개의 번호 생성
			while(set.size() < 6) {
				set.add((int)(Math.random() * 45) + 1);
			}
			//로또번호를 오름차순으로 정렬
			List<Integer> list = new ArrayList<Integer>(set);
			Collections.sort(list);
			System.out.println("로또 번호 : " + list);
		}
		System.out.println("--------대박! 추첨 완료!--------");
	}

}
