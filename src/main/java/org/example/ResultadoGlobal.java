package org.example;

import java.util.Comparator;
import java.util.PriorityQueue;

public class ResultadoGlobal {
	private final PriorityQueue<KNearestNeighbors.PuntoDistancia> globalKVecinos;

    public ResultadoGlobal(int k) {
        this.globalKVecinos = new PriorityQueue<>(Comparator.comparingLong(p -> p.distancia));
    }

    public synchronized void agregarVecinos(PriorityQueue<KNearestNeighbors.PuntoDistancia> vecinosLocales) {
        while (!vecinosLocales.isEmpty()) {
            globalKVecinos.add(vecinosLocales.poll());
        }
    }

    public synchronized PriorityQueue<KNearestNeighbors.PuntoDistancia> obtenerResultados() {
        return new PriorityQueue<>(globalKVecinos); // Copia para evitar modificar el original
    }

     // Mostrar los resultados
    public void mostrarResultados() {
        System.out.println("Vecinos más cercanos (k vecinos):");
        int contador = 1;
        for (KNearestNeighbors.PuntoDistancia punto : globalKVecinos) {
            System.out.printf("Vecino #%d: Indice: %d, Distancia: %d%n", contador++, punto.indice, punto.distancia);
        }
        mostrarResultadoFinal();
    }

    protected void mostrarResultadoFinal() {
        PriorityQueue<KNearestNeighbors.PuntoDistancia> resultados = obtenerResultados();
        KNearestNeighbors.PuntoDistancia puntoGanador = resultados.poll();
        System.out.println("Resultado tag: " + puntoGanador.tag);
    }
}
