package de.codemakers.net.connection;

import de.codemakers.net.NetworkUtil;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * ConnectionInfo
 *
 * @author Paul Hagedorn
 */
public class ConnectionInfo {

    private InetAddress inetAddress = null;
    private int port = -1;

    public ConnectionInfo(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public final InetAddress getInetAddress() {
        return inetAddress;
    }

    public final ConnectionInfo setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
        return this;
    }

    public final int getPort() {
        return port;
    }

    public final ConnectionInfo setPort(int port) {
        this.port = port;
        return this;
    }

    public final boolean hasInetAddress() {
        return inetAddress != null;
    }

    public final boolean hasPort() {
        return NetworkUtil.checkPort(port);
    }

    public final boolean isUsable() {
        return hasInetAddress() && hasPort();
    }

    public final boolean isIPv4() {
        return hasInetAddress() ? (inetAddress instanceof Inet4Address) : false;
    }

    public final boolean isIPv6() {
        return hasInetAddress() ? (inetAddress instanceof Inet6Address) : false;
    }

    @Override
    public final String toString() {
        return (hasInetAddress() ? (inetAddress + ":") : "") + (hasPort() ? port : "");
    }

}
