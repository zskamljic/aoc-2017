import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day19 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("input19.txt"));

        var packet = new Point(lines.get(0).indexOf('|'), 0);
        var field = new Field(lines);

        var direction = Direction.DOWN;

        var seen = new StringBuilder();
        var steps = 0;
        while (true) {
            var movement = packet.add(direction.delta);
            steps++;

            if (field.isTurn(packet) || field.isLetter(packet)) {
                if (field.isLetter(packet)) {
                    seen.append(field.get(packet));
                }

                var done = true;
                for (var nextDirection : Direction.values()) {
                    if (nextDirection == direction.inverted()) continue;

                    var candidate = packet.add(nextDirection.delta);
                    if (field.isPath(candidate)) {
                        direction = nextDirection;
                        movement = candidate;
                        done = false;
                        break;
                    }
                }

                if (done) break;
            }

            packet = movement;
            if (field.isOutside(packet)) break;
        }

        // PART 01
        System.out.println(seen.toString());
        // PART 02
        System.out.println(steps);
    }
}

record Point(int x, int y) {
    Point add(Point other) {
        return new Point(x + other.x, y + other.y);
    }
}

enum Direction {
    DOWN(new Point(0, 1)),
    UP(new Point(0, -1)),
    LEFT(new Point(-1, 0)),
    RIGHT(new Point(1, 0));

    final Point delta;

    Direction(Point delta) {
        this.delta = delta;
    }

    Direction inverted() {
        return switch (this) {
            case DOWN -> UP;
            case UP -> DOWN;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}

class Field {
    private final char[][] data;

    Field(List<String> lines) {
        data = lines.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    boolean isLetter(Point point) {
        return Character.isAlphabetic(get(point));
    }

    boolean isTurn(Point point) {
        return '+' == get(point);
    }

    char get(Point point) {
        if (isOutside(point)) return ' ';
        return data[point.y()][point.x()];
    }

    public boolean isPath(Point point) {
        return ' ' != get(point);
    }

    public boolean isOutside(Point point) {
        return point.x() < 0 || point.y() < 0 || point.x() >= data[0].length || point.y() >= data.length;
    }
}
