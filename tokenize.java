package main.java;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class tokenize {

    public static void main(String[] args) {
        //Creates new parser object to go through each file
        parseDocument parser = new parseDocument();

        //Array stores the list of file names and file directory retrieved from the user specified directory
        File[] fileDirectory;
        String[] fileName;

        ThreadMXBean tm = ManagementFactory.getThreadMXBean();
        long[] cpuTime;
        long[] elapsedTime;

        try {
            //Creates a file object to get directory
            //File files = new File("/home/dafvck/IdeaProjects/TokenParser/files");
            File files = new File("/home/dafvck/Desktop/IdeaProjects/Parser/tokenTestOriginal");
            //File files = new File(args[0]);

            //Stores the individual file directory and the name of the files
            fileDirectory = files.listFiles();
            fileName = files.list();

            cpuTime = new long[fileDirectory.length + 1];
            cpuTime[0] = tm.getCurrentThreadCpuTime();
            elapsedTime = new long[fileDirectory.length + 1];
            elapsedTime[0] = System.nanoTime();

            //Goes through the files, tokenize, calculate frequency, and creates new files of tokens for each file to user selected directory
            for (int i = 0; i < fileDirectory.length; i++) {
                //parser.setDirectory(fileDirectory[i].toString(), "/home/dafvck/IdeaProjects/TokenParser/fileDestination", fileName[i], true);
                parser.setDirectory(fileDirectory[i].toString(), "/home/dafvck/Desktop/IdeaProjects/Parser/tokenTestDestination", fileName[i], true);
                //parser.setDirectory(fileDirectory[i].toString(), args[1], fileName[i]);

                //Tokenizes the words in each file, calculate frequencies, and write to new files
                parser.parseText();

                cpuTime[i + 1] = tm.getCurrentThreadCpuTime();
                elapsedTime[i + 1] = System.nanoTime();
            }

            long h = cpuTime[0];
            for(long l: cpuTime){
                System.out.println((double) (l - h) / 1000000000);
            }

            System.out.println("------");

            long i = elapsedTime[0];
            for(long l: elapsedTime){
                System.out.println((double) (l - i) / 1000000000);
            }

            parser.totalFreq();
        } catch (Exception e) {
            System.out.println("Invalid/Empty Directory");
        }
    }
}

class parseDocument {
    //Stores the directory, name, test for a file to be parsed
    private String parsedText = "";
    private String directory = "";
    private String fileName = "";

    //File object to use to write to new directory given by user
    private String destination;

    private int totalWords = 0;

    //Used to store the tokens retrieved from the text
    HashMap<String, MutableInteger> tokens = new HashMap<String, MutableInteger>();

    private boolean newFile = false;

    //The most used 101 words in the english language
    private final List<String> commonWords = Arrays.asList("A", "ABOUT", "AFTER", "ALL", "ALSO", "AN", "AND", "ANY",
            "AS", "AT", "BACK", "BE", "BECAUSE", "BUT", "BY", "CAN", "COME", "COULD", "DAY", "DO", "EVEN", "FIRST",
            "FOR", "FROM", "GET", "GIVE", "GO", "GOOD", "HAVE", "HE", "HER", "HIM", "HIS", "HOW", "I", "IF", "IN",
            "INTO", "IS", "IT", "ITS", "JUST", "KNOW", "LIKE", "LOOK", "MAKE", "ME", "MOST", "MY", "NEW", "NO", "NOT",
            "NOW", "OF", "ON", "ONE", "ONLY", "OR", "OTHER", "OUR", "OUT", "OVER", "PEOPLE", "SAY", "SEE", "SHE", "SO",
            "SOME", "TAKE", "THAN", "THAT", "THE", "THEIR", "THEM", "THEN", "THERE", "THESE", "THEY", "THINK", "THIS",
            "TIME", "TO", "TWO", "UP", "US", "USE", "WANT", "WAY", "WE", "WELL", "WHAT", "WHEN", "WHICH", "WHO", "WILL",
            "WITH", "WORK", "WOULD", "YEAR", "YOU", "YOUR");

    //Gets the file path, written file destination, the file name to be created based on previous file name, and boolean
    //to start the creation of a new file
    public void setDirectory(String directory, String destination, String fileName, boolean newFile) {
        this.directory = directory;
        this.destination = destination;
        this.fileName = fileName.replace(".", "");
        this.newFile = newFile;
    }

    //Parses the text in the document and only returns the text
    public void parseText() throws IOException {
        File text = new File(directory);

        //Gets all the text components from the parsed file into a document object
        Document doc = Jsoup.parse(text, "UTF-8");

        //Converts the document object to a string to get the text
        //Change to all upper case letters and remove all characters that are not from A to Z
        parsedText = doc.text();
        parsedText = parsedText.toUpperCase();
        parsedText = parsedText.replaceAll("['/\".:]", " ");
        parsedText = parsedText.replaceAll("[^A-Z ]", "").trim();

        //Stores tokens in hashmap and write to file
        countWords();
    }

    //Sort all tokens into hash map to use to calculate token frequency, also create new file and sent to directory
    public void countWords() throws IOException {
        //Trims the string and store it in an array to be iterated through
        String[] tokenArray = parsedText.trim().split("\\s+");

        //If space skip line and keep going in string until character is found
        //If the character is not any of the letters of the alphabet do nothing
        for (String word : tokenArray) {
            if(!commonWords.contains(word) && word.length() > 1) {
                //If the character is part of the alphabet add to the doc
                if (tokens.get(word) != null) {
                    tokens.get(word).increaseCount();
                    addToTokenizedDoc(word);

                    //Adds to the total number of words
                    totalWords++;

                    //Used to make sure no new files are created when on same file
                    newFile = false;
                } else if (word.equals("")) {
                    //Do nothing since there is no words
                } else {
                    //Creates new mutable integer object and token to be inserted into the hash map
                    MutableInteger count = new MutableInteger();
                    tokens.put(word, count);
                    addToTokenizedDoc(word);

                    //Adds to the total number of words
                    totalWords++;

                    //Used to make sure no new files are created when on same file
                    newFile = false;
                }
            }
        }
    }

    //Created final document for frequency and alphabetical sorting orders
    public void totalFreq() throws IOException {
        File path1 = new File(destination.substring(destination.length() - 1).equals("/")
                ? destination + "sortByToken" + ".txt" : destination + "/" + "sortByToken" + ".txt");
        path1.createNewFile();

        String[][] byToken = new String[tokens.size()][2];
        int[] byCounts = new int[tokens.size()];

        //Gets all the key from the hash map into the array to be sorted
        int pos = 0;
        for(String str: tokens.keySet()){
            byToken[pos][0] = str;
            byToken[pos][1] = Integer.toString(pos);

            byCounts[pos] = tokens.get(str).getCount();

            pos++;
        }

        /*Map<String, Integer> unSortedHashMap = new HashMap<>();
        for(String word: tokens.keySet()){
            unSortedHashMap.put(word, tokens.get(word).getCount());
        }
        //
        Map<String, Integer> sortedHashMap = unSortedHashMap.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> { throw new AssertionError(); },
                        LinkedHashMap::new
                ));

        sortedHashMap.entrySet().forEach(System.out::println);*/
        //

        //Sorts the array into alphabetical order
        Arrays.sort(byToken);

        for (int i = 0; i < byToken.length; i++) {
            FileWriter fw = new FileWriter(path1.getAbsolutePath(), !newFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(byToken[i] + ": " + tokens.get(byToken[i]).getCount());
            bw.newLine();
            bw.close();
        }

        /*File path2 = new File(destination.substring(destination.length() - 1).equals("/")
                ? destination + "sortByFreq" + ".txt" : destination + "/" + "sortByFreq" + ".txt");
        path2.createNewFile();

        for(String str: tokens.keySet()){

        }*/
    }

    //Populate to documents
    public void addToTokenizedDoc(String word) throws IOException {
        //Creates/gets the path to the new file
        File path = new File(destination.substring(destination.length() - 1).equals("/")
                ? destination + fileName + ".txt" : destination + "/" + fileName + ".txt");

        //Creates new file based on if we are on a new file path set in setDirectory
        if(newFile) {
            path.createNewFile();
        }

        FileWriter fw = new FileWriter(path.getAbsolutePath(), !newFile);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(word);
        bw.newLine();
        bw.close();
    }
}

//Class to store the number of occurrence of a word
class MutableInteger {
    int count = 1;

    public void increaseCount() {
        count++;
    }

    public int getCount() {
        return count;
    }
}