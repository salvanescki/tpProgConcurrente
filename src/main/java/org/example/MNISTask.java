package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MNISTask extends Task {

    private final int k;
	private final Image imagenTarget;
	private final ResultadoGlobal resultadoGlobal;
	private final List<Image> dataSet;
	
	public MNISTask(Image imagenTarget, List<Image> dataSet, int k, ResultadoGlobal resultadoGlobal) {
		this.imagenTarget = imagenTarget;
        this.dataSet = dataSet;
        this.k = k;
        this.resultadoGlobal = resultadoGlobal;
	}
	
	@Override
	public void run() {
		List<byte[]> dataSetBytes = new ArrayList<>(); 
		for (Image image : dataSet) {
		    dataSetBytes.add(image.getColors()); 
		}
		
		PriorityQueue<KNearestNeighbors.PuntoDistancia> kVecinos =
				KNearestNeighbors.obtenerKVecinosMasCercanos(imagenTarget.getColors(), dataSetBytes, k);
		
		// Registrar los resultados en la estructura global
        synchronized (resultadoGlobal) {
            resultadoGlobal.agregarVecinos(kVecinos);
        }
	}

}
