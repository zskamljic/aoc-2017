import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Day16 {
    public static void main(String[] args) throws IOException {
        var input = "abcdefghijklmnop";
        var dance = Files.readString(Paths.get("input16.txt")).trim();

        var commands = dance.split(",");
        System.out.println("Step 1:");
        System.out.println(step1(input, commands));
        step2(input, commands);
    }

    private static void step2(String input, String[] commands) {
        var seen = new ArrayList<String>();

        for (int i = 0; i < 1_000_000_000; i++) {
            input = step1(input, commands);
            if (!seen.contains(input)) {
                seen.add(input);
            } else {
                break;
            }
        }
        System.out.println(seen.get(1_000_000_000 % seen.size() - 1));
    }

    private static String step1(String input, String[] commands) {
        for (String command : commands) {
            input = applyDance(input, command);
        }
        return input;
    }

    private static String applyDance(String line, String command) {
        if (command.startsWith("s")) {
            return spin(line, Integer.parseInt(command.substring(1)));
        } else if (command.startsWith("x")) {
            return swap(line, command.substring(1));
        } else if (command.startsWith("p")) {
            return swapChars(line, command.substring(1));
        }
        return line;
    }

    private static String spin(String line, int count) {
        return line.substring(line.length() - count) + line.substring(0, line.length() - count);
    }

    private static String swap(String line, String swaps) {
        var positions = swaps.split("/");
        var first = Integer.parseInt(positions[0]);
        var second = Integer.parseInt(positions[1]);
        var chars = line.toCharArray();

        var temp = chars[first];
        chars[first] = chars[second];
        chars[second] = temp;
        return new String(chars);
    }

    private static String swapChars(String line, String swaps) {
        var names = swaps.toCharArray();
        var first = names[0];
        var second = names[2];

        var swapped = 0;
        var chars = line.toCharArray();
        for (int i = 0; i < chars.length && swapped < 2; i++) {
            if (chars[i] == first) {
                chars[i] = second;
                swapped++;
            } else if (chars[i] == second) {
                chars[i] = first;
                swapped++;
            }
        }
        return new String(chars);
    }
}
