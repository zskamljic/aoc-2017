import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day02 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("input02.txt")).stream()
                .map(s -> s.split("\\s+"))
                .map(item -> Stream.of(item).mapToInt(Integer::parseInt).toArray())
                .collect(Collectors.toList());

        // Part 1
        var sum = 0;
        for (var line : lines) {
            var min = Integer.MAX_VALUE;
            var max = Integer.MIN_VALUE;
            for (var n : line) {
                if (n < min) min = n;
                if (n > max) max = n;
            }
            sum += max - min;
        }
        System.out.println(sum);

        // Part 2
        sum = 0;
        for (var line : lines) {
            for (var i = 0; i < line.length - 1; i++) {
                for (var j = i + 1; j < line.length; j++) {
                    if (line[i] % line[j] == 0) {
                        sum += line[i] / line[j];
                    } else if (line[j] % line[i] == 0) {
                        sum += line[j] / line[i];
                    }
                }
            }
        }
        System.out.println(sum);
    }
}
