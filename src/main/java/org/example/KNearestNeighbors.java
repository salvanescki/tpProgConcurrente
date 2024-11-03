package org.example;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class KNearestNeighbors {

  public static class PuntoDistancia {
        int indice;
      long distancia;

      public PuntoDistancia(int indice, long distancia) {
            this.indice = indice;
            this.distancia = distancia;
        }
    }

  public static PriorityQueue<PuntoDistancia> obtenerKVecinosMasCercanos(byte[] imagenTarget, List<byte[]> dataset, int k) {
      PriorityQueue<PuntoDistancia> pq = new PriorityQueue<PuntoDistancia>(Comparator.comparingLong(p -> p.distancia));

        for (int i = 0; i < dataset.size(); i++) {
            byte[] punto = dataset.get(i);
            long distancia = calcularDistancia(imagenTarget, punto);
            pq.add(new PuntoDistancia(i, distancia));
        }

      PriorityQueue<PuntoDistancia> kVecinos = new PriorityQueue<PuntoDistancia>(Comparator.comparingLong(p -> p.distancia));

        for (int i = 0; i < k && !pq.isEmpty(); i++) {
            kVecinos.add(pq.poll());
        }
        return kVecinos;
    }

    private static long calcularDistancia(byte[] p1, byte[] p2) {
        long suma = 0;
        for (int i = 0; i < p1.length; i++) {
            suma += (long) Math.pow(p1[i] - p2[i], 2);
        }
        return suma; // O math.sqrt(suma) o abs() en vez de pow
    }
 
}
