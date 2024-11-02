package org.example;

public class Worker extends Thread {

    private final Buffer buffer;
    private final WorkerCounter workerCounter;

    public Worker(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Acá iría un deserialize() si hubiera que ponerlo (o un cast a Runnable).
                workerCounter.incrementar();
                Runnable task = buffer.read();
                task.run();
            }
        } catch (PoisonException e) {
            Thread.currentThread().interrupt();
        }
        workerCounter.decrementar();
    }
}
