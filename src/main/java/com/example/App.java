package com.example;

import com.example.service.AuthService;
import com.example.service.UserService;
import com.example.sockets.Server;
import com.example.util.LogToFile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    private static Server server;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Iniciar el servidor
        startServer();

        // Inicializar el cliente de logs y de autenticación
        LogToFile.initializeClientSocket();
        AuthService.initializeClientSocket();

        Image logo = new Image(getClass().getResourceAsStream("/icons/logo.png"));
        primaryStage.getIcons().add(logo);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Billetera Virtual");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // Cerrar el cliente de logs y de autenticación
        LogToFile.closeClientSocket();
        AuthService.closeClientSocket();

        // Detener el servidor
        stopServer();

        super.stop();
    }

    private void startServer() {
        if (server == null) {
            server = new Server();
            new Thread(server::start).start();
        }
    }

    private void stopServer() {
        if (server != null) {
            server.stop();
        }
    }

    public static void main(String[] args) {
        UserService.loadProperties();
        launch(args);
    }
}
