import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day06 {
    public static void main(String[] args) throws IOException {
        var banks = Stream.of(Files.readString(Paths.get("input06.txt")).split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();

        var seen = new ArrayList<State>();
        seen.add(new State(banks));

        var steps = 0;

        while (true) {
            nextStep(banks);
            steps++;
            var state = new State(banks);
            if (seen.contains(state)) {
                System.out.print("Loop size: ");
                System.out.println(steps - seen.indexOf(state));
                break;
            }

            seen.add(state);
        }

        System.out.println(steps);
    }

    private static void nextStep(int[] input) {
        var index = findIndex(input);
        var value = input[index];
        input[index] = 0;

        index++;
        while (value > 0) {
            if (index >= input.length) index %= input.length;
            input[index]++;
            index++;
            value--;
        }
    }

    private static int findIndex(int[] input) {
        var max = -1;
        var index = 0;
        for (var i = 0; i < input.length; i++) {
            if (input[i] > max) {
                max = input[i];
                index = i;
            }
        }
        return index;
    }

    private static class State {
        private final int[] state;

        State(int[] state) {
            this.state = Arrays.copyOf(state, state.length);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof State && Arrays.equals(state, ((State) o).state);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(state);
        }
    }
}
