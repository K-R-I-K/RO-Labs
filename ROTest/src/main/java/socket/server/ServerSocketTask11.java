package socket.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketTask11 {
    private ServerSocket server;
    private Socket sock;

    public void start(int port) throws IOException {
        server = new ServerSocket(port);
        while (true)
        {
            sock = server.accept();
            new MyThread(sock).start();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocketTask11 srv = new ServerSocketTask11();
            srv.start(12345);
        }
        catch(IOException e) {
            System.out.println("Error");
        }
    }
}
