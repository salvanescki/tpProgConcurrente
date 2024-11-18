package org.example;

import jdk.jshell.spi.ExecutionControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramaConTests extends Programa {

    private final ThreadPool threadPool;
    private final String archivoPrueba;
    private final int k;
    private final String dataSetEntrenamientoPath;
    private final List<ResultadoGlobal> resultadosGlobales;

    public ProgramaConTests(ThreadPool threadPool, String archivoPrueba, int k, String dataSetEntrenamientoPath) {
        this.threadPool = threadPool;
        this.archivoPrueba = archivoPrueba;
        this.k = k;
        this.dataSetEntrenamientoPath = dataSetEntrenamientoPath;
        this.resultadosGlobales = new ArrayList<>();
    }
    @Override
    void init() {
        cargarDataset(dataSetEntrenamientoPath, 0, Integer.MAX_VALUE);
        try {
            CSVReader csvReader = new CSVReader();
            final int maxLines = 10000;
            List<Image> imagenesPrueba = csvReader.read(archivoPrueba, 0, maxLines);

            float lineasPorRango = (float) maxLines / threadPool.getNumWorkers();

            for (Image imagen : imagenesPrueba) {
                ResultadoGlobal rg = new ResultadoGlobalTest(k, imagen.getTag());
                resultadosGlobales.add(rg);
                for (int i = 0; i < threadPool.getNumWorkers() - 1; i++) {
                    // Chequear caso borde
                    int initialIndex = (int) (i * lineasPorRango);
                    int finalIndex = (int) (initialIndex + lineasPorRango);
                    dispatchWorker(threadPool, initialIndex, finalIndex, dataset, imagen, k, rg);
                }
                int index = threadPool.getNumWorkers() - 1;
                int finalIndex = index + (int) Math.ceil(lineasPorRango);
                dispatchWorker(threadPool, index, finalIndex, dataset, imagen, k, rg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void printResultado() {
        int aciertos = 0;
        for (ResultadoGlobal resultado : resultadosGlobales) {
            try {
                aciertos += resultado.acierto();
            } catch (ExecutionControl.NotImplementedException e) {
                // No ocurre
            }
        }
        System.out.println("Aciertos: " + aciertos);
        System.out.println("Cant total: " + resultadosGlobales.size());
        double promedio = ((double) aciertos / resultadosGlobales.size()) * 100;
        System.out.println("Porcentaje de aciertos: " + promedio + "%");
    }
}
