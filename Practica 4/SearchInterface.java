import java.io.IOException;

public class SearchInterface{
	public static Searcher searcher;

	SearchInterface(String path){
		try{		
			searcher = new Searcher(path);  // Create searcher with first argument as path
		} catch ( IOException e){
			System.out.println(e.getMessage());
		}
	}

	public static void main ( String[] args ) {
		SearchInterface In = new SearchInterface(args[0]);
		
		String[] query = {"vector"};
		try{
			searcher.searchByTitle(query,5);
		} catch (IOException e){
			System.out.println("Error while searching: " + e.getMessage());
		}

	}



}
