# Information-Retrieval
Work for the subject Information-Retrieval (UGR)

## Práctica 2 (Tika)
Read the files in a directory given and extract the text from the interesting files (PDF, HTML, XML, EPUB, WORD, EXCELL). Made with Tika (Java). This module uses gnuplot(Linux) for word frecuency plots, in addition, Javaplot assets are compressed. Uncompression is needed for run the .sh file.

## Práctica 3 (Tika + Lucene)
For the text extracted in Práctica 2, test multiple Lucene's analyzers comparing the most frecuent tokens and the total tokens. Besides, implemets a custom analyzer of Lucene (CustomAnalyzer) in order to analyze c/java code-like files. It extracts variable names and the called methods but removes reserverd words, "strings" and numbers.
