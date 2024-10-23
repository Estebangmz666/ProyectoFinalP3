package com.example;

import com.example.service.UserService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application{
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        Image logo = new Image(getClass().getResourceAsStream("/icons/logo.png"));
        primaryStage.getIcons().add(logo);
        UserService.loadProperties();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Billetera Virtual");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
            launch(args);
    }
}