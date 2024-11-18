package org.example;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class Programa {

    protected List<Image> dataset;

    abstract void init();

    abstract void printResultado();

    protected void dispatchWorker(ThreadPool threadPool, int index, int finalIndex, List<Image> dataset, Image image, int k, ResultadoGlobal rg) {
        List<Image> bloque = dataset.subList(index, finalIndex);
        threadPool.launch(new MNISTask(image, bloque, k, rg));
    }

    protected void cargarDataset(String archivo, int offset, int cantidad) {
        CSVReader csvReader = new CSVReader();
        try {
            dataset = csvReader.read(archivo, offset, cantidad);
        } catch (IOException e) {
            e.printStackTrace();
            dataset = Collections.emptyList();
        }
    }
}
