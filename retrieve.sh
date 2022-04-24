#!/bin/bash
#Replace ~ with path of project
#Replace export with your terminal variable declaration
export CLASSPATH=~/TokenParser/jsoup-1.14.3.jar:$CLASSPATH
javac ~/TokenParser/src/main/java/tokenize.java
java ~/TokenParser/src/main/java/tokenize.java "${@:1}"