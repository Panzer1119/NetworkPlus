package de.codemakers.net.connection;

import de.codemakers.net.listeners.DataReceiveListener;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * AbstractConnection
 *
 * @author Paul Hagedorn
 */
public abstract class AbstractConnection<T> {

    private final ConcurrentLinkedQueue<DataReceiveListener> dataReceiveListeners = new ConcurrentLinkedQueue<>();
    protected ConnectionInfo connectionInfo = null;
    protected boolean connected = false;

    public final boolean addDataReceiveListener(DataReceiveListener dataReceiveListener) {
        return dataReceiveListeners.add(dataReceiveListener);
    }

    public final boolean removeDataReceiveListener(DataReceiveListener dataReceiveListener) {
        return dataReceiveListeners.remove(dataReceiveListener);
    }

    public final T clearDataReceiveListeners() {
        dataReceiveListeners.clear();
        return (T) this;
    }

    protected final boolean receive(final byte[] data, final ConnectionInfo connectionInfo) {
        if (dataReceiveListeners.isEmpty()) {
            return false;
        }
        dataReceiveListeners.forEach((dataReceiveListener) -> dataReceiveListener.receive(data, this));
        return true;
    }

    public final ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public final boolean hasConnectionInfo() {
        return connectionInfo != null;
    }

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
        return AbstractConnection.this.disconnect(null, null);
    }

    public final boolean disconnect(Consumer<T> success) {
        return AbstractConnection.this.disconnect(success, null);
    }

    public abstract boolean disconnect(Consumer<T> success, Consumer<Throwable> failure);

    public abstract boolean isConnected();

    @Override
    public String toString() {
        return String.format("AbstractConnection: \"%s\", connected: %b, listeners: %s", connectionInfo, connected, dataReceiveListeners.toString());
    }

}
