package queue;

import java.util.Arrays;

public class ArrayQueueModule {
    private static int size;
    private static Object[] elements = new Object[5];
    private static int begin;
    private static int end;

    /*
        Model:
        size — size
        a_1, a_2, ... a_size — elements
     */

    // Inv: n >= 0 and for each i = 0 .. n - 1: a[i] != null

    //pre : element != null
    //post: size = size' + 1 && for each  0 <= i < size': a[i] = a'[i] && a[size] = element
    public static void enqueue(Object element) {
        ensureCapacity(begin, next(end));
        elements[end] = element;
        end = next(end);
        size++;
    }


    private static int next(int pos) {
        return pos != (elements.length - 1) ? pos + 1 : 0;
    }


    //pre : size > 0
    //post : R = a[0]
    public static Object element() {
        return elements[begin];
    }


    private static void ensureCapacity(int newBegin, int newEnd) {
        if (newEnd == newBegin) {
            elements = Arrays.copyOf(toArray(),size * 2);
            begin = 0;
            end = size;
        }
    }

    //pre : size > 0
    //post : R = a[0] && size' = size - 1 && for each 0 <= i < size a'[i] = a[i-1]
    public static Object dequeue() {
        size--;
        Object result = elements[begin];
        elements[begin] = null;
        begin = next(begin);
        return result;
    }


    //pre : true
    //post : size = 0
    public static void clear() {
        begin = end;
        size = 0;
        elements = new Object[5];
    }

    //pre : true
    //post : R : size
    public static int size() {
        return size;
    }

    // pre : true
    // post : R : size == 0
    public static boolean isEmpty() {
        return size == 0;
    }


    // pre: size > 0
    // post: R = a[1],...,a[n] && n' = n && for each i=1..n : a'[i] = a[i]
    public static Object[] toArray() {
        Object array[] = new Object[size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = elements[(begin + i) % elements.length];
        }
        return array;
    }

}

