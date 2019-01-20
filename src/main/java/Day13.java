import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day13 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("input13.txt"));

        part1(lines);
        part2(lines);
    }

    private static void part1(List<String> lines) {
        var state = new State(lines);
        state.completeTrip();
        System.out.println(state.score);
    }

    private static void part2(List<String> lines) {
        var delay = 0;
        var state = new State(lines);
        var match = true;
        do {
            delay++;
            int finalDelay = delay;

            // 2 * (entry.value.depth-1) -> time needed for that layer to be on 0
            // just find any that that are in our spot at given time
            match = state.layers.entrySet().stream()
                    .anyMatch(entry -> (entry.getKey() + finalDelay) % (2 * (entry.getValue().depth - 1)) == 0);
        } while (match);
        System.out.println(delay);
    }

    private static class State {
        int lastLayer;
        Map<Integer, Layer> layers = new HashMap<>();
        int position = -1;
        int score = 0;

        State(List<String> lines) {
            for (var line : lines) {
                var scanner = new Scanner(line);
                var index = Integer.parseInt(scanner.next().replaceAll("[^\\d+]", ""));
                var layer = new Layer(scanner.nextInt());
                layers.put(index, layer);

                if (index > lastLayer) lastLayer = index;
            }
        }

        void next() {
            position++;
            if (caught()) {
                score += position * layers.get(position).depth;
            }
            layers.values().forEach(Layer::move);
        }

        private boolean caught() {
            return layers.containsKey(position) && layers.get(position).position == 0;
        }

        boolean shouldMove() {
            return position < lastLayer;
        }

        void completeTrip() {
            while (shouldMove()) {
                next();
            }
        }
    }

    private static class Layer {
        int depth;
        int position;
        int movement = 1;

        Layer(int depth) {
            this.depth = depth;
        }

        void move() {
            if (position + movement < 0 || position + movement >= depth) {
                movement = -movement;
            }
            position += movement;
        }
    }
}
