import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String DIRECTORY_PATH = "";
    private static final List<String> SEARCH_TEXTS = List.of(
            "First pattern",
            "belamqqqqq",
            "asdasdasdasdasd");
    private static final String OUTPUT_FILE = "output.txt";

    public static void searchForTextInJavaFiles(File directory, List<String> searchTexts, String outputFilePath) {
        if (!directory.isDirectory()) {
            System.out.println("Provided path is not a fund.");
            return;
        }

        List<String> results = new ArrayList<>();

        for (String searchText : searchTexts) {
            traverseDirectory(directory, searchText, results);
        }

        if (results.isEmpty()) {
            results.add("No matches found.");
        }

        try (FileWriter writer = new FileWriter(outputFilePath)) {
            for (String result : results) {
                writer.write(result + "\n\n");
            }
            System.out.println("Results written to " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void traverseDirectory(File directory, String searchText, List<String> results) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    traverseDirectory(file, searchText, results);
                } else if (file.getName().endsWith(".java")) {
                    searchInFile(file, searchText, results);
                }
            }
        }
    }

    private static void searchInFile(File file, String searchText, List<String> results) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean found = false;
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (found) {
                    sb.append(" ").append(line);
                    if (line.endsWith(";")) {
                        //EZT ATIRNI SZEBBRE
                        results.add("In file: " + file.getAbsolutePath() + "\nFound: " + sb.toString().trim());
                        found = false;
                        sb.setLength(0);
                    }
                } else if (line.contains(searchText) && line.endsWith(";")) {
                    results.add("In file: " + file.getAbsolutePath() + "\nFound: " + line);
                } else if (line.contains(searchText)) {
                    found = true;
                    sb.append(line.substring(line.indexOf(searchText)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        File directory = new File(DIRECTORY_PATH);
        searchForTextInJavaFiles(directory, SEARCH_TEXTS, OUTPUT_FILE);
    }
}
