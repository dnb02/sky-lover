server: 
	javac -cp './gson-2.10.1.jar:' APOD*.java	
	java -cp './gson-2.10.1.jar:' APODService.java	
gui:
	javac -cp './gson-2.10.1.jar:.' Gui.java
	java -cp './gson-2.10.1.jar:.' Gui
clean:
	rm -rf *.class
