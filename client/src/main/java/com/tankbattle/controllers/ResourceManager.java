package com.tankbattle.controllers;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

public class ResourceManager {
    private Map<String, BufferedImage> images = new ConcurrentHashMap<>();

    public BufferedImage loadImage(String fileName) throws RuntimeException {
        if (!images.containsKey(fileName)) {
            URL resource = this.getClass().getClassLoader().getResource(fileName);
            if (resource == null) {
                throw new RuntimeException("Resource not found: " + fileName);
            }
            try {
                BufferedImage image = ImageIO.read(resource);
                // null is returned if the ImageReader cannot read the image
                if (image == null) {
                    throw new RuntimeException("Cannot read image: " + fileName);
                }

                images.put(fileName, image);
            } catch (IOException e)  {
                e.printStackTrace();
                throw new RuntimeException(fileName + " not found");
            }
        }
        return images.get(fileName);
    }
    
}

