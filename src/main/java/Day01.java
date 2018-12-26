import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day01 {
    public static void main(String[] args) throws IOException {
        var input = Files.readString(Paths.get("input01.txt")).trim();

        var numbers = Stream.of(input.split(""))
                .mapToInt(Integer::parseInt)
                .toArray();

        // Part 1
        var sum = 0;
        for (var i = 0; i < numbers.length - 1; i++) {
            if (numbers[i] == numbers[i + 1]) sum += numbers[i];
        }
        if (numbers[0] == numbers[numbers.length - 1]) sum += numbers[0];

        System.out.println(sum);

        // Part 2
        sum = 0;
        for (var i = 0; i < numbers.length; i++) {
            if (numbers[i] == numbers[(i + numbers.length / 2) % numbers.length]) sum += numbers[i];
        }

        System.out.println(sum);
    }
}
