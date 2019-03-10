package com.kc.study.socket;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket=new Socket("127.0.0.1",8000);
            new Thread(()->{
                System.out.println("客户端启动成功");
                while (true){
                    String msg="hello world";
                    System.out.println("客户端发送数据："+msg);
                    try {
                        socket.getOutputStream().write(msg.getBytes());
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        System.out.println("写数据错误");
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
