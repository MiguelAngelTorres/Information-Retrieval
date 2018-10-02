import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.tika.Tika;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.xml.XMLParser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.parser.epub.EpubParser;
import org.apache.tika.parser.microsoft.OfficeParser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.odf.OpenDocumentParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.metadata.Metadata;


import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;
import org.apache.tika.sax.ToHTMLContentHandler;
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
	public static ArrayList<String> files = new ArrayList<String> ();


	public static String identifyLanguage() throws IOException {
		LanguageDetector identifier  = new  OptimaizeLangDetector().loadModels();
		LanguageResult idioma = identifier.detect(auxText);
		return idioma.getLanguage();
	}


	public static void printList(List l) {
		Iterator it = l.iterator();
		while (it.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry)it.next();
			System.out.println(pair.getKey() + " = " + (-(Integer) pair.getValue()));
		}
	}



	public static void processPDF(File file) throws Exception {    ///  PDF
		FileInputStream inputstream = new FileInputStream(file);
		BodyContentHandler handler = new BodyContentHandler(-1);
		LinkContentHandler linkhandler = new LinkContentHandler();

		TeeContentHandler teeHandler = new TeeContentHandler(linkhandler, handler) ;

		ParseContext pcontext = new ParseContext();
		PDFParser parser = new PDFParser();
	
		parser.parse(inputstream, teeHandler, met, pcontext);
			
		// Getting the content of the document
		auxText = handler.toString();

		// Getting links of the document
		links = linkhandler.getLinks();
	}



	public static void processXML(File file) throws Exception {     ///   XML
		FileInputStream inputstream = new FileInputStream(file);		
		ToXMLContentHandler handler = new ToXMLContentHandler();
		LinkContentHandler linkhandler = new LinkContentHandler();

		TeeContentHandler teeHandler = new TeeContentHandler(linkhandler, handler);

		ParseContext pcontext = new ParseContext();		
		Parser parser = new XMLParser();

		parser.parse(inputstream, teeHandler, met, pcontext);

		// Getting the content of the document
		auxText = handler.toString();

		// Getting links of the document
		links = linkhandler.getLinks();
	}


	public static void processHTML(File file) throws Exception {    ///   HTML
		FileInputStream inputstream = new FileInputStream(file);		
		ToHTMLContentHandler handler = new ToHTMLContentHandler();
		LinkContentHandler linkhandler = new LinkContentHandler();

		TeeContentHandler teeHandler = new TeeContentHandler(linkhandler, handler) ;

		ParseContext pcontext = new ParseContext();
		Parser parser = new HtmlParser();

		parser.parse(inputstream, teeHandler, met, pcontext);

		// Getting the content of the document
		auxText = handler.toString();

		// Getting links of the document
		links = linkhandler.getLinks();
	}


	public static void processEPUB(File file) throws Exception {    ///   EPUB
		FileInputStream inputstream = new FileInputStream(file);		
		BodyContentHandler handler = new BodyContentHandler(-1);
		LinkContentHandler linkhandler = new LinkContentHandler();

		TeeContentHandler teeHandler = new TeeContentHandler(linkhandler, handler) ;

		ParseContext pcontext = new ParseContext();
		Parser parser = new EpubParser();

		parser.parse(inputstream, teeHandler, met, pcontext);

		// Getting the content of the document
		auxText = handler.toString();

		// Getting links of the document
		links = linkhandler.getLinks();
	}


	public static void processMSOFF(File file) throws Exception {   /// WORD 97-2003
		FileInputStream inputstream = new FileInputStream(file);		
		BodyContentHandler handler = new BodyContentHandler(-1);
		LinkContentHandler linkhandler = new LinkContentHandler();

		TeeContentHandler teeHandler = new TeeContentHandler(linkhandler, handler) ;

		ParseContext pcontext = new ParseContext();
		Parser parser = new OfficeParser();

		parser.parse(inputstream, teeHandler, met, pcontext);

		// Getting the content of the document
		auxText = handler.toString();

		// Getting links of the document
		links = linkhandler.getLinks();
	}


	public static void processOOXML(File file) throws Exception {   /// WORD 2007
		FileInputStream inputstream = new FileInputStream(file);		
		BodyContentHandler handler = new BodyContentHandler(-1);
		LinkContentHandler linkhandler = new LinkContentHandler();

		TeeContentHandler teeHandler = new TeeContentHandler(linkhandler, handler) ;

		ParseContext pcontext = new ParseContext();
		Parser parser = new OOXMLParser();

		parser.parse(inputstream, teeHandler, met, pcontext);

		// Getting the content of the document
		auxText = handler.toString();

		// Getting links of the document
		links = linkhandler.getLinks();
	}


	public static void processOD(File file) throws Exception {   /// OPENOFFICE 
		FileInputStream inputstream = new FileInputStream(file);		
		BodyContentHandler handler = new BodyContentHandler(-1);
		LinkContentHandler linkhandler = new LinkContentHandler();

		TeeContentHandler teeHandler = new TeeContentHandler(linkhandler, handler) ;

		ParseContext pcontext = new ParseContext();
		Parser parser = new OpenDocumentParser();

		parser.parse(inputstream, teeHandler, met, pcontext);

		// Getting the content of the document
		auxText = handler.toString();

		// Getting links of the document
		links = linkhandler.getLinks();
	}



	public static void getFilesOf(String dir){
		File folder = new File(dir);
		if(!folder.isFile()){
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					files.add(dir + "/" + listOfFiles[i].getName());
				} else if (listOfFiles[i].isDirectory()) {
					getFilesOf(dir + "/" + listOfFiles[i].getName());
				}
			}
		}else{
			files.add(dir);
		}
	}

	public static void main(String[] args) throws Exception {
		// Create Tika instance
		Tika tika = new Tika();

		System.out.println("_____________");
		System.out.println("_____________");

		// Write into a list every file in the directory given
		getFilesOf(args[0]);

		// Read every file in the list
		for (String file : files) {	
			met = new Metadata();
			File f = new File(file);
			String type = tika.detect(f);
			System.out.println(f.getName());
			if(type.contains("pdf")){

				processPDF(f);

			}else if(type.contains("html")){

				processHTML(f);

			}else if(type.contains("officedocument")){

				processOOXML(f);

			}else if(type.contains("xml")){

				processXML(f);

			}else if(type.contains("epub")){

				processEPUB(f);

			}else if(type.contains("word")){

				processMSOFF(f);

			}else if(type.contains("opendocument")){

				processOD(f);

			}else{

				InputStream is = new FileInputStream(file);
				auxText = tika.parseToString(f);

			}

			System.out.println("\n\n\n Archivo encontrado \n");

			System.out.println("\n Metadatos del archivo. \n");
			System.out.println(f.getAbsolutePath());
			System.out.println(file +":::"+type);
			System.out.println("Content-Encoding:::"+met.get("Content-Encoding"));
			System.out.println("lenguaje::"+identifyLanguage());

			// Show content text
			//System.out.println(auxText);

			System.out.println("\n Frecuencias de las palabras \n");
			     



			FrecuenCounter freCount = new FrecuenCounter(auxText);
			List frec = freCount.getWordFrecuency();
			//printList(frec);


			// Print links
			System.out.println("\n Links encontrados \n");
   		for(Link link : links){
				System.out.println(link.getText());
			}


			Plotter plot = new Plotter();
			//plot.plot(frec, file);
		}
	}
}

 
