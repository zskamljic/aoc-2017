import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day07 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("input07.txt"));

        var entries = lines.stream()
                .map(Program::new)
                .collect(Collectors.toCollection(ArrayList::new));

        // Part 1
        var contained = entries.stream()
                .flatMap(entry -> entry.childNames.stream())
                .collect(Collectors.toList());

        var root = entries.stream()
                .filter(entry -> !contained.contains(entry.name))
                .findFirst();

        if (root.isEmpty()) return;

        System.out.println(root.get().name);

        // Part 2
        var rootNode = root.get();
        rootNode.createTree(entries);
        rootNode.calculateWeight();

        var correctedWeight = rootNode.findCorrectedWeight(0);
        System.out.println(correctedWeight);
    }

    private static class Program {
        String name;
        int weight;
        int calculatedWeight;
        List<String> childNames = new ArrayList<>();
        List<Program> children = new ArrayList<>();

        Program(String line) {
            var scanner = new Scanner(line.replaceAll("[^\\w\\d ]+", ""));
            name = scanner.next();
            weight = scanner.nextInt();
            while (scanner.hasNext()) {
                childNames.add(scanner.next());
            }
        }

        void createTree(ArrayList<Program> entries) {
            entries.stream()
                    .filter(entry -> childNames.contains(entry.name))
                    .forEach(children::add);
            children.forEach(child -> child.createTree(entries));
        }

        int findCorrectedWeight(int adjustment) {
            if (children.size() == 0) return 0;

            var values = new int[2];
            var valueCount = new int[2];

            for (var child : children) {
                var weight = child.calculatedWeight;

                if (values[0] == 0) {
                    values[0] = weight;
                } else if (values[0] != weight && values[1] == 0) {
                    values[1] = weight;
                }

                if (values[0] == weight) {
                    valueCount[0]++;
                } else if (values[1] == weight) {
                    valueCount[1]++;
                }
            }

            if (values[1] == 0) {
                return weight + adjustment;
            }

            int invalidWeight;
            int correctWeight;
            if (valueCount[0] == 1) {
                invalidWeight = values[0];
                correctWeight = values[1];
            } else {
                invalidWeight = values[1];
                correctWeight = values[0];
            }
            for (var child : children) {
                if (child.calculatedWeight == invalidWeight) {
                    return child.findCorrectedWeight(correctWeight - invalidWeight);
                }
            }
            return 0;
        }

        void calculateWeight() {
            calculatedWeight = weight;
            children.forEach(child -> {
                child.calculateWeight();
                calculatedWeight += child.calculatedWeight;
            });
        }
    }
}
