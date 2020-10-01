import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day21 {
    public static void main(String[] args) throws IOException {
        var rules = Files.readAllLines(Paths.get("input21.txt"))
                .stream()
                .filter(Predicate.not(String::isEmpty))
                .map(s -> s.split(" => "))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));
        rules = addPermutations(rules);

        // Part 01
        execute(5, rules);
        // Part 02
        execute(18, rules);
    }

    private static void execute(int count, Map<String, String> rules) {
        var matrix = ".#./..#/###";
        for (int i = 0; i < count; i++) {
            matrix = transform(matrix, rules);
        }
        System.out.println(matrix.replaceAll("[^#]", "").length());
    }

    private static String transform(String matrix, Map<String, String> rules) {
        var mapped = splitMatrix(matrix)
                .stream()
                .map(rules::get);

        return joinMatrix(mapped);
    }

    private static String joinMatrix(Stream<String> mapped) {
        var groups = mapped.map(s -> s.split("/"))
                .toArray(String[][]::new);
        var divisor = groups[0][0].length();
        var size = (int) Math.sqrt(groups.length * divisor * divisor);
        var builder = new StringBuilder();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                builder.append(groups[(y / divisor) * (size / divisor) + x / divisor][y % divisor].charAt(x % divisor));
            }
            if (y != size - 1) {
                builder.append("/");
            }
        }
        return builder.toString();
    }

    private static List<String> splitMatrix(String matrix) {
        var lines = matrix.split("/");
        var divisor = lines.length % 2 == 0 ? 2 : 3;
        var groupSize = lines.length / divisor;
        var groups = (int) Math.pow(groupSize, 2);

        var output = new ArrayList<String>();
        for (int i = 0; i < groups; i++) {
            var builder = new StringBuilder();
            for (int y = 0; y < divisor; y++) {
                for (int x = 0; x < divisor; x++) {
                    builder.append(lines[i / groupSize * divisor + y].charAt(i % groupSize * divisor + x));
                }
                if (y != divisor - 1) {
                    builder.append("/");
                }
            }
            output.add(builder.toString());
        }
        return output;
    }

    private static Map<String, String> addPermutations(Map<String, String> rules) {
        var result = new HashMap<String, String>();

        for (var entry : rules.entrySet()) {
            var group = entry.getKey();
            result.put(group, entry.getValue());

            for (int i = 0; i < 4; i++) {
                group = mirror(group);
                result.put(group, entry.getValue());

                group = flip(group);
                result.put(group, entry.getValue());
            }
        }
        return result;
    }

    private static String mirror(String group) {
        var characters = group.toCharArray();
        var output = new StringBuilder();
        if (group.length() >= 9) {
            // 3x3 grid, 3 and 7 are slashes, separating lines
            /*
            0 1 2 /    0 4 8 /
            4 5 6 / -> 1 5 9 /
            8 9 10     2 6 10
             */
            output.append(characters[0])
                    .append(characters[4])
                    .append(characters[8])
                    .append("/")
                    .append(characters[1])
                    .append(characters[5])
                    .append(characters[9])
                    .append("/")
                    .append(characters[2])
                    .append(characters[6])
                    .append(characters[10]);
        } else {
            // 2x2 grid, 2 is a slash
            /*
            0 1 / -> 0 3 /
            3 4      1 4
             */
            output.append(characters[0])
                    .append(characters[3])
                    .append("/")
                    .append(characters[1])
                    .append(characters[4]);
        }

        return output.toString();
    }

    // Perform vertical flip
    private static String flip(String group) {
        var characters = group.toCharArray();
        var output = new StringBuilder();
        if (group.length() >= 9) {
            output.append(characters[8])
                    .append(characters[9])
                    .append(characters[10])
                    .append("/")
                    .append(characters[4])
                    .append(characters[5])
                    .append(characters[6])
                    .append("/")
                    .append(characters[0])
                    .append(characters[1])
                    .append(characters[2]);

        } else {
            output.append(characters[3])
                    .append(characters[4])
                    .append("/")
                    .append(characters[0])
                    .append(characters[1]);
        }
        return output.toString();
    }
}
