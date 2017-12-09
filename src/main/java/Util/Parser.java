package Util;

import Controller.Main;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;

public class Parser {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    private static final String INSULTS = "db/insultsList.txt";
    private static final String INSULTSOUT = "db/insultsOutput.csv";

    private static final String COMPLIMENTS = "db/compliments.txt";
    private static final String COMPLIMENTSOUT = "db/complimentsOutput.csv";

    private static final String JOKES = "db/jokes.txt";
    private static final String JOKESOUT = "db/jokesOutput.csv";

    public static void main(String[] args) {
        File file = new File(INSULTS);
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        removeNumbers(file, printWriter, bufferedReader);
    }

    public static void removeNumbers(File file, PrintWriter printWriter, BufferedReader bufferedReader) {
        try {
            printWriter = new PrintWriter(INSULTSOUT);
//            Scanner scanner = new Scanner(file);
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine().trim();
////                String newLine = line.replaceFirst("[0-9]+.", "");
//                printWriter.println(line);
////                printWriter.append(NEW_LINE_SEPARATOR);
//            }
//            scanner.close();
//            printWriter.close();

            //this is here because sometimes the scanner doesnt work properly
            //the scanner wasn't reading in the file completely so, you would have
            //to use the buffered reader sometimes.
            bufferedReader = new BufferedReader(new FileReader(file));
            String available;
            while ((available = bufferedReader.readLine()) != null) {
                System.out.println(available);
                String temp = available.trim();
                printWriter.write(temp.replaceAll(",", "."));
                printWriter.append(NEW_LINE_SEPARATOR);
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Main.getLogger().log(Level.INFO, "The file wasn't found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
