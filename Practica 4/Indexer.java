import org.apache.lucene.analysis.Analyzer ;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class Indexer{
	String indexPath = "./index";
	String docPath = "./DataSet";
	boolean create = true;
	private IndexWriter writer ;
	

	Indexer(){
	// TO DO



	}


	public static void main ( String[] args ) {
		Similarity similarity = new ClassicSimilarity(); // BM25 by default

		// Create an analyzer per field, and set default as WhitespaceAnalyzer
		PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrappwer(new WhitespaceAnalyzer(), analyzerSet());

		IndiceSimple baseline = new IndiceSimple() ;

		baseline.configureIndex (analyzer, similarity);
		baseline.indexDocs() ;
		baseline.close();
	}


	public Map<String, Analyzer> analyzerSet(){
	// TO DO



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
		for(elementos d : docPath){
			String cadena = leerDocumento (d);
			Document doc = new Document ();
			
			// Extract ID from String
			Integer start = . . . 
			Integer end = . . . 
			String aux = cadena.substring (start,end);
			Integer valor = Integer.decode(aux);
			// Store field on Lucene doc
			doc.add ( new IntPoint("ID",valor));
			doc.add ( new StoredField("ID", valor));

			// Extract Body from string
			start =
			end = 
			Stringcuerpo = cadena.substring(start,end);
			// Store field on Lucene doc
			doc.add(new TextField( "Body", body, Field.Store.YES));


			// Insert doc in Index
			writer.addDocument(doc);

			// If already exist, update with:
			// writer.updateDocument(new Term("ID", valor.toString()),doc);
		}
	}

	public void close ( ) {
		try {
			writer.commit ( ) ;
			writer . close () ;
		}catch ( IOException e ) {
			System.out.println("Error closing the index.");
		}
	}
}
