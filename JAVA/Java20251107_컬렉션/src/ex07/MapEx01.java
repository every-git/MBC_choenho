package ex07;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class MapEx01 {

	public static void main(String[] args) {

		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("김자바", 100);
		map.put("이자바", 100);
		map.put("강자바", 80);
		map.put("안자바", 90);
		
		System.out.println(map.get("김자바"));
		System.out.println(map.get("이자바"));
		System.out.println(map.get("강자바"));
		System.out.println(map.get("안자바"));
		System.out.println("--------------------------------");
		// while문을 이용해서 반복으로 모든 요소를 출력
		Set set = map.entrySet();
		Iterator it = set.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			System.out.printf("%s : %d\n", entry.getKey(), entry.getValue());
		}
	}

}
