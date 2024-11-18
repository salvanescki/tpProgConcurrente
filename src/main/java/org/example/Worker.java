package org.example;

public class Worker extends Thread {

    private final Buffer buffer;
    private final WorkerCounter workerCounter;

    public Worker(Buffer buffer, WorkerCounter workerCounter) {
        this.buffer = buffer;
        this.workerCounter = workerCounter;
    }

    @Override
    public void run() {
        try {
            workerCounter.incrementar();
            while (true) {
                Runnable task = buffer.read();
                task.run();
            }
        } catch (PoisonException e) {
            workerCounter.decrementar();
        }
    }
}
