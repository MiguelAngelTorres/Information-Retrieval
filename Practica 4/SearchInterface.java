import java.util.ArrayList;
import java.io.IOException;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;


public class SearchInterface{
	public static Searcher searcher;
	public ArrayList<String> clauses;
	public ArrayList<String> filters;
	public ArrayList<String> order;
	public Integer maxHits;
	public boolean firstSearch;

///////////////// -----------------------   SEARCH METHODS  ---------------------------- /////////

	SearchInterface(String path){
		try{		
			searcher = new Searcher(path);  // Create searcher with first argument as path
		} catch ( IOException e){
			System.out.println(e.getMessage());
		}
		clauses = new ArrayList<String>();
		maxHits = 5;
		firstSearch = true;
	}

	public void resetSearch(){
		searcher.resetSearch();
		clauses.clear();
		filters.clear();
		order.clear();
	}

	// words cannot be empty
	public void addSearchByTitle(String[] words, boolean obligatory) throws IOException {
		String clause = "The word(s)";
		if(obligatory){
			searcher.addSearchByTitle(words,BooleanClause.Occur.MUST);
			for(String word : words){
				clause += " [" + word + "]";
 			}
			clause += " must appear on the title.";
			clauses.add(clause);
		}else{
			searcher.addSearchByTitle(words,BooleanClause.Occur.SHOULD);
			for(String word : words){
				clause += " [" + word + "]";
 			}
			clause += " should appear on the title.";
			clauses.add(clause);
		}
	}

	public void executeSearch(){
		try{
			searcher.executeSearch(maxHits);
		} catch( IOException e ){
			System.out.println("Error while executing search: " + e.getMessage());
		}
	}


///////////////// ----------------------   INTEFACE METHODS  ---------------------------- /////////

	public void requestClause(){
		System.out.println("\nPlease, input your's number choice:\n" +
									"  [1] - Add word restriction"
		);
	}




///////////////// -----------------------   MAIN METHOD  ---------------------------- ///////////
	
	public static void main ( String[] args ) {
		SearchInterface In = new SearchInterface(args[0]);
		
		In.requestClause();


	}



}
