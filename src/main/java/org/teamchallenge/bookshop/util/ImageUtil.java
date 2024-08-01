package org.teamchallenge.bookshop.util;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil {

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        float aspectRatio = (float) originalWidth / originalHeight;

        if (originalWidth > originalHeight) {
            targetHeight = Math.round(targetWidth / aspectRatio);
        } else {
            targetWidth = Math.round(targetHeight * aspectRatio);
        }

        return Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight);
    }
    public static BufferedImage base64ToBufferedImage(String base64String) {
        byte[] imageBytes = Base64.getDecoder().decode(base64String);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(bais);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String bufferedImageToBase64(BufferedImage image) {
        try {
            byte[] imageBytes = bufferedImageToBytes(image);
            return Base64.getEncoder().encodeToString(imageBytes);
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