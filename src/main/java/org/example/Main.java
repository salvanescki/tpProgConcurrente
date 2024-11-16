package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
         * El args[] es para los parámetros siguientes:
         * -modo : imagen simple con los pixeles solicitados o el archivo de test (.csv)
         * -k : cantidad de k vecinos más cercanos o k imágenes más parecidas, deseados.
         * -cant_threads : cantidad de Threads workers que va a crear el ThreadPool.
         * -tamano_buffer : El tamaño del buffer.
         */

        int kNeighbors = Integer.parseInt(args[0]); //args[1];
        int numWorkers = Integer.parseInt(args[1]); //args[2];
        int bufferSize = Integer.parseInt(args[2]); //args[3];
        String modo = args[3]; //"imagen" o "CSV"
        String archivoPath = args[4]; //"mnist_test.csv" o "prueba.png"
        String dataSetEntrenamientoPath = args[5]; //"mnist_train.csv"

        long tiempoInicio = System.currentTimeMillis();
        WorkerCounter workerCounter = new WorkerCounter();
        ThreadPool pool = new ThreadPool(bufferSize, numWorkers, workerCounter);

        ArrayList<ResultadoGlobal> resultadosGlobales = new ArrayList<>();

        // Cargar conjunto de entrenamiento
        List<Image> datasetEntrenamiento = cargarDataset(dataSetEntrenamientoPath, 0, Integer.MAX_VALUE);

        //Cargar tareas según el modo
        if (modo.equals("imagen")) {
            //Crear una tarea para una imagen
            analizarImagen(pool, archivoPath, datasetEntrenamiento, kNeighbors, resultadosGlobales);
        } else if (modo.equals("CSV")) {
            analizarCSV(pool, archivoPath, datasetEntrenamiento, kNeighbors, resultadosGlobales);
        }

        /*
        for(int i = 1; i <= 100; i++) {
            pool.iniciar(new DummyTask("Task No." + i + "."));
        }
        */

        pool.stop();
        workerCounter.trabajoTerminado(); //Espera a que no hayan trabajadores activos.

        for(ResultadoGlobal resultado: resultadosGlobales) {
            resultado.mostrarResultados();
        }

        long tiempoFinal = System.currentTimeMillis();
        System.out.println("El tiempo total de ejecución fue de : " + (tiempoFinal - tiempoInicio) + " milisegundos.");
        System.out.println("Todos los threads Workers han terminado sus tareas");
    }

    private static void analizarImagen(ThreadPool threadPool, String filePath, List<Image> datasetEntrenamiento, int k, List<ResultadoGlobal> resultadosGlobales) {
        try {
            // Ruta de la imagen proporcionada como entrada
            Image imagen = new Image(filePath);
            ResultadoGlobal rg = new ResultadoGlobal(k);
            resultadosGlobales.add(rg);
            threadPool.launch(new MNISTask(imagen, datasetEntrenamiento, k, rg));
        } catch (IOException e) {
        	 e.printStackTrace();
        }
    }
	
	private static void analizarCSV(ThreadPool threadPool, String archivoPrueba, List<Image> datasetEntrenamiento, int k, List<ResultadoGlobal> resultadosGlobales) {
        try {
            CSVReader csvReader = new CSVReader();
            final int maxLines = 10000;
            List<Image> imagenesPrueba = csvReader.read(archivoPrueba, 0, maxLines);

            float lineasPorRango = (float) maxLines / threadPool.getNumWorkers();
            System.out.println("Cantidad lineas por rangoo: " + lineasPorRango);

            for (Image imagen : imagenesPrueba) {
                ResultadoGlobal rg = new ResultadoGlobalTest(k, imagen.getTag());
                resultadosGlobales.add(rg);
                for(int i = 0; i < threadPool.getNumWorkers() - 1; i++) {
                    // Chequear caso borde
                    int initialIndex = (int) (i * lineasPorRango);
                    int finalIndex = (int) (initialIndex + lineasPorRango);
                    List<Image> bloque = datasetEntrenamiento.subList(initialIndex, finalIndex);
                    threadPool.launch(new MNISTask(imagen, bloque, k, rg));
                }
                int index = threadPool.getNumWorkers() - 1;
                int finalIndex = (int) Math.ceil(lineasPorRango);
                List<Image> bloque = datasetEntrenamiento.subList(index, finalIndex);
                threadPool.launch(new MNISTask(imagen, bloque, k, rg));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Image> cargarDataset(String archivo, int offset, int cantidad) {
        CSVReader csvReader = new CSVReader();
        try {
            return csvReader.read(archivo, offset, cantidad);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}


