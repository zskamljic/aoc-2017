import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day09 {
    public static void main(String[] args) throws IOException {
        var input = Files.readString(Paths.get("input09.txt"));

        System.out.println(parse(input));
    }

    private static int parse(String input) {
        var stream = new ByteArrayInputStream(input.getBytes());
        var groups = 0;
        var nested = 0;
        var removed = 0;

        while (stream.available() > 0) {
            var value = stream.read();
            switch (value) {
                case '<':
                    removed += readGarbage(stream);
                    break;
                case '{':
                    nested++;
                    break;
                case '}':
                    nested--;
                    groups += nested + 1;
                    break;
                // ignored
                case ',':
                case '\n':
                    break;
                default:
                    System.out.println("invalid character: " + (char) value);
                    return -1;
            }
        }

        System.out.println("removed: " + removed);
        return groups;
    }

    private static int readGarbage(ByteArrayInputStream stream) {
        var removedCount = 0;

        int value;
        do {
            value = stream.read();

            if (value == '!') {
                //noinspection ResultOfMethodCallIgnored
                stream.read();
            } else {
                removedCount++;
            }
        } while (value != '>');
        return removedCount - 1;
    }
}
