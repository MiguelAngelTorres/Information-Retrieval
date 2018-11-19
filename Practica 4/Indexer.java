import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Paths;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.apache.lucene.document.LongPoint;
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
		try{
			reader.read(docPath[0],"Questions",this);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		/*
		cadena = reader.read(docPath[1],"Answers");
		indexQuestions(cadena);
		cadena = reader.read(docPath[2],"Tags");
		indexQuestions(cadena);*/
	}


	public void addQuestion(String[] question){
		Document doc = new Document ();
			 
		Integer valor = Integer.decode(question[0]);
		// Store idQuestion on Lucene doc
		doc.add ( new IntPoint("ID",valor));
		doc.add ( new StoredField("ID", valor));

		// Store body on Lucene doc
		doc.add(new TextField( "Body", question[5], Store.YES));
		
		// Store date		
		try{
			System.out.println(question[2]);
			Date date = new SimpleDateFormat("yyyy−MM−dd'T'HH:mm:s s'Z'").parse(question[2]);
			doc.add( new LongPoint("Date",date.getTime()));
		} catch( ParseException e) {
			System.out.print(e.getMessage());
		}



		// Insert doc in Index
		try{
			writer.addDocument(doc);
		}catch( IOException e ){
			System.out.println("Error adding document to index.");
		}	

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
