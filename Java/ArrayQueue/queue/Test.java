package queue;

import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Object arrayInt[] = new Object[15];
        Random rd = new Random();
        ArrayQueue queue = new ArrayQueue();
        for (int i = 0; i < 15; i++) {
            arrayInt[i] = rd.nextInt();
            queue.enqueue(arrayInt[i]);
        }
        for (int i = 0; i < 15; i++) {
            if (arrayInt[i] != queue.dequeue()) {
                System.out.println("Wrong");
                return;
            }
        }
        System.out.println("Wright");
    }
}
