package org.example;

import java.io.IOException;

public class ProgramaConImagenEntrada extends Programa {

    public ProgramaConImagenEntrada(ThreadPool threadPool, String filePath, int k, String dataSetEntrenamientoPath) {
        super(threadPool, filePath, k, dataSetEntrenamientoPath);
    }

    @Override
    void init() {
        cargarDataset(dataSetEntrenamientoPath, 0, Integer.MAX_VALUE);
        try {
            // Ruta de la imagen proporcionada como entrada
            Image imagen = new Image(filePath);
            dispatchImage(imagen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void printResultado() {
        System.out.println("El resultado final es: " + resultadosGlobales.getFirst().tagGanador());
    }

    @Override
    protected ResultadoGlobal resultadoGlobal(Image imagen) {
        return new ResultadoGlobal(k);
    }
}
