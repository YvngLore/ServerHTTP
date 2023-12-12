package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

                do{
                    String line = in.readLine();
                    System.out.println(line);
                    if(line == null || line.isEmpty())
                        break;
                }while(true);

                sendBinaryFile(socket, in);

                out.flush();
                socket.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendBinaryFile(Socket socket, BufferedReader in) {
        try {
            String inn = in.readLine();
            String file_path = "." + inn.split(" ")[1];
            File fileLettura = new File(file_path);
            String extension = file_path.split("\\.")[file_path.length()];

            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeBytes("HTTP/1.1 200 OK" + "\n");
            output.writeBytes("Content-Lenght: " + fileLettura.length() + "\n");
            output.writeBytes("Server: Java HTTP Server from YvngLore: 1.2" + "\n");
            output.writeBytes("Date: " + new Date() + "\n");
            switch (extension) {
                case "html":
                    output.writeBytes("Content-Type: text/html; charset=utf-8\n");
                    break;

                case "css":
                    output.writeBytes("Content-Type: text/css; charset=utf-8\n");
                    break;
                
                case "js":
                    output.writeBytes("Content-Type: text/javascript; charset=utf-8\n");
                    break;

                case "jpg":
                case "jpeg":
                    output.writeBytes("Content-Type: image/jpeg; charset=utf-8\n");
                    break;

                case "png":
                    output.writeBytes("Content-Type: image/png; charset=utf-8\n");
                    break;

                default:
                    output.writeBytes("HTTP/1.1 404 NOT FOUND\n");
                    break;
            }
            output.writeBytes("\n");

            File file = new File(file_path);
            InputStream input = new FileInputStream(file);
            byte[] buff = new byte[8192];
            int n;
            while((n = input.read(buff)) != -1){
                output.write(buff, 0, n);
            }
            input.close();
        } catch (Exception e) {
            //output.writeBytes("HTTP/1.1 404 NOT FOUND\n");
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
            out.println("\n");

            while(reader.hasNextLine()){
                String data = reader.nextLine();
                out.println(data);
            }

            reader.close();
            out.close();
        } catch (FileNotFoundException ex) {
            out.println("HTTP/1.1 404 NOT FOUND");
        } catch(IOException ex){
            out.println(ex.getMessage());
        }
    }
}