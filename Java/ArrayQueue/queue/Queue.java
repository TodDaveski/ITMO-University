package queue;

import java.util.List;

public interface Queue {

    /*
        Model:
        size — size
        a_1, a_2, ... a_size — elements
     */

    // Inv: n >= 0 and for each i = 0 .. n - 1: a[i] != null


    //pre : element != null
    //post : size = size' + 1 && for  1 <= i <= size' : a[i] = a'[i] && a[size] = element
    void enqueue(Object element);

    //pre : size > 0
    //post : R : a[1]
    Object element();

    //pre : size > 0
    //post : R : a[1] && for 1 < i <= size : a[i] = a'[i + 1]
    Object dequeue();

    //pre : true
    //post : Immutable
    // R : size
    int size();

    //pre : true
    //post : Immutable
    //R : (size > 0)
    boolean isEmpty();


    //pre : true
    //post : size = 0
    void clear();

    //pre : true
    //post : Immutable
    // R : Array(a)
    Object[] toArray();
}
