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
                System.out.println(i + line);
            }

            List<Image> images = new ArrayList<>();

            for (int i = 0; i < linesToRead; i++) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                images.add(parseLine(line));
            }

            return images;
        }
    }

    public Image parseLine(String line) {
        String[] vals_str = line.split(",");
        byte[] data = new byte[vals_str.length - 1];

        byte tag = getValue(vals_str, 0);

        for (int i = 0; i < vals_str.length - 1; i++) {
            data[i] = getValue(vals_str, i + 1);
        }

        return new Image(tag, data);
    }

    public byte getValue(String[] arr, int i) {
        int num = Integer.parseInt(arr[i]);
        return (byte) num;
    }

}
