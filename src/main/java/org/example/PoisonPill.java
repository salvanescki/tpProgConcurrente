package org.example;

public class PoisonPill implements Runnable {
    @Override
    public void run() {
        throw new PoisonException("Deteniendo Worker");
    }

    class PoisonException extends Runtime Exception{}
}
