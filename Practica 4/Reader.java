import java.util.List;
import java.util.LinkedList;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

//import org.jsoup.nodes.Document;
//import org.jsoup.Jsoup;
import com.opencsv.CSVReader;

public class Reader{
	Reader(){}

	public List<String[]> read(String path, String type) throws IOException {
		List<String[]> res = new LinkedList<String[]> ();
		CSVReader csvReader = new CSVReader(new FileReader(path), ',');
		Scanner scanner = new Scanner(new File(path));
		scanner.useDelimiter(",");
		String[] aux;


		if(type == "Questions"){
			//res = csvReader.readAll() ;		
			
			
			while((aux = csvReader.readNext()) != null){
				//System.out.println("_________________________________");
				//System.out.println(aux[0]);
				res.add(aux);
			}

			/*while(scanner.hasNext()){
				System.out.print(scanner.next()+"|-|-|");
			}*/
		}else if(type == "Answers"){
			
		}else{

		}

		return res;
	}
}
