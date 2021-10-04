import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day22 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("input22.txt"));

        part01(lines);
        part02(lines);
    }

    private static void part01(List<String> lines) {
        var grid = new GridPart1(lines);
        for (int i = 0; i < 10_000; i++) {
            grid.step();
        }
        grid.print();
    }

    private static void part02(List<String> lines) {
        var grid = new GridPart2(lines);
        for (int i = 0; i < 10_000_000; i++) {
            grid.step();
        }
        grid.print();
    }

    static abstract class CommonGrid<T> {
        protected Coordinate position;
        protected Direction direction = Direction.UP;
        protected int infections = 0;

        protected void parse(List<String> lines) {
            for (int y = 0; y < lines.size(); y++) {
                var line = lines.get(y);
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) == '#') {
                        addInfected(new Coordinate(x, y));
                    }
                }
            }
            position = new Coordinate(
                lines.get(0).length() / 2,
                lines.size() / 2
            );
        }

        public void step() {
            var state = getState();
            step1(state);
            step2(state);
            step3();
        }

        private void step3() {
            switch (direction) {
                case UP -> position = new Coordinate(position.x, position.y - 1);
                case DOWN -> position = new Coordinate(position.x, position.y + 1);
                case LEFT -> position = new Coordinate(position.x - 1, position.y);
                case RIGHT -> position = new Coordinate(position.x + 1, position.y);
            }
        }

        void print() {
            print(false);
        }

        void print(boolean printGrid) {
            System.out.println("Infections: " + infections);
            if (printGrid) {
                System.out.println("Carrier: " + position + ", direction: " + direction);
                var nodes = getCoordinates();
                var minX = nodes.stream().mapToInt(Coordinate::x).min().orElseThrow();
                var maxX = nodes.stream().mapToInt(Coordinate::x).max().orElseThrow();
                var minY = nodes.stream().mapToInt(Coordinate::y).min().orElseThrow();
                var maxY = nodes.stream().mapToInt(Coordinate::y).max().orElseThrow();
                for (int y = minY; y <= maxY; y++) {
                    for (int x = minX; x <= maxX; x++) {
                        System.out.print(getCharacterForCoordinate(new Coordinate(x, y)));
                    }
                    System.out.println();
                }
            }
        }

        protected abstract void addInfected(Coordinate coordinate);

        protected abstract T getState();

        protected abstract void step1(T state);

        protected abstract void step2(T state);

        protected abstract List<Coordinate> getCoordinates();

        protected abstract char getCharacterForCoordinate(Coordinate coordinate);
    }

    static class GridPart1 extends CommonGrid<Boolean> {
        private final Set<Coordinate> infected = new HashSet<>();

        private GridPart1(List<String> lines) {
            parse(lines);
        }

        @Override
        protected void addInfected(Coordinate coordinate) {
            infected.add(coordinate);
        }

        @Override
        protected Boolean getState() {
            return infected.contains(position);
        }

        @Override
        protected void step1(Boolean isInfected) {
            if (isInfected) {
                direction = direction.rotateRight();
            } else {
                direction = direction.rotateLeft();
            }
        }

        @Override
        protected void step2(Boolean isInfected) {
            if (isInfected) {
                infected.remove(position);
            } else {
                infected.add(position);
                infections++;
            }
        }

        @Override
        protected List<Coordinate> getCoordinates() {
            return infected.stream()
                .sorted((a, b) -> {
                    if (a.y < b.y) return -1;
                    if (a.y > b.y) return 1;
                    return b.x - a.x;
                }).toList();
        }

        @Override
        protected char getCharacterForCoordinate(Coordinate coordinate) {
            if (infected.contains(coordinate)) {
                return '#';
            } else {
                return '.';
            }
        }
    }

    static class GridPart2 extends CommonGrid<State> {
        private final Map<Coordinate, State> states = new HashMap<>();

        protected GridPart2(List<String> lines) {
            parse(lines);
        }

        @Override
        protected void addInfected(Coordinate coordinate) {
            states.put(coordinate, State.INFECTED);
        }

        @Override
        protected State getState() {
            return states.getOrDefault(position, State.CLEAN);
        }

        @Override
        protected void step1(State state) {
            direction = switch (state) {
                case CLEAN -> direction.rotateLeft();
                case WEAKENED -> direction;
                case INFECTED -> direction.rotateRight();
                case FLAGGED -> direction.reverse();
            };
        }

        @Override
        protected void step2(State state) {
            var nextState = state.next();
            if (nextState == State.INFECTED) {
                infections++;
            }
            states.put(position, nextState);
        }

        @Override
        protected List<Coordinate> getCoordinates() {
            return states.keySet().stream().toList();
        }

        @Override
        protected char getCharacterForCoordinate(Coordinate coordinate) {
            var state = states.getOrDefault(coordinate, State.CLEAN);
            return switch (state) {
                case CLEAN -> '.';
                case WEAKENED -> 'W';
                case INFECTED -> '#';
                case FLAGGED -> 'F';
            };
        }
    }

    enum State {
        CLEAN,
        WEAKENED,
        INFECTED,
        FLAGGED;

        State next() {
            return switch (this) {
                case CLEAN -> WEAKENED;
                case WEAKENED -> INFECTED;
                case INFECTED -> FLAGGED;
                case FLAGGED -> CLEAN;
            };
        }
    }

    enum Direction {
        UP, RIGHT, DOWN, LEFT;

        Direction rotateLeft() {
            return switch (this) {
                case UP -> Direction.LEFT;
                case RIGHT -> Direction.UP;
                case DOWN -> Direction.RIGHT;
                case LEFT -> Direction.DOWN;
            };
        }

        Direction rotateRight() {
            return switch (this) {
                case UP -> Direction.RIGHT;
                case RIGHT -> Direction.DOWN;
                case DOWN -> Direction.LEFT;
                case LEFT -> Direction.UP;
            };
        }

        public Direction reverse() {
            return switch (this) {
                case UP -> Direction.DOWN;
                case DOWN -> Direction.UP;
                case LEFT -> Direction.RIGHT;
                case RIGHT -> Direction.LEFT;
            };
        }
    }

    record Coordinate(int x, int y) {
    }
}
