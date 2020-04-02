package search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length < 2 || !args[0].equals("--data")) {
            throw new IllegalArgumentException("Missing --data argument (e.g. java -jar search-engine.jar --data data.txt)");
        }

        String filePath = args[1];
        String[] data = Files.readAllLines(Paths.get(filePath)).toArray(new String[0]);

        Map<String, List<Integer>> invertedIndex = buildInvertedIndex(data);

        Scanner scanner = new Scanner(System.in);
        SearchEngine searchEngine = new InvertedIndexSearchEngine(invertedIndex);

        showMenu();
        int option;
        while ((option = Integer.parseInt(scanner.nextLine())) != 0) {
            System.out.println();
            switch (option) {
                case 1:
                    System.out.println("Select a matching strategy: ALL, ANY, NONE");
                    SearchType searchType = SearchType.valueOfType(scanner.nextLine().toUpperCase());
                    if (searchType == null) {
                        System.out.println("Incorrect option! Try again.\n");
                        break;
                    }
                    System.out.println("Enter a name or email to search all suitable people.");
                    String query = scanner.nextLine();
                    List<String> results = searchEngine.search(data, query, searchType);
					System.out.println();
                    System.out.println(printResults(results));
                    break;
                case 2:
                    printData(data, "=== List of people ===");
                    break;
                default:
                    System.out.println("Incorrect option! Try again.\n");
                    break;
            }
            showMenu();
        }

        System.out.println("Bye!");
    }

    private static Map<String, List<Integer>> buildInvertedIndex(String[] data) {
        Map<String, List<Integer>> invertedIndex = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            String[] words = data[i].split("\\s+");
            int index = i;
            for (String word: words) {
                invertedIndex.compute(word.toLowerCase(), (k, v) -> {
                    if (v == null) {
                        List<Integer> list = new ArrayList<>();
                        list.add(index);
                        return list;
                    }
                    v.add(index);
                    return v;
                });
            }
        }
        return invertedIndex;
    }

    private static void showMenu() {
        System.out.println("=== MENU ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all people");
        System.out.println("0. Exit");
    }

    private static String printResults(List<String> results) {
        if (results.isEmpty()) {
            return "No matching people found.\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(results.size() == 1 ? "1 person found:" : results.size() + " persons found:");
        sb.append('\n');
        results.forEach(line -> {
            sb.append(line);
            sb.append('\n');
        });
        return sb.toString();
    }

    private static <T> void printData(T[] data, String title) {
        System.out.println(title);
        for (T el : data) {
            System.out.println(el);
        }
        System.out.println();
    }
}
