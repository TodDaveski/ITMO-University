package queue;

public class LinkedQueue extends AbstractQueue implements Queue {
    private Node head;
    private Node tail;

    public LinkedQueue() {
        head = new Node();
        tail = head;
    }


    @Override
    protected Object dequeueImpl() {
        Object element = element();
        head = head.next;
        return element;
    }

    protected void enqueueImpl(Object element) {
        tail.value = element;
        Node next = new Node();
        tail.next = next;
        tail = next;
    }

    @Override
    public Object element() {
        return head.value;
    }



    @Override
    protected void clearImpl() {
        head = tail;
    }

    private static class Node {
        private Object value;
        private Node next;

        public Node(Object value, Node next) {
            this.value = value;
            this.next = next;
        }

        public Node() {

        }
    }

}
