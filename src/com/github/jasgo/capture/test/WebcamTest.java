package com.github.jasgo.capture.test;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WebcamTest {
    public static void main(String[] args) {
        Webcam webcam = Webcam.getDefault();
        if (webcam != null) {
            System.out.println(webcam.getName());
            webcam.open();
            try {
                ImageIO.write(webcam.getImage(), "png", new File("webcam.png"));
                webcam.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
