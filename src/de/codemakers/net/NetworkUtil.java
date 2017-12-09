package de.codemakers.net;

import de.codemakers.net.connection.AbstractConnection;
import de.codemakers.net.connection.ConnectionInfo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public static final byte[] convertObjectToBytes(Object object) {
        try {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(object);
                return baos.toByteArray();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static final Object convertBytesToObject(byte[] data) {
        try {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(data); ObjectInputStream ois = new ObjectInputStream(bais)) {
                return ois.readObject();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static final boolean sendObject(Object object, AbstractConnection connection) {
        if (connection == null) {
            return false;
        }
        return connection.send(convertObjectToBytes(object));
    }

}
