import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class FrecuenCounter {

	private String text;

	FrecuenCounter(String t){
		text = t;
	}

	public List getWordFrecuency(){
		//Split the string in words by spaces.		
		String[] splited = text.split("\\s+");
		HashMap<String,Integer> frec = new HashMap<String,Integer> (splited.length/5);		


		//Count a word and take next
		int max = splited.length;
		int aux;
		for(int i=0; i<max; i++){
			if(frec.containsKey(splited[i])){
				aux = frec.get(splited[i]);
				frec.put(splited[i], aux-1);
			} else {
				frec.put(splited[i], -1);
			}
		}

	return sort(frec);
	}




	private static List sort(HashMap m) { 
		List list = new LinkedList(m.entrySet());
		
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) (((Map.Entry) (o1)).getValue())).compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		return list;
	}
}


