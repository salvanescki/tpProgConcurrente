package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Programa {

    protected final ThreadPool threadPool;
    protected final String filePath;
    protected final int k;
    protected final String dataSetEntrenamientoPath;
    protected final List<ResultadoGlobal> resultadosGlobales;
    protected static int maxLines = 60000;

    public Programa(ThreadPool threadPool, String filePath, int k, String dataSetEntrenamientoPath) {
        this.threadPool = threadPool;
        this.filePath = filePath;
        this.k = k;
        this.dataSetEntrenamientoPath = dataSetEntrenamientoPath;
        this.resultadosGlobales = new ArrayList<>();
    }

    protected List<Image> dataset;

    abstract void init();

    abstract void printResultado();

    protected void printTiempo() {
        long tiempoFinal = System.currentTimeMillis();
        System.out.println("El tiempo total de ejecución fue de : " + (tiempoFinal - Main.tiempoInicio) + " milisegundos.");
    }

    protected void dispatchImage(Image imagen) {
        float lineasPorRango = lineasPorRango();

        ResultadoGlobal rg = resultadoGlobal(imagen);
        resultadosGlobales.add(rg);
        for (int i = 0; i < threadPool.getNumWorkers() - 1; i++) {
            // Chequear caso borde
            int initialIndex = (int) (i * lineasPorRango);
            int finalIndex = (int) (initialIndex + lineasPorRango);
            dispatchWorker(threadPool, initialIndex, finalIndex, dataset, imagen, k, rg);
        }
        int index = threadPool.getNumWorkers() - 1;
        int offset = (int) (index * lineasPorRango);
        int finalIndex = offset + (int) Math.ceil(lineasPorRango);
        dispatchWorker(threadPool, offset, finalIndex, dataset, imagen, k, rg);
    }

    private float lineasPorRango() {
        return (float) maxLines / threadPool.getNumWorkers();
    }

    protected abstract ResultadoGlobal resultadoGlobal(Image imagen);

    protected void dispatchWorker(ThreadPool threadPool, int index, int finalIndex, List<Image> dataset, Image image, int k, ResultadoGlobal rg) {
        List<Image> bloque = dataset.subList(index, finalIndex);
        threadPool.launch(new MNISTask(image, bloque, k, rg));
    }

    protected void cargarDataset(String archivo, int offset, int cantidad) {
        Main.tiempoInicio = System.currentTimeMillis();
        CSVReader csvReader = new CSVReader();
        try {
            dataset = csvReader.read(archivo, offset, cantidad);
        } catch (IOException e) {
            e.printStackTrace();
            dataset = Collections.emptyList();
        }
    }
}
