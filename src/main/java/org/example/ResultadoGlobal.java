package org.example;

import jdk.jshell.spi.ExecutionControl;

import java.util.*;

public class ResultadoGlobal {
	private final PriorityQueue<KNearestNeighbors.PuntoDistancia> globalKVecinos;
    int k;

    public ResultadoGlobal(int k) {
        this.globalKVecinos = new PriorityQueue<>(Comparator.comparingLong(p -> p.distancia));
        this.k = k;
    }

    public synchronized void agregarVecinos(PriorityQueue<KNearestNeighbors.PuntoDistancia> vecinosLocales) {
        while (!vecinosLocales.isEmpty()) {
            globalKVecinos.add(vecinosLocales.poll());
        }
    }

    public synchronized PriorityQueue<KNearestNeighbors.PuntoDistancia> obtenerResultados() {
        return new PriorityQueue<>(globalKVecinos); // Copia para evitar modificar el original
    }

    protected int tagGanador() {
        PriorityQueue<KNearestNeighbors.PuntoDistancia> resultados = obtenerResultados();
//        List<Map.Entry<Integer, Integer>> frecuencias = ordenarPorFrecuencia(resultados, k);
//        Map.Entry<Integer, Integer> ganador = frecuencias.getFirst();
        return resultados.poll().tag;
    }

    private List<Map.Entry<Integer, Integer>> ordenarPorFrecuencia(PriorityQueue<KNearestNeighbors.PuntoDistancia> pq, int k) {
        Map<Integer, Integer> frecuencias = new HashMap<>();

        for (int i = 0; i < k && !pq.isEmpty(); i++) {
            KNearestNeighbors.PuntoDistancia elemento = pq.poll();
            int tag = Byte.toUnsignedInt(elemento.tag);
            frecuencias.put(tag, frecuencias.getOrDefault(tag, 0) + 1);
        }

        List<Map.Entry<Integer, Integer>> lista = new ArrayList<>(frecuencias.entrySet());

        lista.sort((a, b) -> {
            int freqComp = b.getValue() - a.getValue();
            if (freqComp == 0) {
                return a.getKey().compareTo(b.getKey());
            } else {
                return freqComp;
            }
        });

        return lista;
    }

    public int acierto() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

}
