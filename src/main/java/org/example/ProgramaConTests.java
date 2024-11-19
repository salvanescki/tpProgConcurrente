package org.example;

import jdk.jshell.spi.ExecutionControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProgramaConTests extends Programa {

    public ProgramaConTests(ThreadPool threadPool, String filePath, int k, String dataSetEntrenamientoPath) {
        super(threadPool, filePath, k, dataSetEntrenamientoPath);
    }

    @Override
    void init() {
        cargarDataset(dataSetEntrenamientoPath, 0, Integer.MAX_VALUE);
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingresar la cantidad de líneas a evaluar: ");
            int lineasALeer = Integer.parseInt(scanner.nextLine());
            System.out.println("Analizando " + lineasALeer + " líneas...");
            scanner.close();
            CSVReader csvReader = new CSVReader();
            List<Image> imagenesPrueba = csvReader.read(filePath, 0, lineasALeer);

            for (Image imagen : imagenesPrueba) {
                dispatchImage(imagen);
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

    @Override
    protected ResultadoGlobal resultadoGlobal(Image imagen) {
        return new ResultadoGlobalTest(k, imagen.getTag());
    }
}
