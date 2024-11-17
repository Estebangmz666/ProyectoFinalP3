package com.example.sockets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {
    private Socket socket;
    private PrintWriter out;

    public ClientSocket(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Cliente conectado al servidor en " + host + ":" + port);
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void close() {
        try {
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Cliente desconectado del servidor");
        } catch (IOException e) {
            System.err.println("Error al cerrar el cliente: " + e.getMessage());
        }
    }
}