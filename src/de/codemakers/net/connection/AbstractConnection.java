package de.codemakers.net.connection;

import java.util.function.Consumer;

/**
 * AbstractConnection
 *
 * @param <T> Type of connection
 *
 * @author Paul Hagedorn
 */
public abstract class AbstractConnection<T> {

    protected ConnectionInfo connectionInfo = null;
    protected boolean connected = false;

    public final ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public final boolean hasConnectionInfo() {
        return connectionInfo != null;
    }

    public abstract boolean receive(final byte[] data);

    public final boolean send(final byte[] data) {
        return send(data, null, null);
    }

    public final boolean send(final byte[] data, Consumer<T> success) {
        return send(data, success, null);
    }

    public abstract boolean send(final byte[] data, Consumer<T> success, Consumer<Throwable> failure);

    public final boolean connect() {
        return connect(null, null, null);
    }

    public final boolean connect(ConnectionInfo connectionInfo) {
        return connect(connectionInfo, null, null);
    }

    public final boolean connect(ConnectionInfo connectionInfo, Consumer<T> success) {
        return connect(connectionInfo, success, null);
    }

    public abstract boolean connect(ConnectionInfo connectionInfo, Consumer<T> success, Consumer<Throwable> failure);

    public final boolean disconnect() {
        return disconnect(null, null);
    }

    public final boolean disconnect(Consumer<T> success) {
        return disconnect(success, null);
    }

    public abstract boolean disconnect(Consumer<T> success, Consumer<Throwable> failure);

    public abstract boolean isConnected();

    @Override
    public String toString() {
        final String simpleName = getClass().getSimpleName();
        return String.format("%s: \"%s\", connected: %b", simpleName.isEmpty() ? getClass().getSuperclass().getSimpleName() : simpleName, connectionInfo, connected);
    }

}
