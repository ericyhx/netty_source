package com.kc.study.socket;


import java.io.InputStream;
import java.net.Socket;

public class ClientHandler {
    private Socket socket;
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    public void start(){
        System.out.println("新客户端接入");
        new Thread(()->doStart()).start();
    }

    private void doStart() {
        try (InputStream inputStream = socket.getInputStream()) {
            while (true){
                byte[] data = new byte[1024];
                int len;
                while ((len=inputStream.read(data))!=-1){
                    String message = new String(data, 0, len);
                    System.out.println("客户端传来的消息："+message);
                    socket.getOutputStream().write(data);
                }
            }
        }catch (Exception e){
            System.out.println("客户端异常");
        }
    }
}
