package de.codemakers.net.connection;

import de.codemakers.net.NetworkUtil;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * AbstractServer
 *
 * @param <T> Type of server
 * @param <B> Type of socket
 *
 * @author Paul Hagedorn
 */
public abstract class AbstractServer<T, B, A extends AbstractConnection> {

    protected final ConcurrentLinkedQueue<A> accepted = new ConcurrentLinkedQueue<>();
    protected int port = -1;
    protected boolean started = false;

    public final ConcurrentLinkedQueue<A> getAccepted() {
        return accepted;
    }

    public final boolean disconnectAccepted() {
        if (accepted.isEmpty()) {
            return true;
        }
        accepted.forEach(A::disconnect);
        return true;
    }

    public final int getPort() {
        return port;
    }

    public final boolean hasPort() {
        return NetworkUtil.checkPort(port);
    }

    protected abstract A acceptIntern(B socket);

    public abstract boolean accept(A accepted);

    public abstract boolean receive(final byte[] data, A connectionInfo);

    public final boolean start() {
        return start(-1, null, null);
    }

    public final boolean start(int port) {
        return start(port, null, null);
    }

    public final boolean start(int port, Consumer<T> success) {
        return start(port, success, null);
    }

    public abstract boolean start(int port, Consumer<T> success, Consumer<Throwable> failure);

    public final boolean stop() {
        return stop(null, null);
    }

    public final boolean stop(Consumer<T> success) {
        return stop(success, null);
    }

    public abstract boolean stop(Consumer<T> success, Consumer<Throwable> failure);

    public abstract boolean isStarted();

    @Override
    public String toString() {
        final String simpleName = getClass().getSimpleName();
        return String.format("%s: %d, started: %b", simpleName.isEmpty() ? getClass().getSuperclass().getSimpleName() : simpleName, port, started);
    }

}
