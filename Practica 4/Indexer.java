import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Paths;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;

public class Indexer{
	String indexPath = "./index";
	String[] docPath = {"./source/Questions.csv", "./source/Answers.csv", "./source/Tags.csv"};
	boolean create = true;
	private Reader reader = new Reader();
	private IndexWriter writer ;
	

	Indexer(){

	}


	public static void main ( String[] args ) {
		Similarity similarity = new ClassicSimilarity(); // BM25 by default

		// Create an analyzer per field, and set default as WhitespaceAnalyzer
		PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(), analyzerSet());

		Indexer baseline = new Indexer() ;

		try{
			baseline.configureIndex (analyzer, similarity);
		}catch( IOException e ){
			System.out.println("Error configuring the index.");
		}		
		baseline.indexDocs() ;
		baseline.close();
	}


	public static Map<String, Analyzer> analyzerSet(){
		Map<String, Analyzer> res = new HashMap<String, Analyzer>();



		return res;
	}


	public void configureIndex(Analyzer analyzer, Similarity similarity) throws IOException {
		IndexWriterConfig iwc = new IndexWriterConfig (analyzer);
		iwc.setSimilarity (similarity);
		
		if(create){
			iwc.setOpenMode (IndexWriterConfig.OpenMode.CREATE) ;
		}else{
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND ) ;
		}

		Directory dir = FSDirectory.open(Paths.get(indexPath));	
		writer = new IndexWriter(dir,iwc);
	}


	public void indexDocs (){
		List<String[]> questions = null;
		try{
			questions = reader.read(docPath[0],"Questions");
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		System.out.println(questions);

		/*indexQuestions(cadena);
		cadena = reader.read(docPath[1],"Answers");
		indexQuestions(cadena);
		cadena = reader.read(docPath[2],"Tags");
		indexQuestions(cadena);*/
	}


	public void indexQuestions(String cadena){
		Document doc = new Document ();
			
		// Extract ID from String
		Integer start = 0;
		Integer end = 0; 
		String aux = cadena.substring (start,end);
		Integer valor = Integer.decode(aux);
		// Store field on Lucene doc
		doc.add ( new IntPoint("ID",valor));
		doc.add ( new StoredField("ID", valor));

		// Extract Body from string
		start = 0;
		end = 0;
		String body = cadena.substring(start,end);
		// Store field on Lucene doc
		doc.add(new TextField( "Body", body, Store.YES));


		// Insert doc in Index
		try{
			writer.addDocument(doc);
		}catch( IOException e ){
			System.out.println("Error adding document to index.");
		}	


		// If already exist, update with:
		// writer.updateDocument(new Term("ID", valor.toString()),doc);
	}
	public void indexAnswers(String cadena){

	}
	public void indexTags(String cadena){

	}


	public void close ( ) {
		try {
			writer.commit ( ) ;
			writer.close () ;
		}catch ( IOException e ) {
			System.out.println("Error closing the index.");
		}
	}
}
