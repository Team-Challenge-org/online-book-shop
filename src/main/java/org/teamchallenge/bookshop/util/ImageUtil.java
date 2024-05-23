package org.teamchallenge.bookshop.util;

import jakarta.xml.bind.DatatypeConverter;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil {

    public static BufferedImage resizeImageByHeight(BufferedImage originalImage, int targetHeight) {
        return Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, targetHeight);
    }

    public static BufferedImage base64ToBufferedImage(String base64String) {
        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64String);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(bais);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String bufferedImageToBase64(BufferedImage image) {
        try {
            return DatatypeConverter.printBase64Binary(bufferedImageToBytes(image));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] bufferedImageToBytes(BufferedImage bufferedImage) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}