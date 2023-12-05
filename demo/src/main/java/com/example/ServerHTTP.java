package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class ServerHTTP {
    public static void main(String[] args) {
        System.out.println("Server in avvio");
        try {
            ServerSocket server = new ServerSocket(8080);
            while(true){
                Socket socket = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                //Togliere se si vuole leggere in input la stringa in cui c'è scritto il file richiesto
                /*do{
                    String line = in.readLine();
                    System.out.println(line);
                    if(line == null || line.isEmpty())
                        break;
                }while(true); */

                sendFile(in, out);

                out.flush();
                socket.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendFile(BufferedReader in, PrintWriter out){
        try {
            String input = in.readLine();
            String file_path = "." + input.split(" ")[1];
            File file = new File(file_path);
            Scanner reader = new Scanner(file);

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Lenght: " + file.length());
            out.println("Server: Java HTTP Server from YvngLore: 1.2");
            out.println("Date: " + new Date());
            out.println("Content-Type: text/html; charset=utf-8");
            
            out.println();

            while(reader.hasNextLine()){
                String data = reader.nextLine();
                out.println(data);
            }

            reader.close();
            out.close();
        } catch (FileNotFoundException ex) {
            out.println("HTTP/1.1 404 OK");
        } catch(IOException ex){
            out.println(ex.getMessage());
        }
    }
}