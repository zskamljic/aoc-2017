import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day20 {
    public static void main(String[] args) throws IOException {
        var particles = Files.readAllLines(Paths.get("input20.txt"))
                .stream()
                .map(Particle::parse)
                .collect(Collectors.toList());

        part01(particles);
        part02(particles);
    }

    private static void part01(List<Particle> particles) {
        for (var i = 0; i < 400; i++) {
            particles = step(particles);
        }
        var closest = particles.stream()
                .min(Comparator.comparing(Particle::distance))
                .map(particles::indexOf)
                .orElse(0);
        System.out.println("Part 1 index: " + closest);
    }

    private static void part02(List<Particle> particles) {
        for (var i = 0; i < 700; i++) {
            particles = step(particles);
            particles = filterCollisions(particles);
        }

        System.out.println(particles.size());
    }

    private static List<Particle> filterCollisions(List<Particle> particles) {
        return particles.stream()
                .collect(Collectors.groupingBy(Particle::position))
                .values()
                .parallelStream()
                .filter(list -> list.size() == 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    static List<Particle> step(List<Particle> original) {
        return original.stream()
                .map(Particle::step)
                .collect(Collectors.toList());
    }

    record Vec3(int x, int y, int z) {
        public static Vec3 read(Scanner scanner) {
            var x = scanner.nextInt();
            var y = scanner.nextInt();
            var z = scanner.nextInt();

            return new Vec3(x, y, z);
        }

        public Vec3 plus(Vec3 other) {
            return new Vec3(x + other.x, y + other.y, z + other.z);
        }
    }

    record Particle(Vec3 position, Vec3 velocity, Vec3 acceleration) {
        public static Particle parse(String line) {
            var scanner = new Scanner(line.replaceAll("[^-0-9,]", "").replaceAll(",", " "));

            var position = Vec3.read(scanner);
            var velocity = Vec3.read(scanner);
            var acceleration = Vec3.read(scanner);
            return new Particle(position, velocity, acceleration);
        }

        int distance() {
            return Math.abs(position.x) + Math.abs(position.y) + Math.abs(position.z);
        }

        Particle step() {
            var newVelocity = velocity.plus(acceleration);
            var newPosition = position.plus(newVelocity);

            return new Particle(newPosition, newVelocity, acceleration);
        }
    }
}
