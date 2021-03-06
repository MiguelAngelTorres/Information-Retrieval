import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Paths;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Long;
import java.lang.Integer;

import static java.lang.Math.toIntExact;

import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
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

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.FacetField;

public class Indexer{
	String[] indexPath = {"./indexes/Question","./indexes/Answer","./indexes/Tags"};
	String[] docPath = {"./source/Questions.csv", "./source/Answers.csv", "./source/Tags.csv"};
	boolean create = true;
	private Reader reader = new Reader();
	private IndexWriter indexQuestionWriter, indexAnswerWriter, indexTagsWriter ;
	private DirectoryTaxonomyWriter taxoWriter;
	public IndexSearcher searcherAns, searcherTags;
	public FacetsConfig fconfig = new FacetsConfig();

	Indexer(){
		

	}


	public static void main ( String[] args ) {
		Similarity similarity = new ClassicSimilarity(); // BM25 by default

		// Create an analyzer per field, and set default as WhitespaceAnalyzer
		PerFieldAnalyzerWrapper analyzerQuestion = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerSetQuestion());
		PerFieldAnalyzerWrapper analyzerAnswer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerSetAnswer());
		PerFieldAnalyzerWrapper analyzerTag = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerSetTags());
		Indexer baseline = new Indexer() ;

		try{
			baseline.configureIndex (analyzerQuestion, analyzerAnswer, analyzerTag, similarity);
		}catch( IOException e ){
			System.out.println("Error configuring the index.");
		}		
		baseline.indexDocs() ;
		baseline.close();
	}


	public static Map<String, Analyzer> analyzerSetQuestion(){
		Map<String, Analyzer> res = new HashMap<String, Analyzer>();
		res.put("Title", new WhitespaceAnalyzer());
		res.put("TopVotedResponse", new KeywordAnalyzer());

		return res;
	}
	public static Map<String, Analyzer> analyzerSetAnswer(){
		Map<String, Analyzer> res = new HashMap<String, Analyzer>();
		res.put("Mark", new KeywordAnalyzer());

		return res;
	}
	public static Map<String, Analyzer> analyzerSetTags(){
		Map<String, Analyzer> res = new HashMap<String, Analyzer>();
		res.put("Tag", new KeywordAnalyzer());

		return res;
	}


	public void configureIndex(Analyzer analyzerQuestion, Analyzer analyzerAnswer, Analyzer analyzerTag, Similarity similarity) throws IOException {
		IndexWriterConfig iwcQ = new IndexWriterConfig (analyzerQuestion);
		iwcQ.setSimilarity (similarity);
		
		IndexWriterConfig iwcA = new IndexWriterConfig (analyzerAnswer);
		iwcA.setSimilarity (similarity);

		IndexWriterConfig iwcT = new IndexWriterConfig (analyzerTag);
		iwcT.setSimilarity (similarity);

		if(create){
			iwcQ.setOpenMode (IndexWriterConfig.OpenMode.CREATE);
			iwcA.setOpenMode (IndexWriterConfig.OpenMode.CREATE);
			iwcT.setOpenMode (IndexWriterConfig.OpenMode.CREATE);
		}else{
			iwcQ.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND ) ;
			iwcA.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND ) ;
			iwcT.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND ) ;
		}

		Directory dir = FSDirectory.open(Paths.get(indexPath[0]));	
		indexQuestionWriter = new IndexWriter(dir,iwcQ);
		dir = FSDirectory.open(Paths.get(indexPath[1]));	
		indexAnswerWriter = new IndexWriter(dir,iwcA);
		dir = FSDirectory.open(Paths.get(indexPath[2]));	
		indexTagsWriter = new IndexWriter(dir,iwcT);

		FSDirectory taxoDir = FSDirectory.open(Paths.get("facets"));
		fconfig.setMultiValued("Tag", true);
		taxoWriter = new DirectoryTaxonomyWriter(taxoDir);
	}

	
	public void indexDocs (){
		try{
			reader.read(docPath[1],"Answers",this);

		}catch(IOException e){
			System.out.println(e.getMessage());
		}

		try{
			reader.read(docPath[2],"Tags",this);

		}catch(IOException e){
			System.out.println(e.getMessage());
		}

		try{
			Directory dir = FSDirectory.open(Paths.get(indexPath[1]));
	 		IndexReader reader = DirectoryReader.open(dir);		
			searcherAns = new IndexSearcher(reader);

			dir = FSDirectory.open(Paths.get(indexPath[2]));
	 		reader = DirectoryReader.open(dir);		
			searcherTags = new IndexSearcher(reader);
		} catch( IOException e ){
			System.out.println("Error while open the searcherers: " + e.getMessage());
		}

		try{
			reader.read(docPath[0],"Questions",this);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}

	}




	public void addQuestion(String[] question) throws IOException{
		Document doc = new Document ();
			 
		Integer valor = Integer.decode(question[0]);
		// Store idQuestion on Lucene doc
		doc.add ( new IntPoint("ID",valor));
		doc.add ( new StoredField("ID", valor));

		// Store number of responses
		Query q = IntPoint.newExactQuery("IDParent", valor);
		BooleanClause bc = new BooleanClause(q,BooleanClause.Occur.MUST);
		BooleanQuery.Builder bqbuilder = new BooleanQuery.Builder();
		bqbuilder.add(bc);
		BooleanQuery bq = bqbuilder.build();
		TopDocs tdocs = searcherAns.search(bq,50);

		long responses = tdocs.totalHits;
		int topVotes = -999999;
		if(responses == 0){
			topVotes = 0;
		}
		for(ScoreDoc sd : tdocs.scoreDocs){
			Document d = searcherAns.doc(sd.doc);
			int votes = d.getField("Mark_store").numericValue().intValue();		
			if(votes > topVotes){
				topVotes = votes;
			}
		}

		doc.add(new IntPoint("Responses", toIntExact(responses)));
		doc.add(new NumericDocValuesField("Responses_sort", responses));
		doc.add(new NumericDocValuesField("TopVotedResponse", (long) topVotes));


		// Store tags
		q = IntPoint.newExactQuery("ID_store", valor);
		bc = new BooleanClause(q,BooleanClause.Occur.MUST);
		bqbuilder = new BooleanQuery.Builder();
		bqbuilder.add(bc);
		bq = bqbuilder.build();
		tdocs = searcherTags.search(bq,50);

		for(ScoreDoc sd : tdocs.scoreDocs){
			Document d = searcherAns.doc(sd.doc);
			String tag = d.get("Tag");		
			doc.add(new FacetField ("Tag", tag));
		}


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
			indexQuestionWriter.addDocument(fconfig.build(taxoWriter, doc));
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
		//System.out.println(answer[4]);
		//doc.add(new TextField("Mark", answer[4], Store.YES));
		doc.add ( new IntPoint("Mark",Integer.decode(answer[4])));
		doc.add(new StoredField("Mark_store", Integer.decode(answer[4])));

		// Store accepted 0(No)/1(Yes)
		valor = answer[5] == "FALSE" ? 0 : 1;
		doc.add(new IntPoint("Accepted", valor));

		// Store title
		doc.add(new IntPoint( "IDParent", Integer.decode(answer[3])));


		// Insert doc in Index
		try{
			indexAnswerWriter.addDocument(doc);
		}catch( IOException e ){
			System.out.println("Error adding document to index.");
		}	
	}




	public void addTag(String[] cadena){
		Document doc = new Document ();
			 
		Integer valor = Integer.decode(cadena[0]);
		// Store idQuestion on Lucene doc
		doc.add ( new IntPoint("ID",valor));
		doc.add ( new StoredField("ID_store", valor));

		// Store body on Lucene doc
		doc.add(new TextField( "Tag", cadena[1], Store.YES));

		// Insert doc in Index
		try{
			indexTagsWriter.addDocument(doc);
		}catch( IOException e ){
			System.out.println("Error adding document to index.");
		}
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
