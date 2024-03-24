run: apod gui
	java -cp './gson-2.10.1.jar:.' Gui
apod: 
	javac -cp './gson-2.10.1.jar:' APOD.java	
	javac -cp './gson-2.10.1.jar:' APODService.java	
gui:
	javac -cp './gson-2.10.1.jar:.' Gui.java
clean:
	rm -rf *.class
