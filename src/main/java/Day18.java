import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day18 {
    public static void main(String[] args) throws IOException {
        var instructions = Files.readAllLines(Paths.get("input18.txt"));
        part1(instructions);
        part2(instructions);
    }

    private static void part1(List<String> instructions) {
        var registers = new BigInteger[26];
        Arrays.fill(registers, BigInteger.ZERO);

        BigInteger result = null;

        var sounds = new ArrayList<BigInteger>();

        label:
        for (int i = 0; i < instructions.size(); ) {
            var parts = instructions.get(i).split(" ");

            switch (parts[0]) {
                case "snd":
                    sounds.add(getValue(parts[1], registers));
                    break;
                case "set":
                    registers[toIndex(parts[1])] = getValue(parts[2], registers);
                    break;
                case "add":
                    registers[toIndex(parts[1])] = registers[toIndex(parts[1])].add(getValue(parts[2], registers));
                    break;
                case "mul":
                    registers[toIndex(parts[1])] = registers[toIndex(parts[1])].multiply(getValue(parts[2], registers));
                    break;
                case "mod":
                    registers[toIndex(parts[1])] = registers[toIndex(parts[1])].mod(getValue(parts[2], registers));
                    break;
                case "rcv":
                    if (getValue(parts[1], registers).compareTo(BigInteger.ZERO) > 0) {
                        result = sounds.get(sounds.size() - 1);
                        break label;
                    }

                    break;
                case "jgz":
                    if (getValue(parts[1], registers).compareTo(BigInteger.ZERO) > 0) {
                        i += getValue(parts[2], registers).intValue();
                        continue;
                    }
                    break;
            }

            i++;
        }

        System.out.println(result);
    }

    private static void part2(List<String> instructions) {
        var program0 = new Program(instructions, 0);
        var program1 = new Program(instructions, 1);
        program0.sink = program1;
        program1.sink = program0;

        while (!((program0.isDone() && program1.isDone()) || (program0.isBlocked() && program1.isBlocked()))) {
            while (!program0.isDone() && !program0.isBlocked()) {
                program0.processInstructions();
            }

            while (!program1.isDone() && !program1.isBlocked()) {
                program1.processInstructions();
            }
        }

        System.out.println(program1.sent);
    }

    private static BigInteger getValue(final String location, final BigInteger[] registers) {
        if (Character.isAlphabetic(location.charAt(0))) {
            return registers[location.charAt(0) - 'a'];
        } else {
            return new BigInteger(location);
        }
    }

    private static int toIndex(String part) {
        return part.charAt(0) - 'a';
    }

    private static class Program {
        private final Deque<BigInteger> messages = new ArrayDeque<>();
        private final List<String> instructions;
        private final BigInteger[] registers = new BigInteger[26];

        private Program sink;
        private int index = 0;
        private int sent;

        Program(List<String> instructions, int id) {
            this.instructions = instructions;
            Arrays.fill(registers, BigInteger.ZERO);
            registers['p' - 'a'] = BigInteger.valueOf(id);
        }

        private boolean isBlocked() {
            return messages.isEmpty() && instructions.get(index).startsWith("rcv");
        }

        private boolean isDone() {
            return index < 0 || index >= instructions.size();
        }

        private void processInstructions() {
            var parts = instructions.get(index).split(" ");

            switch (parts[0]) {
                case "snd":
                    sink.messages.add(getValue(parts[1], registers));
                    sent++;
                    break;
                case "set":
                    registers[toIndex(parts[1])] = getValue(parts[2], registers);
                    break;
                case "add":
                    registers[toIndex(parts[1])] = registers[toIndex(parts[1])].add(getValue(parts[2], registers));
                    break;
                case "mul":
                    registers[toIndex(parts[1])] = registers[toIndex(parts[1])].multiply(getValue(parts[2], registers));
                    break;
                case "mod":
                    registers[toIndex(parts[1])] = registers[toIndex(parts[1])].mod(getValue(parts[2], registers));
                    break;
                case "rcv":
                    registers[toIndex(parts[1])] = messages.pop();
                    break;
                case "jgz":
                    if (getValue(parts[1], registers).compareTo(BigInteger.ZERO) > 0) {
                        index += getValue(parts[2], registers).intValue();
                        return;
                    }
                    break;
                default:
                    throw new RuntimeException(instructions.get(index));
            }

            index++;
        }
    }
}
