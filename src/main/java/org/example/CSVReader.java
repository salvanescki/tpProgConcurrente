package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public List<Image> read(String fileName, int offset, int linesToRead) throws IOException {

        FileReader fileReader = new FileReader(fileName);

        try (BufferedReader br = new BufferedReader(fileReader)) {
            String line;
            for (int i = 0; i < offset; i++) {
                line = br.readLine();
            }

            List<Image> images = new ArrayList<Image>();

            for (int i = 0; i < linesToRead; i++) {
                line = br.readLine();
                images.add(parseLine(line));
            }

            return images;
        }
    }

    public Image parseLine(String line) {
        String[] vals_str = line.split(",");
        byte[] data = new byte[vals_str.length - 1];

        byte tag = getValue(vals_str, 0);

        for (int i = 1; i < vals_str.length; i++) {
            data[i] = getValue(vals_str, i);
        }

        return new Image(tag, data);
    }

    public byte getValue(String[] arr, int i) {
        int num = Integer.parseInt(arr[i]);
        return (byte) num;
    }

}
