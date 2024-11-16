package com.andrewsreis.dundermifflin.utils;

import java.awt.image.BufferedImage;

public abstract class ImageUtil {

    public static String beautify(BufferedImage photo) {
        try {
            // Resize image for better terminal fit
            int newWidth = 60; // Adjust for your terminal size
            int newHeight = (photo.getHeight() * newWidth) / photo.getWidth();

            BufferedImage resizedImage = resizeImage(photo, newWidth, newHeight);

            return renderImageInAnsi(resizedImage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to beautify the image", e);
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        resized.getGraphics().drawImage(originalImage, 0, 0, width, height, null);
        return resized;
    }

    private static String renderImageInAnsi(BufferedImage image) {
        StringBuilder builder = new StringBuilder();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);

                // Extract RGB values
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // ANSI escape code for 24-bit color
                builder.append(String.format("\u001B[48;2;%d;%d;%dm ", red, green, blue));
            }
            builder.append("\u001B[0m\n"); // Reset color at the end of the line
        }

        return builder.toString();
    }
}
