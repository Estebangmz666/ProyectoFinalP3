package com.example.sockets;


import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    // Interfaz gráfica
    private JFrame frame;
    private JTextArea textArea;

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            executorService = Executors.newCachedThreadPool();
            setupGUI();
            logMessage("Servidor iniciado en el puerto " + PORT);
        } catch (IOException e) {
            logMessage("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    // Configuración de la interfaz gráfica
    private void setupGUI() {
        frame = new JFrame("Servidor - Logs");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Método para mostrar mensajes en la interfaz gráfica
    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> textArea.append(message + "\n"));
    }

    public void start() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                logMessage("Cliente conectado al servidor");
                executorService.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            logMessage("Error al aceptar conexión: " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String message;
            while ((message = in.readLine()) != null) {
                logMessage(message); // Mostrar cada mensaje de log en la interfaz gráfica del servidor
            }
        } catch (IOException e) {
            logMessage("Error al manejar el cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                logMessage("Cliente desconectado del servidor");
            } catch (IOException e) {
                logMessage("Error al cerrar la conexión del cliente: " + e.getMessage());
            }
        }
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            executorService.shutdown();
            logMessage("Servidor detenido");
        } catch (IOException e) {
            logMessage("Error al detener el servidor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}


