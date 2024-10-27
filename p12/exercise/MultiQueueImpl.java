package p12.exercise;

import java.util.*;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q> {

    private Map<Q, Queue<T>> queues;

    public MultiQueueImpl() {
        this.queues = new HashMap<>();
    }

    @Override
    public Set<Q> availableQueues() {
        return this.queues.keySet();
    }

    @Override
    public void openNewQueue(Q queue) {
        if (queues.keySet().contains(queue)) throw new IllegalArgumentException("Queue already available");
        Queue<T> newQueue = new PriorityQueue<>();
        queues.put(queue, newQueue);
    }

    @Override
    public boolean isQueueEmpty(Q queue) {
        if (!queues.keySet().contains(queue)) throw new IllegalArgumentException("Queue not available");
        return queues.get(queue).poll() == null;
    }

    @Override
    public void enqueue(T elem, Q queue) {
        if (!queues.keySet().contains(queue)) throw new IllegalArgumentException("Queue not available");
        queues.get(queue).add(elem);
    }

    @Override
    public T dequeue(Q queue) {
        if (!queues.keySet().contains(queue)) throw new IllegalArgumentException("Queue not available");
        return queues.get(queue).poll();
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        Map<Q, T> dequeuedElements = new HashMap<>();
        for (Q queueKey : queues.keySet()) {
            dequeuedElements.put(queueKey, queues.get(queueKey).poll());
        }
        return dequeuedElements;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        Set<T> enqueuedElements = new HashSet<>();
        for (Q queueKey : queues.keySet()) {
            for (T t : queues.get(queueKey)) {
                enqueuedElements.add(t);
            }
        }
        return enqueuedElements;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        if (!queues.keySet().contains(queue)) throw new IllegalArgumentException("Queue not available");

        List <T> dequeuedElements = new ArrayList<>();
        final Queue<T> selectedQueue = queues.get(queue);

        T elem;
        while ((elem = selectedQueue.poll()) != null) {
            dequeuedElements.add(elem);
        }

        return dequeuedElements;
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        if (!queues.keySet().contains(queue)) throw new IllegalArgumentException("Queue not available");
        if (queues.size() < 2) throw new IllegalStateException("No alternative available");
        
        final Iterator<Q> myIterator = queues.keySet().iterator();
        Q transferQueueKey = myIterator.next();
        if (transferQueueKey.equals(queue)) {
            transferQueueKey = myIterator.next();
        }

        Queue<T> transferQueue = queues.get(transferQueueKey);
        final Queue<T> selectedQueue = queues.get(queue);
        T elem;
        while ((elem = selectedQueue.poll()) != null) {
            transferQueue.add(elem);
        }

    }

}
