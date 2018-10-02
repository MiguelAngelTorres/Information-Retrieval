
javac -cp JavaPlot-0.5.0/dist/JavaPlot.jar:tika-app-1.18.jar Reader.java FrecuenCounter.java Plotter.java
java -cp tika-app-1.18.jar:.:JavaPlot-0.5.0/dist/JavaPlot.jar:. Reader $1

rm Reader.class
rm FrecuenCounter.class
rm Plotter.class
