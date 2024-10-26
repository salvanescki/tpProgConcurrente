package org.example;

public class Worker extends Thread {

    private final Buffer buffer;

    public Worker(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Acá iría un deserialize() si hubiera que ponerlo (o un cast a Runnable).
                Runnable task = buffer.read();
                task.run();
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
