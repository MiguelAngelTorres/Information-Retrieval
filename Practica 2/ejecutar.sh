
javac -cp tika-app-1.18.jar Reader.java FrecuenCounter.java
java -cp tika-app-1.18.jar:. Reader $1

rm Reader.class
rm FrecuenCounter.class
