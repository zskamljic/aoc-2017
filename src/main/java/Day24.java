import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Day24 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("input24.txt"));
        var components = lines.stream()
            .map(Component::parse)
            .toList();

        var root = new Node(0, new Component(0, 0), new ArrayList<>(), components);
        var queue = new ArrayDeque<Node>();
        queue.push(root);
        while (!queue.isEmpty()) {
            var node = queue.poll();
            var connectables = node.remaining.stream()
                .filter(c -> c.hasPort(node.out))
                .toList();
            for (var connectable : connectables) {
                var remaining = new ArrayList<>(node.remaining);
                remaining.remove(connectable);
                var out = node.out == connectable.a ? connectable.b : connectable.a;
                var newNode = new Node(out, connectable, new ArrayList<>(), remaining);
                node.children.add(newNode);
                queue.push(newNode);
            }
        }

        // Part 1
        System.out.println(root.maximumSum());
        part02(root);
    }

    private static void part02(Node root) {
        var allPaths = root.allPaths();
        var maxLength = allPaths.stream()
            .mapToInt(List::size)
            .max()
            .orElse(0);

        allPaths.stream()
            .filter(list -> list.size() == maxLength)
            .mapToInt(path -> path.stream()
                .map(Node::current)
                .mapToInt(c -> c.a + c.b)
                .sum())
            .max()
            .ifPresent(System.out::println);
    }

    record Node(int out, Component current, List<Node> children, List<Component> remaining) {
        public int maximumSum() {
            return current.a + current.b + children.stream().mapToInt(Node::maximumSum).max().orElse(0);
        }

        public List<List<Node>> allPaths() {
            if (children.isEmpty()) {
                var path = new ArrayList<Node>();
                path.add(this);
                return List.of(path);
            }

            return children.stream()
                .map(Node::allPaths)
                .flatMap(List::stream)
                .peek(path -> path.add(this))
                .toList();
        }
    }

    record Component(int a, int b) {
        static Component parse(String line) {
            var parts = line.split("/");
            return new Component(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }

        boolean hasPort(int value) {
            return a == value || b == value;
        }
    }
}
