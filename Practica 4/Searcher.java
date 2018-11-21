import java.nio.file.Paths;
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;

public class Searcher{
	IndexSearcher searcher;
	
	
	Searcher(String path) throws IOException {
		Directory dir = FSDirectory.open(Paths.get(path));
	 	IndexReader reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);	
	}


	public void searchByTitle(String[] words, int maxres) throws IOException {
		Query q;
		BooleanClause bc;
		BooleanQuery bq;
		BooleanQuery.Builder bqbuilder = new BooleanQuery.Builder();
	
		for(String word : words){
			q = new TermQuery(new Term("Title", word));
			bc = new BooleanClause(q, BooleanClause.Occur.SHOULD);
			bqbuilder.add(bc);
		}
		bq = bqbuilder.build();

		System.out.println(bq.toString());
		TopDocs tdocs = searcher.search(bq,maxres);
		printDocs(tdocs);
	}


	public void printDocs(TopDocs docs) throws IOException {
		System.out.println(docs.totalHits + " resultados relacionados.\n");

		for(ScoreDoc sd : docs.scoreDocs){
			Document d = searcher.doc(sd.doc);
			System.out.println(sd.score + " - https://stackoverflow.com/questions/"+d.get("ID"));
		}
	}
}
