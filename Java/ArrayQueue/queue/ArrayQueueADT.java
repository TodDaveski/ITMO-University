package queue;

import java.util.Arrays;

public class ArrayQueueADT {
    private int size;
    private Object[] elements = new Object[5];
    private int begin;
    private int end;

    /*
        Model:
        size — size
        a_1, a_2, ... a_size — elements
     */


    // Inv: n >= 0 and for each i = 0 .. n - 1: a[i] != null

    //pre : queue != null && element != null
    //post: size = size' + 1 && for each  0 <= i < size' a[i] = a'[i] && a[size] = element
    public static void enqueue(ArrayQueueADT queue, Object element) {
        ensureCapacity(queue, queue.begin, next(queue, queue.end));
        queue.elements[queue.end] = element;
        queue.end = next(queue, queue.end);
        queue.size++;
    }


    //pre : queue != null && size > 0
    //post : R = a[0]
    public static Object element(ArrayQueueADT queue) {
        return queue.elements[queue.begin];
    }


    private static void ensureCapacity(ArrayQueueADT queue, int newBegin, int newEnd) {
        if (newBegin == newEnd) {
            queue.elements = Arrays.copyOf(ArrayQueueADT.toArray(queue),queue.size * 2);
            queue.begin = 0;
            queue.end = queue.size;
        }
    }

    private static int next(ArrayQueueADT queue, int pos) {
        return pos != (queue.elements.length - 1) ? pos + 1 : 0;
    }


    //pre : queue != null && size > 0
    //post : R = a[0] && size = size' - 1 && for each 0 <= i < size a[i] = a'[i-1]
    public static Object dequeue(ArrayQueueADT queue) {
        queue.size--;
        Object result = queue.elements[queue.begin];
        queue.elements[queue.begin] = null;
        queue.begin = next(queue, queue.begin);
        return result;
    }


    //pre : queue != null
    //post : size = 0
    public static void clear(ArrayQueueADT queue) {
        queue.begin = queue.end;
        queue.size = 0;
        queue.elements = new Object[5];
    }

    //pre : queue != null
    //post : R : size
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // pre : queue != null
    // post : R : size == 0
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // pre: queue != null && size > 0
    // post: R = a[1],...,a[n] && n' = n && for each i=1..n : a'[i] = a[i]
    public static Object[] toArray(ArrayQueueADT queue) {
        Object array[] = new Object[ArrayQueueADT.size(queue)];
        for (int i = 0; i < array.length; i++) {
            array[i] = queue.elements[(queue.begin + i) % queue.elements.length];
        }
        return array;
    }

}
