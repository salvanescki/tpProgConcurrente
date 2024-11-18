package org.example;

public class ProgramaFactory {
    public static Programa crearPrograma(String modo, ThreadPool threadPool, String archivoPrueba, int k, String dataSetEntrenamientoPath) {
        String formattedModo = modo.toLowerCase();
        if (formattedModo.equals("csv")) {
            return new ProgramaConTests(threadPool, archivoPrueba, k, dataSetEntrenamientoPath);
        } else if (formattedModo.equals("png")) {
            return new ProgramaConImagenEntrada(threadPool, archivoPrueba, k, dataSetEntrenamientoPath);
        }
        throw new IllegalArgumentException("Modo " + modo + " no conocido");
    }
}
