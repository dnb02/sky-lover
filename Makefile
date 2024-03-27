server: 
	javac -cp './gson-2.10.1.jar:' APOD*.java	
	java -cp './gson-2.10.1.jar:' APODService.java	
gui:
	javac -cp './gson-2.10.1.jar:.' ApodGui.java
	javac -cp './gson-2.10.1.jar:jsoup-1.17.2.jar:.' NewsScraper.java
	javac -cp './gson-2.10.1.jar:jsoup-1.17.2.jar:.' MainScreen.java
	java -cp './gson-2.10.1.jar:jsoup-1.17.2.jar:.' MainScreen

clean:
	rm -rf *.class
