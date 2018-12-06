# Information-Retrieval
Work for the subject Information-Retrieval (UGR)

## Práctica 1
Look for information about some topics we will work with in the subject. Report made with LaTex.

## Práctica 2 (Tika)
Read the files in a directory given and extract the text from the interesting files (PDF, HTML, XML, EPUB, WORD, EXCELL). Made with Tika (Java). This module uses gnuplot(Linux) for word frecuency plots called from Javaplot-0.5.0, which must be downloaded (https://sourceforge.net/projects/gnujavaplot/) and place on the directory where .sh file is placed. In addition, tika-app-1.18.jar has been used, you can download it from https://tika.apache.org/download.html .

## Práctica 3 (Tika + Lucene)
For the text extracted in Práctica 2, test multiple Lucene's analyzers comparing the most frecuent tokens and the total tokens. Besides, implemets a custom analyzer of Lucene (CustomAnalyzer) in order to analyze c/java code-like files. It extracts variable names and the called methods but removes reserverd words, "strings" and numbers. Lucene can be found in https://lucene.apache.org/core/, the code asumes the 7.0.1 version is used.

## Práctica 4 (Lucene, a complete Stack Overflow search engine)
Given a set of questions, answers and tags from Stack Overflow (https://www.kaggle.com/stackoverflow/rquestions) we build a Lucene Index to search over them. It was thought for being a console like program, but an user inteface could be developed (not the aim of this subject).
For execute, first execute 'ejecutarIndexer.sh' for creating the indexes. Note that some directories should be created before (watch Indexer.java for change them). Then, the searcher can be used by executing 'ejecutarSearcher.sh'. A friendly interface will be shown where you can prepare multiple clauses questions and throw them when the question is finished.
