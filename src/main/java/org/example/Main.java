package org.example;

public class Main {
    public static void main(String[] args) {
        /*
         * TODO: hacer que en args[] lleguen los parámetros:
         *  - modo : imagen simple o archivo de test (.csv).
         *  - k : cantidad de k vecinos más cercanos, o k imágenes más parecidas, deseados.
         *  - cant_threads : cantidad de threads worker que va a crear el ThreadPool.
         *  - tamano_buffer : el tamaño del buffer.
         *
         * */


        int kNeighbors = 0; //args[1];
        int numWorkers = 8; //args[2];
        int bufferSize = 10; //args[3];

        WorkerCounter workerCounter = new WorkerCounter();  // Crea el monitor WorkerCounter
        ThreadPool pool = new ThreadPool(bufferSize, numWorkers, workerCounter);

        for (int i = 1; i <= 100; i++) {
            pool.launch(new DummyTask("Task No." + i + "."));
        }

        pool.stop();
        workerCounter.trabajoTerminado(); //Espera a que no hayan trabajadores activos.

        long endTime = System.currentTimeMillis();
        System.out.println("Todos los threads worker terminaron sus tareas.");
    }
}
