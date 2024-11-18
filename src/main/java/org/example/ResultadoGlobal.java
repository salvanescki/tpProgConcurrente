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
        List<Integer> frecuencias = ordenarPorFrecuenciaYDistancia(resultados, k);
        return frecuencias.getFirst();
    }

    private List<Integer> ordenarPorFrecuenciaYDistancia(PriorityQueue<KNearestNeighbors.PuntoDistancia> pq, int k) {
        List<KNearestNeighbors.PuntoDistancia> puntoDistancias = pq.stream().limit(k).toList();
        Map<Integer, Integer> frecuencias = new HashMap<>();

        // Creo frecuencias
        for (KNearestNeighbors.PuntoDistancia puntoDistancia : puntoDistancias) {
            int tag = Byte.toUnsignedInt(puntoDistancia.tag);
            frecuencias.put(tag, frecuencias.getOrDefault(tag, 0) + 1);
        }

        List<Map.Entry<Integer, Integer>> lista = new ArrayList<>(frecuencias.entrySet());

        // Agarro el de mayor frecuencia
        int mayorFrecuencia = lista.stream().mapToInt(Map.Entry::getValue)
                .max()
                .getAsInt();

        // Filtro por la mayor frecuencia, por tener en cuenta el caso de empate
        List<Integer> tagsConMayorFrecuencia = lista.stream()
                .filter(entrada -> entrada.getValue() == mayorFrecuencia)
                .map(Map.Entry::getKey)
                .toList();

        // Filtro los elementos por tag
        List<KNearestNeighbors.PuntoDistancia> distanciasDeTagsConMayorFrecuencia =
                puntoDistancias.stream()
                        .filter(puntoDistancia -> tagsConMayorFrecuencia.stream().anyMatch(tag -> puntoDistancia.tag == tag))
                        .toList();

        long menorDistancia = distanciasDeTagsConMayorFrecuencia.stream()
                .mapToLong(entrada -> entrada.distancia)
                .min()
                .getAsLong();

        List<Integer> tagsConMenorDistanciaYMayorFrecuencia = distanciasDeTagsConMayorFrecuencia.stream()
                .filter(puntoDistancia -> puntoDistancia.distancia == menorDistancia)
                .map(puntoDistancia -> puntoDistancia.tag & 0xff)
                .sorted(Comparator.naturalOrder())
                .toList();

        // Ordeno por frecuencias, de menor a mayor

        return tagsConMenorDistanciaYMayorFrecuencia;
    }

    public int acierto() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

}
