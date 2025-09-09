cd C:\code\weatherBackground
call mvn clean compile
call mvn exec:java "-Dexec.mainClass=Main"
cd src\main\c
call update.exe C:\code\weatherBackground\image\wallpaper.png