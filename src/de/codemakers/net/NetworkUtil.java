package de.codemakers.net;

import de.codemakers.net.connection.ConnectionInfo;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * NetworkUtil
 *
 * @author Paul Hagedorn
 */
public class NetworkUtil {

    /**
     * Lowest possible port number
     */
    public static final int PORT_MIN = 0x400;
    /**
     * Default port number
     */
    public static final int PORT_DEFAULT = 1234;
    /**
     * Highest possible port number
     */
    public static final int PORT_MAX = 0xFFFF;

    /**
     * Checks if the given port is between the lowest and highest possible Port
     * number
     *
     * @param port Port to get checked
     * @return <tt>true</tt> if the port is a possible port number
     */
    public static final boolean checkPort(int port) {
        return ((port >= PORT_MIN) && (port <= PORT_MAX));
    }

    public static final ConnectionInfo socketToConnectionInfo(Socket socket) {
        if (socket == null) {
            return null;
        }
        return new ConnectionInfo(socket.getInetAddress(), socket.getPort());
    }

    public static final <T> void accept(Consumer<T> consumer, T object) {
        if (consumer == null) {
            return;
        }
        new Thread(() -> {
            try {
                consumer.accept(object);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

}
