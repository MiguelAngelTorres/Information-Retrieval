/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jhg
 */


 
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
import org.apache.tika.sax.Link;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.List;

import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageResult;


public class EjemploSimple {



	public static String identifyLanguage(String text) throws IOException {
		LanguageDetector identifier  = new  OptimaizeLangDetector().loadModels();
		LanguageResult idioma = identifier.detect(text);
		System.out.println("XXXXXX"+idioma.getLanguage());
		return idioma.getLanguage();
	}



	public static void process(File file) throws Exception {
		Parser parser = new AutoDetectParser();
		Metadata metadata = new Metadata();
		// The PhoneExtractingContentHandler will examine any characters for phone numbers before passing them
		// to the underlying Handler.
		LinkContentHandler handler = new LinkContentHandler();

		InputStream stream = new FileInputStream(file);
		System.out.println("seleccionando links ");
		try {
			parser.parse(stream, handler, metadata,new ParseContext());
		}finally{
			stream.close();
		}
		List<Link> lista  = new LinkedList<Link>();

		System.out.println(metadata.toString());
		System.out.println(handler.toString());
		/* lista = handler.getLinks();
		for (Link enlace : lista) {
		System.out.println("lin k:"+enlace.toString());
		 
		}
		* */      
	}



	public static void processPDF(File file, Metadata metadata, String[] text) throws Exception {
		FileInputStream inputstream = new FileInputStream(file);
		BodyContentHandler handler = new BodyContentHandler();
		ParseContext pcontext = new ParseContext();
		PDFParser parser = new PDFParser();
			
		parser.parse(inputstream, handler, metadata, pcontext);
			
		// getting the content of the document
		text[0] = handler.toString();
	}



	public static void processXML(File file, Metadata metadata, String[] text) throws Exception {
		FileInputStream inputstream = new FileInputStream(file);		
		ParseContext pcontext = new ParseContext();
		ToXMLContentHandler handler = new ToXMLContentHandler();
		Parser parser = new XMLParser();

		parser.parse(inputstream, handler, metadata, pcontext);

		// getting the content of the document
		text[0] = handler.toString();
	}



	public static void main(String[] args) throws Exception {
		// Creamos una instancia de Tika con la configuracion por defecto
		Tika tika = new Tika();
		Metadata met = new Metadata();
		String [] text = new String [] {"empty"};

		System.out.println("_____________");
		System.out.println("_____________");


		for (String file : args) {	
			File f = new File(file);
			System.out.println("\n Ruta completa del archivo y tipo \n\n" + f.getAbsolutePath());
			String type = tika.detect(f);
			System.out.println(file +":::"+type);

			if(type.contains("pdf")){

				processPDF(f, met, text);

			}else if(type.contains("")){

				processXML(f, met, text);

			}else{

				InputStream is = new FileInputStream(file);
				text[0] = tika.parseToString(f);
			}



			System.out.println("\n Metadatos del archivo. \n");
			String[] metNames = met.names();
			for(String name : metNames) {
				System.out.println(name + ":" + met.get(name));
			}

			System.out.println("\n Lenguaje del archivo. \n");
			System.out.println("lenguaje::"+identifyLanguage(text[0]));

			System.out.println("Contenido del archivo");
			//System.out.println(text[0]);        
		}
	}
}

 
