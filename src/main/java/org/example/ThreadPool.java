package org.example;

public class ThreadPool {

    private final Buffer buffer;
    private final Worker[] workers;

    public ThreadPool(int bufferSize, int workersQuantity) {
        buffer = new Buffer(bufferSize);
        workers = new Worker[workersQuantity];

        for (int i = 0; i < workersQuantity; i++) {
            workers[i] = new Worker(buffer);
            workers[i].start();
        }
    }

    public void launch(Runnable task) {
        // Acá iría un serialize() si hubiera que ponerlo
        buffer.write(task);
    }

    public void stop() {
        for (int i = 0; i < workers.length; i++) {
            launch(new PoisonPill());
        }
        for (Worker worker : workers) {
            // join() espera que el thread worker termine
            try {
                worker.join();
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
