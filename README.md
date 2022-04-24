README

Program takes in a queries of words along with weight of words if any is applied and a directory where your files are stored to be read and a destination directory that you want files created by the program to be store. The default weight for each query word is 1.
Total corpus size, top 10 most relevant documents along with top 10 of each BM25 terms for each document are return if any exists.

*Bash file is written to work on Ubuntu linux terminal
**Has a variable to store the class path of the jsoup jar file to be used
**Takes in 2 arguments:
	1 What the directory of the files you want to read from
	2 What the directory is that you want to write to

Steps to run program
	In the bash script retrieve.sh have the CLASSPATH variable set to the jsoup jar file directory as well as setting javac and java to the main class directory
	Open the terminal in the project folder
	Run: bash retrieve.sh weight_1 query_term1 weight_2 query_term2 ... weight_(n) query_term(n) directory_of_files_to_read directory_to_write_to