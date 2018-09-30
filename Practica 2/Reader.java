import java.util.Iterator;
import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.tika.Tika;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.xml.XMLParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.metadata.Metadata;


import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.Link;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.List;

import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageResult;


public class Reader {

	public static String auxText;
	public static Metadata met = new Metadata();
	public static List<Link> links = new LinkedList<Link>();


	public static String identifyLanguage() throws IOException {
		LanguageDetector identifier  = new  OptimaizeLangDetector().loadModels();
		LanguageResult idioma = identifier.detect(auxText);
		return idioma.getLanguage();
	}


	public static void printMap(HashMap mp) {
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry)it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
	}




/*
		Parser parser = new AutoDetectParser();
		Metadata metadata = new Metadata();



		InputStream stream = new FileInputStream(file);
		parser.parse(stream, handler, metadata,new ParseContext());

		List<Link> lista  = new LinkedList<Link>();
		lista = handler.getLinks();
		for (Link enlace : lista) {
		System.out.println("lin k:"+enlace.toString());

*/


	public static void processPDF(File file) throws Exception {
		FileInputStream inputstream = new FileInputStream(file);
		BodyContentHandler handler = new BodyContentHandler(-1);
		LinkContentHandler linkhandler = new LinkContentHandler();

		TeeContentHandler teeHandler = new TeeContentHandler(linkhandler, handler) ;

		ParseContext pcontext = new ParseContext();
		PDFParser parser = new PDFParser();
	
		parser.parse(inputstream, teeHandler, met, pcontext);
			
		// Getting the content of the document
		auxText = handler.toString();
	}



	public static void processXML(File file) throws Exception {
		FileInputStream inputstream = new FileInputStream(file);		
		ParseContext pcontext = new ParseContext();
		ToXMLContentHandler handler = new ToXMLContentHandler();
		Parser parser = new XMLParser();

		parser.parse(inputstream, handler, met, pcontext);

		// Getting the content of the document
		auxText = handler.toString();
	}



	public static void main(String[] args) throws Exception {
		// Create Tika instance
		Tika tika = new Tika();

		System.out.println("_____________");
		System.out.println("_____________");


		for (String file : args) {	
			File f = new File(file);
			String type = tika.detect(f);

			if(type.contains("pdf")){

				processPDF(f);

			}else if(type.contains("")){

				processXML(f);

			}else{

				InputStream is = new FileInputStream(file);
				auxText = tika.parseToString(f);
			}



			System.out.println("\n Metadatos del archivo. \n");
			System.out.println(f.getAbsolutePath());
			System.out.println(file +":::"+type);
			System.out.println("lenguaje::"+identifyLanguage());

			// Show content text
			//System.out.println(auxText);

			System.out.println("\n Frecuencias de las palabras \n");
			     



			FrecuenCounter freCount = new FrecuenCounter(auxText);
			HashMap<String,Integer> frec = freCount.getWordFrecuency();
			//printMap(frec);


   
		}
	}
}

 
