import java.math.BigInteger;
import java.util.stream.IntStream;

public class Day14 {
    public static void main(String[] args) {
        var input = "hfdlxzhv";
        var hashes = IntStream.range(0, 128)
                .mapToObj(i -> input + "-" + i)
                .map(Day10::part2)
                .map(Day14::toBinary)
                .toArray(boolean[][]::new);

        part1(hashes);
        part2(hashes);
    }

    private static void part1(boolean[][] hashes) {
        int used = 0;
        for (boolean[] hash : hashes) {
            for (boolean b : hash) {
                if (b) used++;
            }
        }
        System.out.println(used);
    }

    private static void part2(boolean[][] hashes) {
        var groups = 0;

        for (var j = 0; j < hashes.length; j++) {
            for (var i = 0; i < hashes[j].length; i++) {
                if (hashes[j][i]) {
                    clearGroup(hashes, i, j);
                    groups++;
                }
            }
        }
        System.out.println(groups);
    }

    private static void clearGroup(boolean[][] hashes, int x, int y) {
        int[] xOff = {-1, 1, 0, 0};
        int[] yOff = {0, 0, -1, 1};

        hashes[y][x] = false;
        for (var i = 0; i < xOff.length; i++) {
            var newX = x + xOff[i];
            var newY = y + yOff[i];
            if (newX < 0 || newX >= hashes.length) continue;
            if (newY < 0 || newY >= hashes.length) continue;
            if (!hashes[newY][newX]) continue;

            clearGroup(hashes, x + xOff[i], y + yOff[i]);
        }
    }

    private static boolean[] toBinary(String string) {
        var integer = new BigInteger(string, 16);
        var output = new boolean[128];
        for (var i = 0; i < output.length; i++) {
            output[i] = integer.testBit(i);
        }
        return output;
    }
}
