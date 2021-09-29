@echo off

echo Installing jasperreports-6.17.0-landclan.jar to local Maven repository...
call mvn install:install-file -Dfile="dist/jasperreports-6.17.0-landclan.jar" -DpomFile="pom.xml"

echo Updating landclandev01 Maven repository...
call robocopy "C:\mvn-repository\net\sf\jasperreports\jasperreports\6.17.0-landclan" ^
  "Y:\mvn-repository\net\sf\jasperreports\jasperreports\6.17.0-landclan" /E