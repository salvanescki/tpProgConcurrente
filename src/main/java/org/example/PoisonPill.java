package org.example;

public class PoisonPill extends Task {
    @Override
    public void run() {
        throw new PoisonException("Deteniendo Worker");
    }
}
