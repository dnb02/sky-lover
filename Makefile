CC = javac
INC = './jars/jsoup-1.17.2.jar:./jars/gson-2.10.1.jar:./jars/solarpositioning-2.0.2.jar:./jars/slf4j-api-1.7.36.jar:./jars/sqlite-jdbc-3.45.2.0.jar:.'
CFLAGS = -cp $(INC) 

build:
	javac $(CFLAGS) ./*.java
client: build
	java $(CFLAGS) MainScreen   
server: build
	java $(CFLAGS) CombinedServer
clean:
	rm -rf *.class
