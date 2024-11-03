package org.example;

/**
 * Clase de ejemplo para probar que funcionan el buffer y los workers
 */
public class DummyTask extends Task {

    private final String msg;

    public DummyTask(String msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        System.out.println(msg);
    }
}
