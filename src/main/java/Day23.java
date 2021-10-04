import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day23 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("input23.txt"));

        var instructions = lines.stream()
            .map(Instruction::parse)
            .toList();

        part01(instructions);
        part02();
    }

    private static void part01(List<Instruction> instructions) {
        var program = new Program(instructions);
        program.execute();
        System.out.println(program.muls);
    }

    private static void part02() {
        int b = 79 * 100 + 100_000;
        int c = b + 17000;
        int d;
        int f;
        int g;
        int h = 0;
        do {
            f = 1; // 9
            d = 2; // 10
            while (d < b) {
                if (b % d == 0) {
                    f = 0;
                    break;
                }
                d++; // 21
            }
            if (f == 0) { // 25
                h++; // 26
            }
            g = b - c; // 27
            b += 17; // 31
        } while (g != 0);
        System.out.println(h);
    }

    static class Program {
        private final int[] registers = new int[8];
        private final List<Instruction> instructions;
        private int pc = 0;
        private int muls = 0;

        public Program(List<Instruction> instructions) {
            this.instructions = instructions;
        }

        public void execute() {
            while (pc < instructions.size()) {
                var instruction = instructions.get(pc);
                switch (instruction) {
                    case Instruction.Set s -> registers[s.register] = s.value;
                    case Instruction.SetRegister s -> registers[s.register] = registers[s.value];
                    case Instruction.Sub s -> registers[s.register] -= s.value;
                    case Instruction.SubRegister s -> registers[s.register] -= registers[s.value];
                    case Instruction.Mul s -> {
                        muls++;
                        registers[s.register] += s.value;
                    }
                    case Instruction.MulRegister s -> {
                        muls++;
                        registers[s.register] += registers[s.value];
                    }
                    case Instruction.JumpNotZero jmp -> {
                        var comparedValue = jmp.register < 0 ? jmp.register + 'a' : registers[jmp.register];

                        if (comparedValue != 0) {
                            pc += jmp.offset;
                            continue;
                        }
                    }
                }
                pc++;
            }
        }
    }

    sealed interface Instruction {
        static Instruction parse(String line) {
            var parts = line.split(" ");
            var register = parts[1].charAt(0) - 'a';
            if (parts[2].matches("[a-h]")) {
                return parseRegister(parts[0], register, parts[2]);
            } else {
                return parseLiteral(parts[0], register, parts[2]);
            }
        }

        static Instruction parseRegister(String instruction, int register, String rawValue) {
            var value = rawValue.charAt(0) - 'a';
            return switch (instruction) {
                case "set" -> new SetRegister(register, value);
                case "sub" -> new SubRegister(register, value);
                case "mul" -> new MulRegister(register, value);
                case "jnz" -> new JumpNotZero(register, value);
                default -> throw new RuntimeException(instruction);
            };
        }

        static Instruction parseLiteral(String instruction, int register, String rawValue) {
            var value = Integer.parseInt(rawValue);
            return switch (instruction) {
                case "set" -> new Set(register, value);
                case "sub" -> new Sub(register, value);
                case "mul" -> new Mul(register, value);
                case "jnz" -> new JumpNotZero(register, value);
                default -> throw new RuntimeException(instruction);
            };
        }

        record Set(int register, int value) implements Instruction {
        }

        record SetRegister(int register, int value) implements Instruction {
        }

        record Sub(int register, int value) implements Instruction {
        }

        record SubRegister(int register, int value) implements Instruction {
        }

        record Mul(int register, int value) implements Instruction {
        }

        record MulRegister(int register, int value) implements Instruction {
        }

        record JumpNotZero(int register, int offset) implements Instruction {
        }
    }
}
