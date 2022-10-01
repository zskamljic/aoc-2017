import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Day25 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Path.of("input25.txt"));

        var program = new Day25.Program(lines);
        program.execute();
    }

    static class Program {
        private final Map<String, State> states = new HashMap<>();
        private final String startState;
        private final int steps;

        public Program(List<String> lines) {
            lines = new ArrayList<>(lines);
            startState = lines.remove(0)
                .replace("Begin in state ", "")
                .replace(".", "");
            var stepsString = lines.remove(0)
                .replace("Perform a diagnostic checksum after ", "")
                .replace(" steps.", "");
            steps = Integer.parseInt(stepsString);
            lines.remove(0);

            while (!lines.isEmpty()) {
                var state = parseStep(lines);
                states.put(state.name(), state);
                if (!lines.isEmpty()) {
                    lines.remove(0);
                }
            }
        }

        private State parseStep(List<String> lines) {
            var name = lines.remove(0)
                .replace("In state ", "")
                .replace(":", "");
            var negative = parseInstructions(lines);
            var positive = parseInstructions(lines);
            return new State(name, negative, positive);
        }

        private List<Instruction> parseInstructions(List<String> lines) {
            var instructions = new ArrayList<Instruction>();

            lines.remove(0); // If the current value is (0|1):
            while (!lines.isEmpty() && !lines.get(0).isEmpty() && !lines.get(0).contains("If the current")) {
                var line = lines.remove(0);
                if (line.contains("Write")) {
                    instructions.add(new Instruction.Write(line.contains("1")));
                } else if (line.contains("Move")) {
                    instructions.add(new Instruction.Move(line.contains("right")));
                } else if (line.contains("Continue with state")) {
                    instructions.add(
                        new Instruction.ChangeState(
                            line.replace("- Continue with state ", "")
                                .replace(".", "")
                                .trim()
                        )
                    );
                }
            }
            return instructions;
        }

        public void execute() {
            var tape = new HashSet<Integer>();
            var position = 0;

            var currentState = startState;
            for (int i = 0; i < steps; i++) {
                var state = states.get(currentState);
                List<Instruction> instructions;
                if (tape.contains(position)) {
                    instructions = state.positive;
                } else {
                    instructions = state.negative;
                }
                for (var instruction : instructions) {
                    switch (instruction) {
                        case Instruction.Write(var enabled) -> {
                            if (enabled) {
                                tape.add(position);
                            } else {
                                tape.remove(position);
                            }
                        }
                        case Instruction.Move(var positive) -> position += positive ? 1 : -1;
                        case Instruction.ChangeState(var next) -> currentState = next;
                        default -> throw new IllegalArgumentException();
                    }
                }
            }
            System.out.println(tape.size());
        }
    }

    record State(String name, List<Instruction> negative, List<Instruction> positive) {
    }

    interface Instruction {
        record Write(boolean state) implements Instruction {
        }

        record Move(boolean right) implements Instruction {
        }

        record ChangeState(String state) implements Instruction {
        }
    }
}
