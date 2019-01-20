import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day12 {
    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("input12.txt"));

        var nodes = parseNodes(lines);

        part1(nodes);
        part2(new ArrayList<>(nodes.values()));
    }

    private static void part1(Map<String, Node> nodes) {
        var connected = findConnected(nodes.get("0"));

        System.out.println(connected.size());
    }

    private static void part2(List<Node> nodes) {
        var groups = 0;

        while (nodes.size() > 0) {
            var root = nodes.get(0);
            var connected = findConnected(root);
            nodes.removeAll(connected);
            groups++;
        }
        System.out.println(groups);
    }

    private static List<Node> findConnected(Node root) {
        var queue = new LinkedList<Node>();
        var connected = new ArrayList<Node>();

        queue.add(root);
        while (queue.size() > 0) {
            var node = queue.poll();

            connected.add(node);
            queue.addAll(node.connected);
            queue.removeAll(connected);
        }
        return connected;
    }

    private static Map<String, Node> parseNodes(List<String> lines) {
        var map = new HashMap<String, Node>();

        for (var line : lines) {
            var scanner = new Scanner(line);

            var id = scanner.next();
            Node origin;
            if (map.containsKey(id)) {
                origin = map.get(id);
            } else {
                origin = new Node(id);
                map.put(id, origin);
            }

            scanner.next(); // <->
            var nodes = new ArrayList<Node>();
            while (scanner.hasNext()) {
                id = scanner.next().replaceAll(",", "");
                if (map.containsKey(id)) {
                    nodes.add(map.get(id));
                } else {
                    var node = new Node(id);
                    map.put(id, node);
                    nodes.add(node);
                }
            }

            origin.connect(nodes);
        }
        return map;
    }

    private static class Node {
        String name;
        Set<Node> connected = new HashSet<>();

        Node(String name) {
            this.name = name;
        }

        void connect(List<Node> nodes) {
            nodes.stream()
                    .filter(node -> !node.name.equals(name))
                    .forEach(node -> {
                        connected.add(node);
                        node.connected.add(this);
                    });
        }
    }
}
