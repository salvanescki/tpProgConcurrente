package org.example;

import jdk.jshell.spi.ExecutionControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    /**
     * Se toman los argumentos en el siguiente orden:
     * <ul>
     *     <li>args[0] : Los k vecinos más cercanos deseados</li>
     *     <li>args[1] : La cantidad de threads Workers a utilizar en el procesamiento</li>
     *     <li>args[2] : El tamaño del buffer de tareas</li>
     *     <li>args[3] : El path (ruta) del archivo de test "mnist_test.csv" o de la imagen .png (define el modo del programa)</li>
     *     <li>args[4] : El path (ruta) de la base de datos mnist_train.csv</li>
     * </ul>
     *
     * @param args
     * @throws ExecutionControl.NotImplementedException
     */

    static int kNeighbors;
    static int numWorkers;
    static int bufferSize;
    static String archivoPath;
    static String dataSetEntrenamientoPath;
    static String modo;

    public static void main(String[] args) throws ExecutionControl.NotImplementedException {
        initArgs(args);

        long tiempoInicio = System.currentTimeMillis();

        WorkerCounter workerCounter = new WorkerCounter();
        ThreadPool pool = new ThreadPool(bufferSize, numWorkers, workerCounter);

        Programa program = ProgramaFactory.crearPrograma(modo, pool, archivoPath, kNeighbors, dataSetEntrenamientoPath);

        program.init();

        pool.stop();
        workerCounter.trabajoTerminado(); //Espera a que no hayan trabajadores activos.

        program.printResultado();

        long tiempoFinal = System.currentTimeMillis();
        System.out.println("El tiempo total de ejecución fue de : " + (tiempoFinal - tiempoInicio) + " milisegundos.");
    }

    private static void initArgs(String[] args) {
        kNeighbors = Integer.parseInt(args[0]); //args[1];
        numWorkers = Integer.parseInt(args[1]); //args[2];
        bufferSize = Integer.parseInt(args[2]); //args[3];
        archivoPath = args[3]; //"mnist_test.csv" o "prueba.png"
        dataSetEntrenamientoPath = args[4]; //"mnist_train.csv"
        modo = args[3].split("\\.")[1]; // "png" o "csv"
    }


}


