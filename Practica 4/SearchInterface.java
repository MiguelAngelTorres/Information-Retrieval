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
					clause += " [ ";
					for(String word : words){
						clause += word + " ";
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
					clause += " [ ";
					for(String word : words){
						clause += word + " ";
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
					clause += " [ ";
					for(String word : words){
						clause += word + " ";
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
					clause += " [ ";
					for(String word : words){
						clause += word + " ";
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

	public void addSearchByVotes(int min, int max, int mode){
		String clause = "The question must have ";
		if(mode == 1){
			clause += "more than " + min + " votes."; 
		}else if(mode == 2){
			clause += "less than " + max + " votes.";
		}else{
			clause += "between " + min + " and " + max + " votes.";
		}
		clauses.add(clause);
		searcher.addSearchByVotes(min,max);
	}


	public void addOrder(int mode, boolean descendent){
		String clause = "Order the questions by ";

		if(descendent){
			clause += "descendent ";			
		}else{
			clause += "ascendent ";
		}
		
		if(mode == 1){
			clause += "number of votes."; 
		}else if(mode == 2){
			clause += "publication date.";
		}else{
			clause += "number of responses.";
		}
		order.add(clause);
		searcher.addOrder(mode,ascendent);
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
		for(String st : order){
			System.out.println(" - " + st);
		}
	}

	public boolean searchAgain(){
		Scanner input = new Scanner(System.in);

		System.out.println("\nWould you like to make a new search?\n" +
									"  [1] - YES\n" +
									"  [2] - NO\n"
		);

		String res = input.nextLine();
		if(res.equals("1") || res.equals("y")){
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
									"  [3] - Add a word restriction\n" +
									"  [4] - Add a range of votes\n" +
									"  [5] - Add an order of relevance"
		);
		if(!first){
			System.out.println("  [6] - Charge last restrictions");
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
			if(!first){
				resetSearch();
			}
			requestTextField();
			requestClause(first);
		}else if(res == 4){
			if(!first){
				resetSearch();
			}
			requestRangeModeVotes();
			requestClause(first);
		}else if(res == 5){
			if(!first){
				resetSearch();
			}
			requestOrderType();
			requestClause(first);
		}else if(res == 6 && !first){
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

	public void requestRangeModeVotes(){
		Scanner input = new Scanner(System.in);
		System.out.println("\nSelect your choice:\n" +
									"  [1] - Minimum of votes.\n" +
									"  [2] - Maximum of votes.\n" +
									"  [3] - Between a range of votes."
		);
		int res = input.nextInt();

		if(res == 1 || res == 2 || res == 3){
			requestRange(res);
		}else{
			System.out.println("This is embarrasing, it seems you typed wrongly.");
			requestTextField();
		}
	}

	public void requestRange(int mode){
		Scanner input = new Scanner(System.in);

		if(mode == 1){
			System.out.println("\nWrite the minimum:\n");
			int res = input.nextInt();
			addSearchByVotes(res,999999,1);
		}else if(mode == 2){
			System.out.println("\nWrite the maximum:\n");
			int res = input.nextInt();
			addSearchByVotes(-999999,res,2);
		}else{
			boolean condition = true;
			while(condition){
				System.out.println("\nWrite the minimum and maximum separated with a whitespace:\n");
				String res = input.nextLine();
				String[] splited = res.split("\\s+");
				if(splited.length == 2){
					int res1 = Integer.decode(splited[0]);
					int res2 = Integer.decode(splited[1]);
					if(res1 < res2){
						addSearchByVotes(res1,res2,3);
					}else{
						addSearchByVotes(res2,res1,3);
					}
				condition = false;
				}
			}
		}
	}
	

	public void requestOrderType(){
		Scanner input = new Scanner(System.in);
		System.out.println("\nSelect the field for order:\n" +
									"  [1] - Order by vote puntuation.\n" +
									"  [2] - Order by publication date.\n" +
									"  [3] - Order by number of answers (todo section)."
		);
		int res = input.nextInt();

		if(res == 1 || res == 2 || res == 3){
			requestOrderSide(res);
		}else{
			System.out.println("This is embarrasing, it seems you typed wrongly.");
			requestTextField();
		}
	}
	
	public void requestOrderSide(int mode){
		Scanner input = new Scanner(System.in);
		System.out.println("\nChoose ascendent or descendent order:\n" +
										"  [1] - Ascendent order.\n" +
										"  [2] - Descendent order"
		);
		int res = input.nextInt();
		if(res == 1){
			addOrder(mode, false);
		}else if(res==2){
			addOrder(mode, true);
		}else{
			System.out.println("This is embarrasing, it seems you typed wrongly.");
			requestOrderSide(mode);
		}		
	}


///////////////// -----------------------   MAIN METHOD  ---------------------------- ///////////
	
	public static void main ( String[] args ) {
		SearchInterface In = new SearchInterface(args[0]);

		
		In.requestClause(true);


	}



}
