package de.codemakers.net.exceptions;

/**
 * ServerException
 *
 * @author Paul Hagedorn
 */
public class ServerException extends RuntimeException {

    private final int port;

    public ServerException(int port) {
        this(null, port);
    }

    public ServerException(String message, int port) {
        this(message, port, null);
    }

    public ServerException(int port, Throwable cause) {
        this(null, port, cause);
    }

    public ServerException(String message, int port, Throwable cause) {
        super(message, cause);
        this.port = port;
    }

    public final int getPort() {
        return port;
    }

}
