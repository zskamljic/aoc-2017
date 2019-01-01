import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day04 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("input04.txt"));

        // Part 1
        solve(lines, s -> List.of(s.split("\\s")));

        // Part 2
        solve(lines, s -> Stream.of(s.split("\\s"))
                .map(Word::new)
                .collect(Collectors.toList()));

    }

    private static <T> void solve(List<String> lines, Function<String, Collection<T>> mapper) {
        var valid = 0;

        line:
        for (var line : lines) {
            var words = mapper.apply(line);

            var present = new HashSet<T>();
            for (var word : words) {
                if (present.contains(word)) continue line;
                present.add(word);
            }

            valid++;
        }

        System.out.println(valid);
    }

    static class Word {
        private final char[] array;

        Word(String s) {
            var array = s.toCharArray();
            Arrays.sort(array);
            this.array = array;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Word && Arrays.equals(array, ((Word) obj).array);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(array);
        }
    }
}
