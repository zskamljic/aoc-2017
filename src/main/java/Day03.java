public class Day03 {

    public static void main(String[] args) {
        var input = 289326;

        part1(input);
        part2(input);
    }

    private static void part1(int input) {
        var layer = 0;

        while (layer * layer < input) layer += 2;

        var pivots = new int[4];
        for (var i = 0; i < pivots.length; i++) {
            pivots[i] = layer * layer - i * (layer - 1);
        }

        for (var pivot : pivots) {
            var distance = Math.abs(pivot - input);
            if (distance <= (layer - 1) / 2) {
                System.out.println(layer - 1 - distance);
                return;
            }
        }
    }

    private static void part2(int goal) {
        // Store coordinates and sum
        int[][] array = new int[1000][3];

        // Neighbours
        int[][] coords = {{1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}};

        // Start coordinates
        var x = 0;
        var y = 0;

        // Movement
        int deltaX = 0;
        int deltaY = -1;

        for (var step = 0; step < array.length; step++) {
            if (step == 0) {
                // Start at 1
                array[step][0] = 0;
                array[step][1] = 0;
                array[step][2] = 1;
            } else {
                // Calculate value
                var sum = 0;
                for (var i = 0; i < 1000; i++) {
                    var tx = array[i][0];
                    var ty = array[i][1];
                    for (var j = 0; j < 8; j++) {
                        if ((x + coords[j][0] == tx) && (y + coords[j][1] == ty))
                            sum += array[i][2];
                    }
                }

                // Goal reached
                if (sum > goal) {
                    System.out.println(sum);
                    return;
                }

                // Store value
                array[step][0] = x;
                array[step][1] = y;
                array[step][2] = sum;
            }

            step += 1;

            // If in corners
            if ((x == y) || ((x < 0) && (x == -y)) || ((x > 0) && (x == 1 - y))) {
                // Turn
                var tmp = deltaX;
                deltaX = -deltaY;
                deltaY = tmp;
            }

            // Move
            x += deltaX;
            y += deltaY;
        }
    }
}
