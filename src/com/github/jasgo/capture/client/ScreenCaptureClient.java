package com.github.jasgo.capture.client;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ScreenCaptureClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 3000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            String s;
            System.out.println("서버에 연결되었습니다.");
            while ((s = reader.readLine()) != null) {
                if (s.equalsIgnoreCase("capture")) {
                    System.out.println("Screen Capture");
                    BufferedImage image = capture();
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", output);
                    byte[] data = output.toByteArray();
                    writer.println(Base64.getEncoder().encodeToString(data));
                } else if (s.equalsIgnoreCase("cam")) {
                    System.out.println("Webcam Activated");
                    BufferedImage image = cam();
                    if (image == null) {
                        writer.println("해당 기기에 웹캠이 존재하지 않습니다.");
                    } else {
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", output);
                        byte[] data = output.toByteArray();
                        writer.println(Base64.getEncoder().encodeToString(data));
                    }
                }
            }
        } catch (IOException | AWTException e) {
            throw new RuntimeException(e);
        }

    }

    public static BufferedImage capture() throws AWTException {
        Robot robot = new Robot();
        Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return robot.createScreenCapture(area);
    }
    public static BufferedImage cam() {
        Webcam webcam = Webcam.getDefault();
        if (webcam != null) {
            webcam.open();
            BufferedImage image = webcam.getImage();
            webcam.close();
            return image;
        } else {
            return null;
        }
    }
}
