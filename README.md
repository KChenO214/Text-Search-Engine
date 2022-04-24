README

Program takes in a queries of words along with weight of words if any is applied and a directory where your files are stored to be read and a destination directory that you want files created by the program to be store. The default weight for each query word is 1.
Total corpus size, top 10 most relevant documents along with top 10 of each BM25 terms for each document are return if any exists.

*Bash file is written to work on Ubuntu linux terminal
**Has a variable to store the class path of the jsoup jar file to be used
**Takes in multiple arguments (minimum 3: a query term, directory of html files, and a directory to be written to):
	1 query term weight 1 (how important is the term in comparison to the other terms)
	2 query term 1
	3 query term weight 2
	4 query term 2
	5 query term weight N
	6 query term N
	7 What the directory of the files you want to read from
	8 What the directory is that you want to write to

Steps to run program
	In the bash script retrieve.sh have the CLASSPATH variable set to the jsoup jar file directory as well as setting javac and java to the main class directory
	Open the terminal in the project folder
	Run: bash retrieve.sh weight_1 query_term1 weight_2 query_term2 ... weight_(n) query_term(n) directory_of_files_to_read directory_to_write_to
