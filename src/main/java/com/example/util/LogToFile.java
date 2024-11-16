package com.example.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.service.UserService;

public class LogToFile {
    public static void logToFile(String level, String message){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(UserService.getLogPath(), true))){
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(String.format("%s [%s]: %s%n", timestamp, level, message));
        } catch (IOException e){
            System.out.println("Error al escribir en el log" + e.getMessage());
        }
    }
}