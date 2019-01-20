import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day10 {
    public static void main(String[] args) throws IOException {
        var inputStr = Files.readString(Paths.get("input10.txt")).trim();

        part1(inputStr);
        System.out.println(part2(inputStr));
    }

    private static void part1(String inputStr) {
        var list = createList();
        var inputs = Stream.of(inputStr.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        var position = 0;
        var skipSize = 0;

        for (int input : inputs) {
            rotate(list, position, input);
            position += input + skipSize;
            skipSize++;
        }

        System.out.println(list[0] * list[1]);
    }

    static String part2(String inputStr) {
        // Append static suffix
        inputStr += (char) 17;
        inputStr += (char) 31;
        inputStr += (char) 73;
        inputStr += (char) 47;
        inputStr += (char) 23;

        var list = createList();
        var inputs = inputStr.chars().toArray();

        var position = 0;
        var skipSize = 0;

        // 64 rounds of same algorithm
        for (var i = 0; i < 64; i++) {
            for (int input : inputs) {
                rotate(list, position, input);
                position += input + skipSize;
                skipSize++;
            }
        }

        // Reduce to dense hash
        var denseHash = new int[16];
        for (var i = 0; i < denseHash.length; i++) {
            for (var j = 0; j < 16; j++) {
                denseHash[i] ^= list[i * 16 + j];
            }
        }

        // Create hex string
        var builder = new StringBuilder();
        for (var datum : denseHash) {
            builder.append(String.format("%02x", datum));
        }
        return builder.toString();
    }

    private static int[] createList() {
        var list = new int[256];
        for (var i = 0; i < list.length; i++) {
            list[i] = i;
        }
        return list;
    }

    private static void rotate(int[] list, int position, int length) {
        for (var i = 0; i < length / 2; i++) {
            var first = (position + i) % list.length;
            var second = (position + length - i - 1) % list.length;

            var temp = list[first];
            list[first] = list[second];
            list[second] = temp;
        }
    }
}
