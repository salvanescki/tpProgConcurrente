package org.example;

import jdk.jshell.spi.ExecutionControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramaConTests extends Programa {

    public ProgramaConTests(ThreadPool threadPool, String filePath, int k, String dataSetEntrenamientoPath) {
        super(threadPool, filePath, k, dataSetEntrenamientoPath);
    }

    @Override
    void init() {
        cargarDataset(dataSetEntrenamientoPath, 0, Integer.MAX_VALUE);
        try {
            CSVReader csvReader = new CSVReader();
            final int maxLines = 60000;
            List<Image> imagenesPrueba = csvReader.read(filePath, 0, maxLines);

            float lineasPorRango = (float) maxLines / threadPool.getNumWorkers();

            for (Image imagen : imagenesPrueba) {
                dispatchImage(imagen, lineasPorRango);
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
