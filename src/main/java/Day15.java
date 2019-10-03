import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day15 {
    private static final long FACTOR_A = 16807;
    private static final long FACTOR_B = 48271;
    private static final long MASK = 0xFFFF;
    private static final long PICKY_A = 0b11;
    private static final long PICKY_B = 0b111;

    public static void main(String[] args) throws IOException {
        var startValue = loadStartValue();
        part1(startValue);
        part2(startValue);
    }

    private static void part1(long[] startValue) {
        var valueA = startValue[0];
        var valueB = startValue[1];

        var score = 0L;

        for (int i = 0; i < 40_000_000; i++) {
            valueA = getNextValue(valueA, FACTOR_A);
            valueB = getNextValue(valueB, FACTOR_B);

            if ((valueA & MASK) == (valueB & MASK)) {
                score++;
            }
        }

        System.out.println(score);
    }

    private static void part2(long[] startValue) {
        var valueA = startValue[0];
        var valueB = startValue[1];

        var score = 0L;

        for (int i = 0; i < 5_000_000; i++) {
            valueA = getNextValuePicky(valueA, FACTOR_A, PICKY_A);
            valueB = getNextValuePicky(valueB, FACTOR_B, PICKY_B);

            if ((valueA & MASK) == (valueB & MASK)) {
                score++;
            }
        }

        System.out.println(score);
    }

    private static long getNextValuePicky(long value, long factor, long picky) {
        do {
            value = getNextValue(value, factor);
        } while ((value & picky) > 0);

        return value;
    }

    private static long getNextValue(long value, long factor) {
        return value * factor % 2_147_483_647;
    }

    private static long[] loadStartValue() throws IOException {
        var lines = Files.readAllLines(Paths.get("input15.txt"));
        return lines.stream()
                .map(line -> line.split(" "))
                .map(parts -> parts[parts.length - 1])
                .mapToLong(Long::parseLong)
                .toArray();
    }
}
