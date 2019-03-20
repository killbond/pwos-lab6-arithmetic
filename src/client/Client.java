package client;

import network.TCPConnection;
import network.TCPConnectionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client implements TCPConnectionListener {

    private TCPConnection connection;

    private static final int PORT = 8189;

    public static void main(String[] args) throws IOException {
        new Client();
    }

    public Client() throws IOException {
        try {
            connection = new TCPConnection(this, "127.0.0.1", PORT);
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        connection.sendString(br.readLine());
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready...");
        printMsg("\nEnter expression:");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMsg("\n" + value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection exception: " + e);

    }

    private synchronized void printMsg(String msg) {
        System.out.print(msg);
    }
}
