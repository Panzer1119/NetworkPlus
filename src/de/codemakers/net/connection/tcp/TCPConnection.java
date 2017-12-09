package de.codemakers.net.connection.tcp;

import de.codemakers.net.NetworkUtil;
import de.codemakers.net.connection.AbstractConnection;
import de.codemakers.net.connection.ConnectionInfo;
import de.codemakers.net.exceptions.ConnectionException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * TCPConnection
 *
 * @author Paul Hagedorn
 */
public abstract class TCPConnection extends AbstractConnection<TCPConnection> {

    public static final int BUFFER_SIZE = 1024;

    private Socket socket = null;
    private boolean listening = false;
    private Thread thread_listener = null;
    private int buffer_size = BUFFER_SIZE;

    public TCPConnection() {
        this((Socket) null);
    }

    public TCPConnection(Socket socket) {
        setSocket(socket);
    }

    public TCPConnection(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    @Override
    public final boolean send(final byte[] data, Consumer<TCPConnection> success, Consumer<Throwable> failure) {
        try {
            final OutputStream outputStream = socket.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            NetworkUtil.accept(success, this);
            return true;
        } catch (Exception ex) {
            NetworkUtil.accept(failure, ex);
            if (ex instanceof SocketException) {
                connected = false;
            }
            return false;
        }
    }

    @Override
    public final boolean connect(ConnectionInfo connectionInfo, Consumer<TCPConnection> success, Consumer<Throwable> failure) {
        if (connectionInfo != null) {
            this.connectionInfo = connectionInfo;
        } else if (isConnected()) {
            disconnect();
        }
        if (!this.connectionInfo.isUsable()) {
            NetworkUtil.accept(failure, new ConnectionException("The connection information is not usable", this.connectionInfo));
            return false;
        }
        try {
            socket = new Socket(this.connectionInfo.getInetAddress(), this.connectionInfo.getPort());
            connected = true;
        } catch (Exception ex) {
            NetworkUtil.accept(failure, new ConnectionException(ex.getLocalizedMessage(), this.connectionInfo, ex));
        }
        if (isConnected()) {
            NetworkUtil.accept(success, this);
            return true;
        } else {
            NetworkUtil.accept(failure, new ConnectionException("The connection could not be established", this.connectionInfo));
            return false;
        }
    }

    @Override
    public final boolean disconnect(Consumer<TCPConnection> success, Consumer<Throwable> failure) {
        if (socket == null) {
            NetworkUtil.accept(failure, new ConnectionException("The connection is already closed", connectionInfo));
            return false;
        }
        try {
            if (isListening() && !stopListening()) {
                NetworkUtil.accept(failure, new ConnectionException("The listener thread could not be stopped", connectionInfo));
                return false;
            }
            socket.close();
            if (socket.isClosed() && !socket.isConnected()) {
                socket = null;
            }
            connected = false;
        } catch (Exception ex) {
            NetworkUtil.accept(failure, new ConnectionException(ex.getLocalizedMessage(), connectionInfo, ex));
        }
        if (!isConnected() && (socket == null || socket.isClosed())) {
            NetworkUtil.accept(success, this);
            return true;
        } else {
            NetworkUtil.accept(failure, new ConnectionException("The connection could not be closed", connectionInfo));
            return false;
        }
    }

    @Override
    public final boolean isConnected() {
        return socket != null && connected;
    }

    protected final TCPConnection setSocket(Socket socket) {
        if (isConnected()) {
            return this;
        }
        this.socket = socket;
        this.connectionInfo = NetworkUtil.socketToConnectionInfo(socket);
        if (socket != null) {
            this.connected = !socket.isClosed() && socket.isConnected();
        }
        return this;
    }

    public final int getBufferSize() {
        return buffer_size;
    }

    public final TCPConnection setBufferSize(int buffer_size) {
        this.buffer_size = buffer_size;
        return this;
    }

    public final boolean isListening() {
        return listening;
    }

    public final boolean startListening() {
        if (isListening() || thread_listener != null || !isConnected()) {
            return false;
        }
        try {
            listening = true;
            thread_listener = new Thread(() -> {
                try {
                    final InputStream inputStream = socket.getInputStream();
                    final byte[] data_empty = new byte[buffer_size];
                    while (listening) {
                        final byte[] data = new byte[data_empty.length];
                        inputStream.read(data);
                        if (Arrays.equals(data, data_empty)) {
                            continue; //FIXME Wenn das hier Daten vernichtet, waere das schlecht
                        }
                        receive(data);
                    }
                } catch (Exception ex) {
                    listening = false;
                    if (ex instanceof SocketException) {
                        connected = false;
                    } else {
                        ex.printStackTrace();
                    }
                }
            });
            thread_listener.start();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            listening = false;
            return false;
        }
    }

    public final boolean stopListening() {
        if (!isListening() || thread_listener == null) {
            return false;
        }
        try {
            thread_listener.interrupt();
            thread_listener = null;
            listening = false;
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
