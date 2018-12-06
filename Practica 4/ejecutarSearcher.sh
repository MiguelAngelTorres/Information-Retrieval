
javac -cp lucene-7.1.0/analysis/common/lucene-analyzers-common-7.1.0.jar:lucene-7.1.0/core/lucene-core-7.1.0.jar:jsoup-1.11.3.jar:lucene-7.1.0/facet/lucene-facet-7.1.0.jar:commons-lang3-3.8.1.jar:opencsv-4.3.2.jar:lucene-7.1.0/queryparser/lucene-queryparser-7.1.0.jar SearchInterface.java Searcher.java 

java -cp lucene-7.1.0/analysis/common/lucene-analyzers-common-7.1.0.jar:.:lucene-7.1.0/core/lucene-core-7.1.0.jar:.:jsoup-1.11.3.jar:.:lucene-7.1.0/facet/lucene-facet-7.1.0.jar:.:commons-lang3-3.8.1.jar:.:opencsv-4.3.2.jar:.:lucene-7.1.0/queryparser/lucene-queryparser-7.1.0.jar:. SearchInterface $1
