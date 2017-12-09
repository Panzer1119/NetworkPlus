package de.codemakers.net.connection.tcp;

import de.codemakers.net.NetworkUtil;
import de.codemakers.net.connection.AbstractServer;
import de.codemakers.net.exceptions.ServerException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

/**
 * TCPServer
 *
 * @author Paul Hagedorn
 */
public abstract class TCPServer extends AbstractServer<TCPServer, Socket, TCPConnection> {

    private final TCPServer ME = this;
    private ServerSocket serverSocket = null;
    private Thread thread_acceptor = null;

    public TCPServer() {
        this((ServerSocket) null);
    }

    public TCPServer(ServerSocket socket) {
        setServerSocket(socket);
    }

    public TCPServer(int port) {
        this.port = port;
    }

    @Override
    public TCPConnection acceptIntern(Socket socket) {
        final TCPConnection connection = new TCPConnection(socket) {
            @Override
            public final boolean receive(byte[] data) {
                return ME.receive(data, connectionInfo);
            }
        };
        connection.startListening();
        return connection;
    }

    @Override
    public final boolean start(int port, Consumer<TCPServer> success, Consumer<Throwable> failure) {
        if (NetworkUtil.checkPort(port)) {
            this.port = port;
        } else if (isStarted()) {
            stop();
        }
        try {
            serverSocket = new ServerSocket(this.port);
            started = true;
            thread_acceptor = new Thread(() -> {
                try {
                    while (started) {
                        final Socket socket = serverSocket.accept();
                        final TCPConnection connection = acceptIntern(socket);
                        if (connection != null) {
                            if (accept(connection)) {
                                accepted.add(connection);
                            } else {
                                connection.stopListening();
                                connection.disconnect();
                            }
                        }
                    }
                } catch (Exception ex) {
                    started = false;
                    if (!(ex instanceof SocketException)) {
                        ex.printStackTrace();
                    }
                }
            });
            thread_acceptor.start();
        } catch (Exception ex) {
            NetworkUtil.accept(failure, new ServerException(ex.getLocalizedMessage(), this.port, ex));
        }
        if (isStarted()) {
            NetworkUtil.accept(success, this);
            return true;
        } else {
            NetworkUtil.accept(failure, new ServerException("The server could not be started", this.port));
            return false;
        }
    }

    @Override
    public final boolean stop(Consumer<TCPServer> success, Consumer<Throwable> failure) {
        if (serverSocket == null) {
            NetworkUtil.accept(failure, new ServerException("The server is already stopped", port));
            return false;
        }
        try {
            if (!disconnectAccepted()) {
                NetworkUtil.accept(failure, new ServerException("The server could not disconnect every connected socket", port));
            }
            if (thread_acceptor != null) {
                thread_acceptor.interrupt();
                thread_acceptor = null;
            }
            serverSocket.close();
            if (serverSocket.isClosed()) {
                serverSocket = null;
            }
            started = false;
        } catch (Exception ex) {
            NetworkUtil.accept(failure, new ServerException(ex.getLocalizedMessage(), port, ex));
        }
        if (!isStarted() && (serverSocket == null || serverSocket.isClosed())) {
            NetworkUtil.accept(success, this);
            return true;
        } else {
            NetworkUtil.accept(failure, new ServerException("The server could not be stopped", port));
            return false;
        }
    }

    @Override
    public final boolean isStarted() {
        return started;
    }

    protected final TCPServer setServerSocket(ServerSocket serverSocket) {
        if (isStarted()) {
            return this;
        }
        this.serverSocket = serverSocket;
        if (serverSocket != null) {
            this.port = serverSocket.getLocalPort();
            this.started = !serverSocket.isClosed();
        }
        return this;
    }

}
