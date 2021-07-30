import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlacklistChecker {
    public static void main(String[] args) {
        findMatchingNames("Osama Bin Laden", "src/names.txt", "src/noise_words.txt");
    }

    public static void findMatchingNames(String name, String blackListFileName, String noiseFileName) {
        List<String> blackListedNames = readFileToList(blackListFileName);
        List<String> noiseWords = readFileToList(noiseFileName);

        System.out.println("Input name: " + name);
        System.out.println("Partially matching names are: ");
        blackListedNames.forEach(n -> {
            if (hasBlacklistedName(n, name, noiseWords)) {
                System.out.println(n);
            }
        });
    }

    public static boolean hasBlacklistedName(String blackListedName, String name, List<String> noiseWords) {
        List<String> temp = new ArrayList<>();

        String filteredName = Arrays.stream(name.split(" "))
                .filter(c -> !noiseWords.contains(c))
                .sorted()
                .collect(Collectors.joining(" "));

        for (String nameComponent : blackListedName.strip().replace(",", "").split(" ")) {
            if (filteredName.toLowerCase().strip().contains(nameComponent.toLowerCase())) {
                temp.add(nameComponent.toLowerCase());
            }
        }

        if (temp.stream().sorted().collect(Collectors.joining(" ")).equals(filteredName.toLowerCase())) {
            return true;
        }
        return false;
    }

    public static List<String> readFileToList(String filename) {
        List<String> fileLines = new ArrayList<>();

        try {
            fileLines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileLines;
    }
}
