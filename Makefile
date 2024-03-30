CCFLAGS = -cp '../jars/jsoup-1.17.2.jar:../jars/gson-2.10.1.jar:../jars/solarpositioning-2.0.2.jar:../jars/slf4j-api-1.7.36.jar:../jars/sqlite-jdbc-3.45.2.0.jar:.:..'
dum:
	javac $(CCFLAGS) SolarPositionCalculator.java
	java $(CCFLAGS) SolarPositionCalculator.java
server: 
	javac $(CCFLAGS) ./server/CombinedServer.java
	java $(CCFLAGS) ./server/CombinedServer.java
gui: 
	javac $(CCFLAGS) ./client/MainScreen.java	

spos:
	javac -cp './jars/solarpositioning-2.0.2.jar:./jars/slf4j-api-1.7.36.jar:./jars/sqlite-jdbc-3.45.2.0.jar:.' SolarPositionCalculator.java
	java -cp './jars/solarpositioning-2.0.2.jar:./jars/slf4j-api-1.7.36.jar:./jars/sqlite-jdbc-3.45.2.0.jar:.' SolarPositionCalculator

db:
	javac -cp './jars/slf4j-api-1.7.36.jar:./jars/sqlite-jdbc-3.45.2.0.jar:.' Try.java
	java -cp './jars/slf4j-api-1.7.36.jar:./jars/sqlite-jdbc-3.45.2.0.jar:.' Try
	
clean:
	rm -rf *.class
