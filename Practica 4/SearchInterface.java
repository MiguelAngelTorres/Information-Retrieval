import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;


public class SearchInterface{
	public static Searcher searcher;
	public ArrayList<String> clauses;
	public ArrayList<String> filters;
	public ArrayList<String> order;
	public ArrayList<String> lastclauses;
	public ArrayList<String> lastfilters;
	public ArrayList<String> lastorder;
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
		filters = new ArrayList<String>();
		order = new ArrayList<String>();
		lastclauses = new ArrayList<String>();
		lastfilters = new ArrayList<String>();
		lastorder = new ArrayList<String>();
		maxHits = 5;
		firstSearch = true;
	}

	public void resetSearch(){
		searcher.resetSearch();
		clauses.clear();
		filters.clear();
		order.clear();
	}

	public void redoSearch(){
		searcher.redoSearch();
		clauses.addAll(lastclauses);
		filters.addAll(lastfilters);
		order.addAll(lastorder);
		lastclauses.clear();
		lastorder.clear();
		lastfilters.clear();
	}

	// words cannot be empty
	public void addSearchByTitle(String[] words, boolean obligatory, String fields, boolean phrase) throws IOException {
		String clause = "The word(s)";
		if(obligatory){
			searcher.addSearchByTitle(words,BooleanClause.Occur.MUST, phrase);
			if(fields != ""){
				if(phrase){
					clause += " [";
					for(String word : words){
						clause +=  word;
		 			}
					clause += "]";
				}else{
					for(String word : words){
						clause += " [" + word + "]";
		 			}
				}
				clause += " must appear on " + fields;
				clauses.add(clause);
			}
		}else{
			searcher.addSearchByTitle(words,BooleanClause.Occur.SHOULD, phrase);
			if(fields != ""){
				if(phrase){
					clause += " [";
					for(String word : words){
						clause += word;
		 			}
					clause += "]";
				}else{
					for(String word : words){
						clause += " [" + word + "]";
		 			}
				}
				clause += " should appear on " + fields;
				clauses.add(clause);
			}
		}
	}
	public void addSearchByBody(String[] words, boolean obligatory, String fields, boolean phrase) throws IOException {
		String clause = "The word(s)";
		if(obligatory){
			searcher.addSearchByBody(words,BooleanClause.Occur.MUST, phrase);
			if(fields != ""){
				if(phrase){
					clause += " [";
					for(String word : words){
						clause += word;
		 			}
					clause += "]";
				}else{
					for(String word : words){
						clause += " [" + word + "]";
		 			}
				}
				clause += " must appear on " + fields;
				clauses.add(clause);
			}
		}else{
			searcher.addSearchByBody(words,BooleanClause.Occur.SHOULD, phrase);
			if(fields != ""){
				if(phrase){
					clause += " [";
					for(String word : words){
						clause += word;
		 			}
					clause += "]";
				}else{
					for(String word : words){
						clause += " [" + word + "]";
		 			}
				}
				clause += " should appear on " + fields;
				clauses.add(clause);
			}
		}
	}

	public void executeSearch(){
		try{
			lastclauses.addAll(clauses);
			lastfilters.addAll(filters);
			lastorder = order;
			clauses.clear();
			filters.clear();
			order.clear();
			searcher.executeSearch(maxHits);
			if(searchAgain()){
				requestClause(false);
			}
		} catch( IOException e ){
			System.out.println("Error while executing search: " + e.getMessage());
		}
	}






///////////////// ----------------------   INTEFACE METHODS  ---------------------------- /////////


	public void chargedClauses(){
		System.out.println("\nThe added restriction are:");
		for(String st : clauses){
			System.out.println(" - " + st);
		}
	}

	public boolean searchAgain(){
		Scanner input = new Scanner(System.in);

		System.out.println("\nWould you like to make a new search?\n" +
									"  [1] - YES\n" +
									"  [2] - NO\n"
		);

		int res = input.nextInt();
		if(res==1){
			return true;
		}else{
			return false;
		}
	}

	public void requestClause(boolean first){
		Scanner input = new Scanner(System.in);
		System.out.println("\nPlease, input your's number choice:\n" +
									"  [1] - Execute the actual search\n" +
									"  [2] - Watch the actual restrictions\n" +
									"  [3] - Add word restriction"
		);
		if(!first){
			System.out.println("  [4] - Charge last restrictions");
		}
		int res = input.nextInt();

		if(res == 1){
			if(!first){
				resetSearch();
			}
			executeSearch();
		}else if(res == 2){
			chargedClauses();
			requestClause(first);
		}else if(res == 3){
			resetSearch();
			requestTextField();
			requestClause(first);
		}else if(res == 4 && !first){
			redoSearch();
			requestClause(true);
		}else{
			System.out.println("This is embarrasing, it seems you typed wrongly.");
			requestClause(first);
		}
	}

	public void requestTextField(){
		Scanner input = new Scanner(System.in);
		System.out.println("\nSelect your choice:\n" +
									"  [1] - Word on title.\n" +
									"  [2] - Word on body.\n" +
									"  [3] - Word on both (title and body)."
		);
		int res = input.nextInt();

		if(res == 1 || res == 2 || res == 3){
			requestWords(res);
		}else{
			System.out.println("This is embarrasing, it seems you typed wrongly.");
			requestTextField();
		}
	}

	public void requestWords(int field){
		Scanner input = new Scanner(System.in);
		Scanner input2 = new Scanner(System.in);
		String clause = "";
		List<String> aux = new ArrayList<String>();
		boolean phrase = false;
		System.out.println("\nSelect your choice:\n" +
									"  [1] - Multiple words delimited by whitespaces.\n" +
									"  [2] - A complete phrase or expression." 
		);
		int res = input.nextInt();

		if(res == 1 || res == 2){
			System.out.println("\nType your word(s):");
			String wordsin = input2.nextLine();		
			aux = Arrays.asList(wordsin.split("\\s+"));

			boolean oblig = requestObligatory();
			if(res == 2){
				phrase = true;
			}

			String[] words = new String[aux.size()];
			words = aux.toArray(words);

			try{
				if(field == 1){
					addSearchByTitle(words,oblig,"the title.", phrase);
				}else if(field == 2){
					addSearchByBody(words,oblig,"the body.", phrase);
				}else{
					String fields;
					if(oblig){
						fields="the title and the body.";
					}else{
						fields="the title or the body.";
					}
					addSearchByTitle(words,oblig,fields, phrase);
					addSearchByBody(words,oblig,"", phrase);
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}else{
			System.out.println("This is embarrasing, it seems you typed wrongly.");
			requestWords(field);
		}
	}

	public boolean requestObligatory(){	
		Scanner input = new Scanner(System.in);
		System.out.println("\nSelect your choice:\n" +
									"  [1] - The words must appear.\n" +
									"  [2] - The words could not appear." 
		);
		int res = input.nextInt();
		if(res == 1){
			return true;
		}else if(res == 2){
			return false;
		}else{
			System.out.println("This is embarrasing, it seems you typed wrongly.");
			return requestObligatory();
		}

	}





///////////////// -----------------------   MAIN METHOD  ---------------------------- ///////////
	
	public static void main ( String[] args ) {
		SearchInterface In = new SearchInterface(args[0]);

		
		In.requestClause(true);


	}



}
