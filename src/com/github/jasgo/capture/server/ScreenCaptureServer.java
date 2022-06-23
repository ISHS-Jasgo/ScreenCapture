package com.github.jasgo.capture.server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class ScreenCaptureServer extends Thread {

    public static Socket socket = null;

    public ScreenCaptureServer(Socket socket) {
        ScreenCaptureServer.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader system = new BufferedReader(new InputStreamReader(System.in));
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            String readLine;
            while ((readLine = system.readLine()) != null) {
                if (readLine.equalsIgnoreCase("capture")) {
                    writer.println("capture");
                } else if (readLine.equalsIgnoreCase("cam")) {
                    writer.println("cam");
                }
            }
        } catch (IOException e) {
            System.out.println("사용자와 연결이 종료되었습니다.");
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(3000);
            while (true) {
                Socket socket = server.accept();
                Thread thread = new ScreenCaptureServer(socket);
                thread.start();
                new ListeningThread().start();
            }
         } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static class ListeningThread extends Thread {
        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
                String readValue;
                while ((readValue = reader.readLine()) != null) {
                    System.out.println("Capture data Reached");
                    System.out.println(readValue);
                    byte[] data = Base64.getDecoder().decode(readValue);
                    ByteArrayInputStream input = new ByteArrayInputStream(data);
                    BufferedImage image = ImageIO.read(input);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
                    LocalDateTime now = LocalDateTime.now();
                    File f = new File(String.format("capture_%s.png", dtf.format(now)));
                    ImageIO.write(image, "png", f);
                }
            } catch (IOException e) {
                System.out.println("사용자와 연결이 종료되었습니다.");
            }
        }
    }
}
