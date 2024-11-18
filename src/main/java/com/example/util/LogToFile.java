package com.example.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.sockets.ClientSocket;

public class LogToFile {
    private static ClientSocket clientSocket;
    public static void initializeClientSocket() {
        clientSocket = new ClientSocket("localhost", 12345);
    }

    public static void logToFile(String level, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = String.format("%s [%s]: %s", timestamp, level, message);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PropertiesLoader.getRutaFromProperties("log_path"), true))) {
            writer.write(logMessage + "\n");
        } catch (IOException e) {
            System.out.println("Error al escribir en el log: " + e.getMessage());
        }
        if (clientSocket != null) {
            clientSocket.sendMessage(logMessage);
        }
    }

    public static void closeClientSocket() {
        if (clientSocket != null) {
            clientSocket.close();
        }
    }
}
