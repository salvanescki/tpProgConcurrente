package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class Image {

    private byte tag = 127;
    private final byte[] data;

    public Image(byte tag, byte[] values) {
        this.tag = tag;
        this.data = values;
    }

    public Image(String path) throws IOException {
        File imgPath = new File(path);
        BufferedImage bufferedImage = ImageIO.read(imgPath);
        WritableRaster raster = bufferedImage.getRaster();
        data = ((DataBufferByte) raster.getDataBuffer()).getData();
    }

    public byte[] getColors() {
        return data;
    }

    public byte getTag() {
        verifyTag();
        return tag;
    }

    public void verifyTag() {
        if (tag == 127) throw new RuntimeException("El tag de la imagen, es inválido");
    }

    public void saveVectorToImage() {
        verifyTag();
        BufferedImage imagen = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);
        imagen.getRaster().setDataElements(0, 0, 28, 28, data);
        File outputfile = new File("ImageOut");
        try {
            ImageIO.write(imagen, "png", outputfile);
        } catch (IOException e) {
        }
    }

}
