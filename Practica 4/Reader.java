import java.util.List;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

public class Reader{

	Reader(){}

	public void read(String path, String type, Indexer index) throws IOException {
		BufferedReader buffer = null;
		String line;
		String accumline = "";
		buffer = new BufferedReader(new FileReader( path ));

		buffer.readLine();
		while ((line = buffer.readLine()) != null) {
			//System.out.println(line + "\n\n");
			if(line.length() != 0){
				if(line.equals("\"") ){
					//System.out.println(accumline);
					//System.out.println("________________");
					index.addQuestion(splitInstance(accumline));
				
					accumline = "";
				}else{
					accumline += line;
				}
			}
		}
	}


	public static String[] splitInstance(String instance) {
		String[] res = new String[6]; 
		String lastField = "";
		if (instance != null) {
			String[] splitData = instance.split(",");
			for (int i = 5; i < splitData.length; i++) {
				lastField+=splitData[i];
			}
			//System.out.println(splitData[0]);
			//System.out.println(lastField + "\n\n");
			
			res[0] = splitData[0];
			res[1] = splitData[1];
			res[2] = splitData[2];
			res[3] = splitData[3];
			res[4] = splitData[4];
			res[5] = lastField;

		}
		return res;
	}

}
