package com.tankbattle.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ResourceManager {
    private Map<String, BufferedImage> images = new HashMap<>();

    public BufferedImage loadImage(String path) {
        if (!images.containsKey(path)) {
            try {
                BufferedImage image = ImageIO.read(getClass().getResourceAsStream(path));
                if (image == null) {
                    throw new IOException("Image not found: " + path);
                }
                images.put(path, image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images.get(path);
    }
    
}

