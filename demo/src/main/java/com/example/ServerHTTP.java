package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerHTTP {
    public static void main(String[] args) {
        System.out.println("Server in avvio");
        try {
            ServerSocket server = new ServerSocket(8080);
            while(true){
                Socket socket = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                String line = in.readLine();
                //System.out.println(line);
                String file_path = line.split(" ")[1];
                //System.out.println(file_path);
                do{
                    line = in.readLine();
                    System.out.println(line);
                }while(!line.isEmpty());
                

                System.out.println("ciccio");
                String [] parts = file_path.split("\\.");
                String extension = parts[parts.length - 1];
                System.out.println(extension);
                if(file_path.endsWith("/")){
                    file_path = "index.html";
                }
                System.out.println(file_path);
                File file = new File("/htdocs" + file_path);

                if(file.exists()){
                    sendBinaryFile(file, extension, file_path, out);
                }else{
                    if(!file_path.endsWith("/")){
                        out.writeBytes("HTTP/1.1 301 Move Permanently\n");
                        out.writeBytes("Location: /htdocs/\n");
                        out.writeBytes("\n");
                    }else{
                        out.writeBytes("HTTP/1.1 404 NOT FOUND\n");
                        out.writeBytes("Content-Lenght: " + file.length() + "\n");
                        out.writeBytes("Content-Type: text/plain; charset=utf-8\n");
                        out.writeBytes("\n");
                        out.writeBytes("File not found\n");
                    }
                }

                out.flush();
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("-------" + e.getMessage());
        }
    }

    private static void sendBinaryFile(File file, String extension, String file_path, DataOutputStream output) throws IOException{
        try {
            output.writeBytes("HTTP/1.1 200 OK" + "\n");
            output.writeBytes("Content-Lenght: " + file.length() + "\n");
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

            File fileOut = new File("htdocs" + file_path);
            InputStream input = new FileInputStream(fileOut);
            byte[] buff = new byte[8192];
            int n;
            while((n = input.read(buff)) != -1){
                output.write(buff, 0, n);
            }
            input.close();
        } catch (Exception e) {
            output.writeBytes("HTTP/1.1 404 NOT FOUND\n");
        }
    }   

    /*private static void sendFile(BufferedReader in, PrintWriter out){
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
    }*/
}