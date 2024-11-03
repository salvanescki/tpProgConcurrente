package org.example;

public class Buffer {

    private final Runnable[] tasks;
    private int begin = 0;
    private int end = 0;

    public Buffer(int size) {
        tasks = new Runnable[size + 1];
    }

    private int next(int i) {
        return (i + 1) % tasks.length;
    }

    private boolean isEmpty() {
        return begin == end;
    }

    private boolean isFull() {
        return next(begin) == end;
    }

    public synchronized void write(Runnable aTask) {
        while (isFull()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        tasks[begin] = aTask;
        begin = next(begin);
        notifyAll();
    }

    public synchronized Runnable read() {
        while (isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        Runnable result = tasks[end];
        end = next(end);
        notifyAll();
        return result;
    }
}
