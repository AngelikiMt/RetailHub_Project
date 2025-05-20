With Maven:

    In terminal, project root Directory, run:

        -mvn compile

        -mvn exec:java

Without Maven:
    
    In terminal, in project/src/main/java, run:

        -javac -d bin -cp ".;mysql-connector-j-9.3.0.jar" *.java
        -java -cp ".;mysql-connector-j-9.3.0.jar;bin" Main
    