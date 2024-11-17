package com.example.util;

import com.example.sockets.ClientSocket;
import com.example.service.UserService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogToFile {
    private static ClientSocket clientSocket;

    // Inicializar el cliente de sockets para logs
    public static void initializeClientSocket() {
        clientSocket = new ClientSocket("localhost", 12345);
    }

    public static void logToFile(String level, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = String.format("%s [%s]: %s", timestamp, level, message);

        // Escribir en el archivo de log
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(UserService.getLogPath(), true))) {
            writer.write(logMessage + "\n");
        } catch (IOException e) {
            System.out.println("Error al escribir en el log: " + e.getMessage());
        }

        // Enviar el mensaje al servidor
        if (clientSocket != null) {
            clientSocket.sendMessage(logMessage);
        }
    }

    // Método para cerrar el socket al finalizar
    public static void closeClientSocket() {
        if (clientSocket != null) {
            clientSocket.close();
        }
    }
}
