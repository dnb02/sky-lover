apod: 
	javac -cp '/home/deva/src/jars/gson-2.10.1.jar' APOD.java	
gui:
	javac -cp '/home/deva/src/jars/gson-2.10.1.jar:.' Gui.java
run: apod gui
	java -cp '/home/deva/src/jars/gson-2.10.1.jar:.' Gui

