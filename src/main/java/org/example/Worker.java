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
                // Acá iría un deserialize() si hubiera que ponerlo (o un cast a Runnable).
                Runnable task = buffer.read();
                task.run();
            }
        } catch (PoisonException e) {
            System.out.println("termine, soy el worker no. " + (this.threadId() - 20));
            workerCounter.decrementar();
        }
    }
}
