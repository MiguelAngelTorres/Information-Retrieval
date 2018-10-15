import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.HashMap;

public class Statistic {

	ArrayList<LinkedList> anal = new ArrayList<LinkedList>();
	ArrayList<String> names = new ArrayList<String>();

	public void addAnalisis(String name, LinkedList tokenCounter){
		anal.add(tokenCounter);
		names.add(name);
	}


	public void printStatistics(String name) { 
		int j = 0;
		System.out.println("\n Estadísticas de " + name);

		System.out.println("\n Cantidad de tokens por analizador y mas frecuentes\n");
		for(int i = names.size()-1; i >= 0; i--){
			System.out.println(names.get(i) + ": " + anal.get(i).size());
			
			Iterator it = anal.get(i).iterator();
			System.out.println("Tokens frecuentes de " + names.get(i) + ":");
			j = 0;
			while(it.hasNext() && j < 30){
				HashMap.Entry pair = (HashMap.Entry)it.next();
				System.out.println(pair.getKey() + " = " + (-(Integer) pair.getValue()));
				j++;
			}
			System.out.println("\n");
		}

		

	}
}

