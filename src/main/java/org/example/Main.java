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
    public static void main(String[] args) throws ExecutionControl.NotImplementedException {

        int kNeighbors = Integer.parseInt(args[0]); //args[1];
        int numWorkers = Integer.parseInt(args[1]); //args[2];
        int bufferSize = Integer.parseInt(args[2]); //args[3];
        String archivoPath = args[3]; //"mnist_test.csv" o "prueba.png"
        String dataSetEntrenamientoPath = args[4]; //"mnist_train.csv"

        String modo = args[3].split("\\.")[1]; // "png" o "csv"

        long tiempoInicio = System.currentTimeMillis();
        WorkerCounter workerCounter = new WorkerCounter();
        ThreadPool pool = new ThreadPool(bufferSize, numWorkers, workerCounter);

        ArrayList<ResultadoGlobal> resultadosGlobales = new ArrayList<>();

        // Cargar conjunto de entrenamiento
        List<Image> datasetEntrenamiento = cargarDataset(dataSetEntrenamientoPath, 0, Integer.MAX_VALUE);

        //Cargar tareas según el modo
        if (modo.equals("png")) {
            //Crear una tarea para una imagen
            analizarImagen(pool, archivoPath, datasetEntrenamiento, kNeighbors, resultadosGlobales);
        } else if (modo.equals("csv")) {
            analizarCSV(pool, archivoPath, datasetEntrenamiento, kNeighbors, resultadosGlobales);
        }

        pool.stop();
        workerCounter.trabajoTerminado(); //Espera a que no hayan trabajadores activos.

        if (modo.equals("csv")) {
            int aciertos = 0;
            for(ResultadoGlobal resultado: resultadosGlobales) {
                aciertos += resultado.acierto();
            }
            System.out.println("Aciertos: " + aciertos);
            System.out.println("Cant total: " + resultadosGlobales.size());
            double promedio = ((double) aciertos / resultadosGlobales.size()) * 100;
            System.out.println("Porcentaje de aciertos: " + promedio + "%");
        } else {
            System.out.println("El resultado final es: " + resultadosGlobales.getFirst().tagGanador());
        }

        long tiempoFinal = System.currentTimeMillis();
        System.out.println("El tiempo total de ejecución fue de : " + (tiempoFinal - tiempoInicio) + " milisegundos.");
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

            for (Image imagen : imagenesPrueba) {
                ResultadoGlobal rg = new ResultadoGlobalTest(k, imagen.getTag());
                resultadosGlobales.add(rg);
                for(int i = 0; i < threadPool.getNumWorkers() - 1; i++) {
                    // Chequear caso borde
                    int initialIndex = (int) (i * lineasPorRango);
                    int finalIndex = (int) (initialIndex + lineasPorRango);
                    dispatchWorker(threadPool, initialIndex, finalIndex, datasetEntrenamiento, imagen, k, rg);
                }
                int index = threadPool.getNumWorkers() - 1;
                int finalIndex = index + (int) Math.ceil(lineasPorRango);
                dispatchWorker(threadPool, index, finalIndex, datasetEntrenamiento, imagen, k, rg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void dispatchWorker(ThreadPool threadPool, int index, int finalIndex, List<Image> dataset, Image image, int k, ResultadoGlobal rg) {
        List<Image> bloque = dataset.subList(index, finalIndex);
        threadPool.launch(new MNISTask(image, bloque, k, rg));
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


