package de.codemakers.net.listeners;

import de.codemakers.net.connection.AbstractConnection;

/**
 * DataReceiveListener
 *
 * @author Paul Hagedorn
 */
public interface DataReceiveListener {

    public void receive(byte[] data, AbstractConnection connection);

}
