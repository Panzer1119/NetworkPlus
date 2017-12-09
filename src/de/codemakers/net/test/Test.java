package de.codemakers.net.test;

import de.codemakers.net.NetworkUtil;
import de.codemakers.net.connection.ConnectionInfo;
import de.codemakers.net.connection.tcp.TCPConnection;
import de.codemakers.net.connection.tcp.TCPServer;
import java.net.InetAddress;

/**
 * Test
 *
 * @author Paul Hagedorn
 */
public class Test {

    public static final void main(String[] args) throws Exception {
        final TCPServer server = new TCPServer() {
            @Override
            public final boolean accept(TCPConnection accepted) {
                accepted.send("Hi!".getBytes());
                return true;
            }

            @Override
            public final boolean receive(byte[] data, TCPConnection connection) {
                System.out.println(String.format("[SERVER] Received data from \"%s\": %s", connection.getConnectionInfo(), new String(data)));
                return true;
            }
        };
        server.start(NetworkUtil.PORT_DEFAULT, (server_) -> {
            try {
                System.out.println("[SERVER] YAY Server started!");
                Thread.sleep(7000);
                server_.stop((server__) -> System.out.println("[SERVER] YAY Server stopped!"), System.err::println);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, System.err::println);
        final TCPConnection connection = new TCPConnection() {
            @Override
            public final boolean receive(byte[] data) {
                try {
                    System.out.println(String.format("[CLIENT] \"%s\" Received data: %s", getConnectionInfo(), new String(data)));
                    Thread.sleep(1000);
                    send("Selber Hi!".getBytes());
                    return true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
        };
        final ConnectionInfo connectionInfo = new ConnectionInfo(InetAddress.getByName("localhost"), NetworkUtil.PORT_DEFAULT);
        connection.connect(connectionInfo, (connection_) -> {
            try {
                System.out.println("[CLIENT] YAY Client connected!");
                connection_.startListening();
                Thread.sleep(4000);
                connection_.stopListening();
                connection_.disconnect((connection__) -> System.out.println("[CLIENT] YAY Client disconnected!"), System.err::println);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, System.err::println);
    }

}
