import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.pattern.PatternTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;

public class Analizador {

	private String text;

	Analizador(String t){
		text = t;
	}

	public LinkedList SimpleAndShingle() throws IOException {
		Analyzer an = new SimpleAnalyzer();
		List<String> splitedList = new ArrayList<String>();


		TokenStream stream  = an.tokenStream(null,  text);
		ShingleFilter sf = new ShingleFilter(stream,2,2);
		
		sf.reset();
		while (sf.incrementToken()) {
			splitedList.add(sf.getAttribute(CharTermAttribute.class).toString()) ;
		}
		sf.end();
		sf.close();

		String[] splited = new String[splitedList.size()];
		splited = splitedList.toArray(splited);

		FrecuenCounter tokenCounter = new FrecuenCounter(splited);
		LinkedList frec = tokenCounter.getWordFrecuency();

		return frec;
	}




	public LinkedList Simple() throws IOException {
		Analyzer an = new SimpleAnalyzer();
		List<String> splitedList = new ArrayList<String>();


		TokenStream stream  = an.tokenStream(null,  text);
		
		stream.reset();
		while (stream.incrementToken()) {
			splitedList.add(stream.getAttribute(CharTermAttribute.class).toString()) ;
		}
		stream.end();
		stream.close();

		String[] splited = new String[splitedList.size()];
		splited = splitedList.toArray(splited);

		FrecuenCounter tokenCounter = new FrecuenCounter(splited);
		LinkedList frec = tokenCounter.getWordFrecuency();

		return frec;
	}
    




	public LinkedList Standard() throws IOException {
		Analyzer an = new StandardAnalyzer();
		List<String> splitedList = new ArrayList<String>();


		TokenStream stream  = an.tokenStream(null,  text);
		
		stream.reset();
		while (stream.incrementToken()) {
			splitedList.add(stream.getAttribute(CharTermAttribute.class).toString()) ;
		}
		stream.end();
		stream.close();

		String[] splited = new String[splitedList.size()];
		splited = splitedList.toArray(splited);

		FrecuenCounter tokenCounter = new FrecuenCounter(splited);
		LinkedList frec = tokenCounter.getWordFrecuency();

		return frec;
	}




	public LinkedList Custom() throws IOException {
		 Analyzer an = CustomAnalyzer.builder()
   .withTokenizer(StandardTokenizerFactory.class)
   .addTokenFilter(StandardFilterFactory.class)
   .addTokenFilter(LowerCaseFilterFactory.class)
   .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words", "stopwords.txt", "format", "wordset")
   .build();
		List<String> splitedList = new ArrayList<String>();


		TokenStream stream  = an.tokenStream(null,  text);
		
		stream.reset();
		while (stream.incrementToken()) {
			splitedList.add(stream.getAttribute(CharTermAttribute.class).toString()) ;
		}
		stream.end();
		stream.close();

		String[] splited = new String[splitedList.size()];
		splited = splitedList.toArray(splited);

		FrecuenCounter tokenCounter = new FrecuenCounter(splited);
		LinkedList frec = tokenCounter.getWordFrecuency();

		return frec;
	}


	public LinkedList JavaAnalyzer() throws IOException { 
		Analyzer an = CustomAnalyzer.builder()
   .withTokenizer(PatternTokenizerFactory.class, "pattern", "(?<![\"\\.\\sA-z])[A-z\\.]+[0-9]*[A-z\\.]*(?![\"\\.\\sA-z])", "group", "0")
   .addTokenFilter(LowerCaseFilterFactory.class)
   .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words", "codestopwords.txt", "format", "wordset")
   .build();
		List<String> splitedList = new ArrayList<String>();


		TokenStream stream  = an.tokenStream(null,  text);
		
		stream.reset();
		while (stream.incrementToken()) {
			splitedList.add(stream.getAttribute(CharTermAttribute.class).toString()) ;
		}
		stream.end();
		stream.close();

		String[] splited = new String[splitedList.size()];
		splited = splitedList.toArray(splited);

		FrecuenCounter tokenCounter = new FrecuenCounter(splited);
		LinkedList frec = tokenCounter.getWordFrecuency();

		return frec;
	}
}
 
