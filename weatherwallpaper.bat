cd C:\code\weatherBackground
call mvn clean compile
call mvn exec:java "-Dexec.mainClass=Main"