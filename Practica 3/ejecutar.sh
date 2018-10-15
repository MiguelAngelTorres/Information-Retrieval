
javac -cp lucene-7.1.0/analysis/common/lucene-analyzers-common-7.1.0.jar:lucene-7.1.0/core/lucene-core-7.1.0.jar:tika-app-1.18.jar Analizador.java FrecuenCounter.java Statistic.java  Reader.java

java -cp lucene-7.1.0/analysis/common/lucene-analyzers-common-7.1.0.jar:.:lucene-7.1.0/core/lucene-core-7.1.0.jar:.:tika-app-1.18.jar:. Reader $1
