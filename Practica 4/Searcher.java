import java.util.ArrayList;
import java.nio.file.Paths;
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.Sort;

public class Searcher{
	IndexSearcher searcher;
	public ArrayList<BooleanClause> clauses;
	public ArrayList<SortField> order;	
	public ArrayList<BooleanClause> lastclauses;
	public ArrayList<SortField> lastorder;	
	
	Searcher(String path) throws IOException {
		Directory dir = FSDirectory.open(Paths.get(path));
	 	IndexReader reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);	
		clauses = new ArrayList<BooleanClause>();
		order = new ArrayList<SortField>();
		lastclauses = new ArrayList<BooleanClause>();
		lastorder = new ArrayList<SortField>();
	}

	public void resetSearch(){
		clauses.clear();
		order.clear();
	}

	public void redoSearch(){
		clauses.addAll(lastclauses);
		order.addAll(lastorder);
		lastclauses.clear();
		lastorder.clear();
	}

	public void addSearchByTitle(String[] words, BooleanClause.Occur oblig, boolean phrase) throws IOException {
		Query q;
		BooleanClause bc;
		if(phrase){
			PhraseQuery.Builder builder = new PhraseQuery.Builder();
			int i = 0;
			for(String word : words){
				builder.add(new Term("Title", word),i);
				i++;
			}
			PhraseQuery pq = builder.build();
			bc = new BooleanClause(pq, oblig);
			clauses.add(bc);
		}else{
			for(String word : words){
				q = new TermQuery(new Term("Title", word));
				bc = new BooleanClause(q, oblig);
				clauses.add(bc);
			}
		}
	}
	public void addSearchByBody(String[] words, BooleanClause.Occur oblig, boolean phrase) throws IOException {
		Query q;
		BooleanClause bc;
		if(phrase){
			PhraseQuery.Builder builder = new PhraseQuery.Builder();
			int i = 0;
			for(String word : words){
				builder.add(new Term("Body", word),i);
				i++;
			}
			PhraseQuery pq = builder.build();
			bc = new BooleanClause(pq, oblig);
			clauses.add(bc);
		}else{	
			for(String word : words){
				q = new TermQuery(new Term("Body", word));
				bc = new BooleanClause(q, oblig);
				clauses.add(bc);
			}
		}
	}

	
	public void addSearchByVotes(int min, int max){
		Query q = IntPoint.newRangeQuery("Mark", min, max);
		BooleanClause bc = new BooleanClause(q, BooleanClause.Occur.MUST);
		clauses.add(bc);
	}


	public void addOrder(int mode, boolean isDescendent){
		SortField sf;

		if(mode==1){
			sf = new SortField("Mark_sort",SortField.Type.LONG,isDescendent);
			order.add(sf);
		}else if(mode==2){
			sf = new SortField("Date",SortField.Type.STRING,isDescendent);
			order.add(sf);
		}else{

		}	
	}

	// if no clauses are given, all the questions are returned
	public void executeSearch(int maxres) throws IOException {
		BooleanQuery bq;
		BooleanQuery.Builder bqbuilder = new BooleanQuery.Builder();

		if(clauses.isEmpty()){
			Query q = new MatchAllDocsQuery();
			BooleanClause bc = new BooleanClause(q, BooleanClause.Occur.SHOULD);
			clauses.add(bc);
		}
		for(BooleanClause bc : clauses){
			bqbuilder.add(bc);
		}
		bq = bqbuilder.build();

		order.add(SortField.FIELD_SCORE);
		SortField[] orderArray = new SortField[order.size()];
		for(int i=0; i<order.size(); i++)
		orderArray[i] = order.get(i);
		Sort s = new Sort(orderArray);


		lastclauses.addAll(clauses);
		lastorder.addAll(order);
		clauses.clear();
		order.clear();
		TopFieldDocs tdocs = searcher.search(bq,maxres,s);
		printDocs(tdocs);

	}

	public void printDocs(TopFieldDocs docs) throws IOException {
		System.out.println(docs.totalHits + " resultados relacionados.\n");

		for(ScoreDoc sd : docs.scoreDocs){
			Document d = searcher.doc(sd.doc);
			System.out.println(sd.score + " - https://stackoverflow.com/questions/"+d.get("ID"));
		}
	}
}
