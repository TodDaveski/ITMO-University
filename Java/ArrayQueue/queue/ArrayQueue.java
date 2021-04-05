package queue;

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue implements Queue {
    private Object[] elements = new Object[5];
    private int begin;
    private int end;

    @Override
    protected void enqueueImpl(Object element) {
        ensureCapacity(begin, next(end));
        elements[end] = element;
        end = next(end);
    }

    @Override
    public Object element() {
        return elements[begin];
    }

    @Override
    protected Object dequeueImpl() {
        Object element = element();
        begin = next(begin);
        return element;
    }

    @Override
    protected void clearImpl() {
        begin = end;
        elements = new Object[5];
    }


    private int next(int pos) {
        return pos != (elements.length - 1) ? pos + 1 : 0;
    }


    private void ensureCapacity(int newBegin, int newEnd) {
        if (newBegin == newEnd) {
            elements = Arrays.copyOf(toArray(), size * 2);
            begin = 0;
            end = size;
        }
    }


}
