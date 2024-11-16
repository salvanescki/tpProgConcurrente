package org.example;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class KNearestNeighbors {

  public static class PuntoDistancia {
      int indice;
      byte tag;
      public long distancia;

      public PuntoDistancia(int indice, long distancia, byte tag) {
            this.indice = indice;
            this.distancia = distancia;
            this.tag = tag;
        }
    }

  public static PriorityQueue<PuntoDistancia> obtenerKVecinosMasCercanos(Image imageTarget, List<Image> dataset, int k) {
        byte[] colors = imageTarget.getColors();
        PriorityQueue<PuntoDistancia> pq = new PriorityQueue<PuntoDistancia>(Comparator.comparingLong(p -> p.distancia));

        for (int i = 0; i < dataset.size(); i++) {
            byte[] punto = dataset.get(i).getColors();
            long distancia = calcularDistancia(colors, punto);
            PuntoDistancia puntoDistancia = new PuntoDistancia(i, distancia, dataset.get(i).getTag());
            pq.add(puntoDistancia);
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
            int i1 = p1[i] & 0xff;
            int i2 = p2[i] & 0xff;
            suma += (long) Math.pow(i1 - i2, 2);
        }
        return suma; // O math.sqrt(suma) o abs() en vez de pow
    }
 
}
