package de.codemakers.net.exceptions;

import de.codemakers.net.connection.ConnectionInfo;

/**
 * ConnectionException
 *
 * @author Paul Hagedorn
 */
public class ConnectionException extends RuntimeException {

    private final ConnectionInfo connectionInfo;

    public ConnectionException(ConnectionInfo connectionInfo) {
        this(null, connectionInfo);
    }

    public ConnectionException(String message, ConnectionInfo connectionInfo) {
        this(message, connectionInfo, null);
    }

    public ConnectionException(ConnectionInfo connectionInfo, Throwable cause) {
        this(null, connectionInfo, cause);
    }

    public ConnectionException(String message, ConnectionInfo connectionInfo, Throwable cause) {
        super(message, cause);
        this.connectionInfo = connectionInfo;
    }

    public final ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

}
