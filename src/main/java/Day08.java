import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day08 {
    public static void main(String[] args) throws IOException {
        var instructions = Files.readAllLines(Paths.get("input08.txt")).stream()
                .map(Instruction::new)
                .collect(Collectors.toList());

        var maxHistory = Integer.MIN_VALUE;

        var registers = new HashMap<String, Integer>();
        for (var instruction : instructions) {
            instruction.apply(registers);

            var currentMax = registers.values().stream().mapToInt(i -> i).max().orElseThrow();
            if (currentMax > maxHistory) {
                maxHistory = currentMax;
            }
        }

        System.out.println(registers.values().stream().mapToInt(i -> i).max().orElseThrow());
        System.out.println(maxHistory);
    }

    private static class Instruction {
        final String comparedRegister;
        final String register;
        final String operation;
        final int value;
        final String operator;
        final int comparedValue;

        Instruction(String line) {
            var scanner = new Scanner(line);

            register = scanner.next();
            operation = scanner.next();
            value = scanner.nextInt();

            scanner.next(); // if
            comparedRegister = scanner.next();
            operator = scanner.next();
            comparedValue = scanner.nextInt();
        }

        void apply(HashMap<String, Integer> registers) {
            var comparedRegisterValue = registers.getOrDefault(comparedRegister, 0);

            switch (operator) {
                case ">":
                    if (comparedRegisterValue <= comparedValue) return;
                    break;
                case "<":
                    if (comparedRegisterValue >= comparedValue) return;
                    break;
                case ">=":
                    if (comparedRegisterValue < comparedValue) return;
                    break;
                case "<=":
                    if (comparedRegisterValue > comparedValue) return;
                    break;
                case "==":
                    if (comparedRegisterValue != comparedValue) return;
                    break;
                case "!=":
                    if (comparedRegisterValue == comparedValue) return;
                    break;
            }

            var registerValue = registers.getOrDefault(register, 0);

            switch (operation) {
                case "inc":
                    registerValue += value;
                    break;
                case "dec":
                    registerValue -= value;
                    break;
            }

            registers.put(register, registerValue);
        }
    }
}
