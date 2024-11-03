package org.example;

public class ThreadPool {

    private Buffer buffer;
    private final Worker[] workers;
    private int numWorkers;
    private final WorkerCounter workerCounter;

    public ThreadPool(int bufferSize, int numWorkers, WorkerCounter workerCounter) {
        this.buffer = new Buffer(bufferSize);
        this.numWorkers = numWorkers;
        this.workers = new Worker[numWorkers];
        this.workerCounter = workerCounter;

        for (int i = 0; i < numWorkers; i++) {
            this.workers[i] = new Worker(buffer, workerCounter);
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
    }
}
