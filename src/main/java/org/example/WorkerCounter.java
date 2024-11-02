/*
3. Una clase WorkerCounter (implementada como un monitor utilizando m etodos
synchronized) que evita que Main termine su ejecucion mientras queden threads
trabajando.
*/

public class WorkerCounter {
    private int workersActivos = 0;  // Contador de Workers activos

    public synchronized void incrementar() {
        workersActivos++;
    }

    public synchronized void decrementar() {
        workersActivos--;
        if (count == 0) {
            notifyAll();  // Despierta al main si todos los Workers terminaron.
        }
    }

    public synchronized void trabajoTerminado() {
        while (count > 0) {
            wait();  // Bloquea hasta que no haya Workers activos.
        }
    }
}
