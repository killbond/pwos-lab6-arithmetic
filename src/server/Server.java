﻿package server;

import arithmetic.Postfix;
import javafx.application.Application;
import javafx.stage.Stage;
import network.TCPConnection;
import network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server extends Application implements TCPConnectionListener {

    public static void main(String[] args) {
        launch(args);
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Server running...");
        try(ServerSocket serverSocket = new ServerSocket(8189)) {
            while(true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) throws Exception {
        Postfix postfix = new Postfix(value);
        sendToAllConnections("Польская запись: " + postfix.view());
        sendToAllConnections("Результат: " + postfix.calculate());
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnections(String value){
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++) connections.get(i).sendString(value);
    }
}
