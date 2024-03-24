apod: 
	javac -cp './gson-2.10.1.jar' APOD.java	
gui:
	javac -cp './gson-2.10.1.jar:.' Gui.java
run: apod gui
	java -cp './gson-2.10.1.jar:.' Gui
clean:
	rm -rf *.class
