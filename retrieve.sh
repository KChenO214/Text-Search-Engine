#!/bin/bash
#Replace ~ with path of project
#Replace export with your terminal variable declaration
export CLASSPATH=~/Text-Search-Engine/jsoup-1.14.3.jar:$CLASSPATH
javac ~/Text-Search-Engine/src/main/java/tokenize.java
java ~/Text-Search-Engine/src/main/java/tokenize.java "${@:1}"
