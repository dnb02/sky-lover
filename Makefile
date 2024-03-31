SCCFLAGS = -cp 'classes/server:./jars/jsoup-1.17.2.jar:./jars/gson-2.10.1.jar:./jars/solarpositioning-2.0.2.jar:./jars/slf4j-api-1.7.36.jar:./jars/sqlite-jdbc-3.45.2.0.jar:.'

GCCFLAGS = -cp 'classes/client:./jars/jsoup-1.17.2.jar:./jars/gson-2.10.1.jar:./jars/solarpositioning-2.0.2.jar:./jars/slf4j-api-1.7.36.jar:./jars/sqlite-jdbc-3.45.2.0.jar:.'
gui: 
	javac $(GCCFLAGS) -d classes ./client/MainScreen.java	
	java $(GCCFLAGS) ./client/MainScreen.java
	


CombinedServer.class: 
	javac $(SCCFLAGS) -d classes ./server/CombinedServer.java
server: CombinedServer.class
	java $(SCCFLAGS) ./server/CombinedServer.java

clean:
	rm -rf *.class
	rm -rf ./client/*.class
	rm -rf ./server/*.class
