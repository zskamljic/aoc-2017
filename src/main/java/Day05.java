import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Day05 {
    public static void main(String[] args) throws IOException {
        var input = Files.readAllLines(Paths.get("input05.txt")).stream()
                .mapToInt(Integer::parseInt)
                .toArray();

        part1(input);
        part2(input);
    }

    private static void part1(int[] input) {
        input = Arrays.copyOf(input, input.length);
        var jumps = 0;
        var index = 0;
        while (index < input.length) {
            input[index]++;
            index += input[index] - 1;
            jumps++;
        }
        System.out.println(jumps);
    }

    private static void part2(int[] input) {
        input = Arrays.copyOf(input, input.length);
        var jumps = 0;
        var index = 0;
        while (index < input.length) {
            var prevIndex = index;

            index += input[index];

            var offset = input[prevIndex];
            if (offset >= 3) {
                input[prevIndex]--;
            } else {
                input[prevIndex]++;
            }

            jumps++;
        }
        System.out.println(jumps);
    }
}
