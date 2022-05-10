package com.lespayne.base;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MessageClient {
    public static final String mClientIp = "172.12.152.65";
    public static final int mClientPort = 8080;
    private boolean isReceive = false;
    private boolean isConnect = false;
    private ReceiveThread receiveThread;
    private Socket socket;

    public void connect() {
        try {
            socket = new Socket(mClientIp, mClientPort);
            isConnect = true;
            isReceive = true;
            receiveThread = new ReceiveThread(socket);
            receiveThread.start();
            System.out.println("----connected success----");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //接收线程
    private class ReceiveThread extends Thread {
        private InputStream inStream = null;
        private byte[] buffer;
        private String str = null;

        ReceiveThread(Socket socket) {
            try {
                inStream = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (isReceive) {
                buffer = new byte[512];
                try {
                    inStream.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                str = new String(buffer, StandardCharsets.UTF_8).trim();
                Log.e("TAG", "run: " + str);
            }
        }
    }

    private OutputStream outStream;
    private String strMessage;
    Runnable sendThread = new Runnable() {
        @Override
        public void run() {
            byte[] sendBuffer = null;
            sendBuffer = strMessage.getBytes(StandardCharsets.UTF_8);
            try {
                outStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outStream.write(sendBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
