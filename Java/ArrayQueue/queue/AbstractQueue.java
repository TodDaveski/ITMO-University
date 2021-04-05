package queue;


public abstract class AbstractQueue implements Queue {
    protected int size;

    @Override
    public Object dequeue() {
        size--;
        return dequeueImpl();

    }

    abstract protected Object dequeueImpl();

    @Override
    public void enqueue(Object element) {
        size++;
        enqueueImpl(element);
    }

    abstract protected void enqueueImpl(Object element);

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
        clearImpl();
    }

    abstract protected void clearImpl();

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            Object element = dequeue();
            result[i] = element;
            enqueue(element);
        }
        return result;
    }
}
