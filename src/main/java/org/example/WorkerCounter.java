package org.example;

public class WorkerCounter {
    private int workersActivos = 0;  // Contador de Workers activos

    public synchronized void incrementar() {
        workersActivos++;
    }

    public synchronized void decrementar() {
        workersActivos--;
        if (workersActivos == 0) {
            notifyAll();  // Despierta al main si todos los Workers terminaron.
        }
    }

    public synchronized void trabajoTerminado() {
        while (workersActivos > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }  // Bloquea hasta que no haya Workers activos.
        }
    }
}
