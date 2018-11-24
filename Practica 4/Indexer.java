import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Paths;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Long;

import org.apache.lucene.util.BytesRef;
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
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.Field.Store;


public class Indexer{
	String[] indexPath = {"./indexes/Question","./indexes/Answer"};
	String[] docPath = {"./source/Questions.csv", "./source/Answers.csv", "./source/Tags.csv"};
	boolean create = true;
	private Reader reader = new Reader();
	private IndexWriter indexQuestionWriter, indexAnswerWriter ;
	

	Indexer(){

	}


	public static void main ( String[] args ) {
		Similarity similarity = new ClassicSimilarity(); // BM25 by default

		// Create an analyzer per field, and set default as WhitespaceAnalyzer
		PerFieldAnalyzerWrapper analyzerQuestion = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerSetQuestion());
		PerFieldAnalyzerWrapper analyzerAnswer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerSetAnswer());

		Indexer baseline = new Indexer() ;

		try{
			baseline.configureIndex (analyzerQuestion, analyzerAnswer, similarity);
		}catch( IOException e ){
			System.out.println("Error configuring the index.");
		}		
		baseline.indexDocs() ;
		baseline.close();
	}


	public static Map<String, Analyzer> analyzerSetQuestion(){
		Map<String, Analyzer> res = new HashMap<String, Analyzer>();
		res.put("Title", new WhitespaceAnalyzer());
		res.put("Mark_string", new WhitespaceAnalyzer());


		return res;
	}
	public static Map<String, Analyzer> analyzerSetAnswer(){
		Map<String, Analyzer> res = new HashMap<String, Analyzer>();

		return res;
	}


	public void configureIndex(Analyzer analyzerQuestion, Analyzer analyzerAnswer, Similarity similarity) throws IOException {
		IndexWriterConfig iwcQ = new IndexWriterConfig (analyzerQuestion);
		iwcQ.setSimilarity (similarity);
		
		IndexWriterConfig iwcA = new IndexWriterConfig (analyzerAnswer);
		iwcA.setSimilarity (similarity);

		if(create){
			iwcQ.setOpenMode (IndexWriterConfig.OpenMode.CREATE);
			iwcA.setOpenMode (IndexWriterConfig.OpenMode.CREATE);
		}else{
			iwcQ.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND ) ;
			iwcA.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND ) ;
		}

		Directory dir = FSDirectory.open(Paths.get(indexPath[0]));	
		indexQuestionWriter = new IndexWriter(dir,iwcQ);
		dir = FSDirectory.open(Paths.get(indexPath[1]));	
		indexAnswerWriter = new IndexWriter(dir,iwcA);
	}


	public void indexDocs (){
		try{
			reader.read(docPath[0],"Questions",this);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}


		try{
			reader.read(docPath[1],"Answers",this);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}


		/*try{
			reader.read(docPath[2],"Tags",this);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}*/


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

		//Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(question[2]);
		//doc.add( new LongPoint("Date",date.getTime()));
		doc.add(new SortedDocValuesField("Date", new BytesRef(question[2])));


		// Store mark
		doc.add(new IntPoint("Mark", Integer.decode(question[3])));
		doc.add(new NumericDocValuesField("Mark_sort", Long.parseLong(question[3])));
		// Store title
		doc.add(new TextField( "Title", question[4], Store.YES));


		// Insert doc in Index
		try{
			indexQuestionWriter.addDocument(doc);
		}catch( IOException e ){
			System.out.println("Error adding document to index.");
		}	
	}


	public void addAnswer(String[] answer){
		Document doc = new Document ();
			 
		Integer valor = Integer.decode(answer[0]);
		// Store idQuestion on Lucene doc
		doc.add ( new IntPoint("ID",valor));
		doc.add ( new StoredField("ID", valor));

		// Store body on Lucene doc
		doc.add(new TextField( "Body", answer[6], Store.YES));

		// Store mark
		doc.add(new IntPoint("Mark", Integer.decode(answer[4])));

		// Store accepted 0(No)/1(Yes)
		valor = answer[5] == "FALSE" ? 0 : 1;
		doc.add(new IntPoint("Accepted", valor));

		// Store title
		doc.add(new StoredField( "IDParent", Integer.decode(answer[3])));


		// Insert doc in Index
		try{
			indexAnswerWriter.addDocument(doc);
		}catch( IOException e ){
			System.out.println("Error adding document to index.");
		}	
	}


	public void addTag(String[] cadena){

	}


	public void close ( ) {
		try {
			indexQuestionWriter.commit() ;
			indexQuestionWriter.close() ;
			indexAnswerWriter.commit() ;
			indexAnswerWriter.close() ;
		}catch ( IOException e ) {
			System.out.println("Error closing the index.");
		}
	}
}
