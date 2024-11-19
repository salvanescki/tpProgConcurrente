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
        List<KNearestNeighbors.PuntoDistancia> puntoDistancias = new ArrayList<>();
        Map<Integer, Integer> frecuencias = new HashMap<>();

        for (int i = 0; i < k; i++) {
            KNearestNeighbors.PuntoDistancia pd = pq.poll();
            puntoDistancias.addLast(pd);
        }

        // Creo frecuencias
        for (KNearestNeighbors.PuntoDistancia pd: puntoDistancias) {
            int tag = Byte.toUnsignedInt(pd.tag);
            frecuencias.put(tag, frecuencias.getOrDefault(tag, 0) + 1);
        }

        List<Map.Entry<Integer, Integer>> lista = new ArrayList<>(frecuencias.entrySet());

        // Guardo la mayor frecuencia
        int mayorFrecuencia = lista.stream().mapToInt(Map.Entry::getValue)
                .max()
                .getAsInt();

        // Filtro por la mayor frecuencia, por tener en cuenta el caso de empate entre uno o varios
        List<Integer> tagsConMayorFrecuencia = lista.stream()
                .filter(entrada -> entrada.getValue() == mayorFrecuencia)
                .map(Map.Entry::getKey)
                .toList();

        // Filtro los elementos de los tags que tuvieron mayor frecuencia
        List<KNearestNeighbors.PuntoDistancia> distanciasDeTagsConMayorFrecuencia =
                puntoDistancias.stream()
                        .filter(puntoDistancia -> tagsConMayorFrecuencia.stream().anyMatch(tag -> puntoDistancia.tag == tag))
                        .toList();

        // Guardo la menor distancia, en caso de que hayan empatado por frecuencia
        long menorDistancia = distanciasDeTagsConMayorFrecuencia.stream()
                .mapToLong(entrada -> entrada.distancia)
                .min()
                .getAsLong();

        // Filtro por menor distancia. En caso de empatar mas de uno en caso de mayor frecuencia y menor distancia,
        // desempato por orden de menor a mayor
        List<Integer> tagsConMenorDistanciaYMayorFrecuencia = distanciasDeTagsConMayorFrecuencia.stream()
                .filter(puntoDistancia -> puntoDistancia.distancia == menorDistancia)
                .map(puntoDistancia -> Byte.toUnsignedInt(puntoDistancia.tag))
                .sorted(Comparator.naturalOrder())
                .toList();

        return tagsConMenorDistanciaYMayorFrecuencia;
    }

    public int acierto() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

}
