import java.util.List;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

public class Reader{

	Reader(){}

	public void read(String path, String type, Indexer index) throws IOException {		
		BufferedReader buffer = new BufferedReader(new FileReader( path ));

		if(type == "Tags"){
			readTags(buffer, index);
		}

		String line;
		String accumline = "";

		buffer.readLine();
		while ((line = buffer.readLine()) != null) {
			//System.out.println(line + "\n\n");
			if(line.length() != 0){
				if(line.equals("\"") ){
					//System.out.println(accumline);
					//System.out.println("________________");
					if(type == "Questions"){
						try{
							index.addQuestion(splitInstance(accumline, 5));
						}catch( IOException e ){
							System.out.println("Error while saving a question: " + e.getMessage());
						}
					} else if(type == "Answers"){
						index.addAnswer(splitInstance(accumline, 6));
					}
					accumline = "";
				}else{
					accumline += line;
				}
			}
		}
	}


	public static String[] splitInstance(String instance, Integer fields) {
		String[] res = new String[fields+1]; 
		String lastField = "";
		if (instance != null) {
			String[] splitData = instance.split(",");
			for (int i = fields; i < splitData.length; i++) {
				lastField+=splitData[i];
			}
			for(int j = 0; j < fields; j++){
				res[j] = splitData[j];
			}
			res[fields] = lastField;

		}
		return res;
	}


	public void readTags(BufferedReader buffer, Indexer index) throws IOException {
		String line;
		String accumline = "";

		buffer.readLine();
		while ((line = buffer.readLine()) != null) {
			index.addTag(line.split(","));
		}
	}
}
