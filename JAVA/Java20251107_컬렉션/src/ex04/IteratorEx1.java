package ex04;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
public class IteratorEx1 {

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(10);
		list.add(5);
		list.add(9);
		list.add(15);
		System.out.println(list);
		
		for(int i : list)
			System.out.print(i + " ");

		System.out.println("\n--------Iterator--------");

		Iterator<Integer> it = list.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}

		/* it = list.iterator();
		while(it.hasNext()) {
			System.out.println(it.next()); */
		
	}

}



/*
 * [10, 5, 9, 15]
 * 10 5 9 15
 * --------Iterator--------
 * 10
 * 5
 * 9
 * 15
 */