package com.github.jasgo.capture.test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ScreenCapture {
    public static void main(String[] args) {
        try {
            File f = new File("capture.png");
            BufferedImage image = capture();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();
            String img = Base64.getEncoder().encodeToString(data);
            System.out.println(Base64.getEncoder().encodeToString(data));
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(img));
            BufferedImage bi = ImageIO.read(byteArrayInputStream);
            ImageIO.write(bi, "png", f);
        } catch (AWTException|IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static BufferedImage capture() throws AWTException {
        Robot robot = new Robot();
        Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return robot.createScreenCapture(area);
    }
}
