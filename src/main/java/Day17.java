import java.util.ArrayList;

public class Day17 {
    private static final int INPUT = 369;

    public static void main(String[] args) {
        step1();
        step2();
    }

    private static void step1() {
        var list = new ArrayList<Integer>();
        list.add(0);

        int position = 0;
        for (int i = 1; i < 2018; i++) {
            position = ((position + INPUT) % i) + 1;
            list.add(position, i);
        }

        System.out.println(list.get(position + 1));
    }

    private static void step2() {
        int answer = 0;
        for (int i = 1, position = 0; i < 50_000_000; i++) {
            position = ((position + INPUT) % i) + 1;
            if (position == 1) {
                answer = i;
            }
        }

        System.out.println(answer);
    }
}
