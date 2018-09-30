import java.util.HashMap;

public class FrecuenCounter {

	private String text;

	FrecuenCounter(String t){
		text = t;
	}

	public HashMap<String,Integer> getWordFrecuency(){
		//Split the string in words by spaces.		
		String[] splited = text.split("\\s+");
		HashMap<String,Integer> frec = new HashMap<String,Integer> (splited.length/5);		


		//Count a word and take next
		int max = splited.length;
		int aux;
		for(int i=0; i<max; i++){
			if(frec.containsKey(splited[i])){
				aux = frec.get(splited[i]);
				frec.put(splited[i], aux+1);
			} else {
				frec.put(splited[i], 1);
			}
		}

	return frec;
	}

}
