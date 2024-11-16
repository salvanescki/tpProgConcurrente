package org.example;

import jdk.jshell.spi.ExecutionControl;

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

    protected byte tagGanador() {
        PriorityQueue<KNearestNeighbors.PuntoDistancia> resultados = obtenerResultados();
        KNearestNeighbors.PuntoDistancia puntoGanador = resultados.poll();
        return puntoGanador.tag;
    }

    public int acierto() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

}
