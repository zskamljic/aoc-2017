import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day11 {
    public static void main(String[] args) throws IOException {
        var input = Files.readString(Paths.get("input11.txt")).trim();
        var directions = input.split(",");

        var x = 0;
        var y = 0;
        var maxDistance = 0;

        for (var direction : directions) {
            switch (direction) {
                case "n":
                    y++;
                    break;
                case "ne":
                    x++;
                    break;
                case "se":
                    x++;
                    y--;
                    break;
                case "s":
                    y--;
                    break;
                case "sw":
                    x--;
                    break;
                case "nw":
                    x--;
                    y++;
                    break;
            }
            var distance = distance(x, y);
            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }

        System.out.println(distance(x,y));
        System.out.println(maxDistance);
    }

    private static int distance(int x, int y) {
        return (Math.abs(x) + Math.abs(y) + Math.abs(x + y)) / 2;
    }
}
