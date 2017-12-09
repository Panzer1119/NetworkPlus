package de.codemakers.net.test;

import de.codemakers.net.NetworkUtil;
import de.codemakers.net.connection.ConnectionInfo;
import de.codemakers.net.connection.tcp.TCPConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Test
 *
 * @author Paul Hagedorn
 */
public class Test {

    public static final void main(String[] args) throws Exception {
        final ServerSocket serverSocket = new ServerSocket(NetworkUtil.PORT_DEFAULT);
        System.out.println("YAY Server started!");
        final TCPConnection connection = new TCPConnection();
        connection.addDataReceiveListener((data, connection_) -> {
            try {
                System.out.println(String.format("[CLIENT] Received data from \"%s\": \"%s\"", connection_.getConnectionInfo(), new String(data)));
                Thread.sleep(1000);
                connection_.send("Selber Hi!".getBytes());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        System.out.println(connection);
        final Thread thread_server = new Thread(() -> {
            try {
                while (true) {
                    final Socket socket = serverSocket.accept();
                    System.out.println("YAY a Client connected to me: " + socket);
                    Thread.sleep(1000);
                    final TCPConnection connection_2 = new TCPConnection(socket);
                    connection_2.addDataReceiveListener((data, connection_) -> System.out.println(String.format("[SERVER] Received data from \"%s\": \"%s\"", connection_.getConnectionInfo(), new String(data))));
                    System.out.println("NEW TCPCONNECTION: " + connection_2);
                    connection_2.startListening();
                    connection_2.send("HALLO!!!".getBytes());
                    Thread.sleep(2000);
                    connection_2.stopListening();
                    connection_2.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread_server.start();
        new Thread(() -> {
            try {
                Thread.sleep(7000);
                thread_server.interrupt();
                serverSocket.close();
                System.out.println("YAY Server stopped!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
        final ConnectionInfo connectionInfo = new ConnectionInfo(InetAddress.getByName("localhost"), NetworkUtil.PORT_DEFAULT);
        connection.connect(connectionInfo, (connection_) -> {
            try {
                System.out.println("YAY Client connected!");
                System.out.println(connection_);
                connection_.startListening();
                Thread.sleep(4000);
                connection_.stopListening();
                connection_.disconnect((connection__) -> System.out.println("YAY Client disconnected!"), System.err::println);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, System.err::println);
    }

}
