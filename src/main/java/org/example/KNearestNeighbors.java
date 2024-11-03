import java.util.Comparator;
import java.util.PriorityQueue;

public class KNearestNeighbors {
  public static class PuntoDistancia {
        int indice;
        double distancia;

        public IndiceDistancia(int indice, double distancia) {
            this.indice = indice;
            this.distancia = distancia;
        }
    }

  public static PriorityQueue<PuntoDistancia> obtenerKVecinosMasCercanos(byte[] imagenTarget, List<byte[]> dataset, int k) {
        PriorityQueue<IndiceDistancia> pq = new PriorityQueue<>(Comparator.comparingDouble(p -> p.distancia));

        for (int i = 0; i < dataset.size(); i++) {
            int[] punto = dataset.get(i);
            double distancia = calcularDistancia(target, punto);
            pq.add(new PuntoDistancia(i, distancia));
        }

        PriorityQueue<PuntoDistancia> kVecinos = new PriorityQueue<>(Comparator.comparingDouble(p -> -p.distancia));
        for (int i = 0; i < k && !pq.isEmpty(); i++) {
            kVecinos.add(pq.poll());
        }
        return kVecinos;
    }

  private static double calcularDistancia(int[] p1, int[] p2) {
        double suma = 0;
        for (int i = 0; i < p1.length; i++) {
            suma += Math.pow(p1[i] - p2[i], 2);
        }
        return Math.sqrt(suma);
    }
 
}
