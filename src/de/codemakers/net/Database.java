package de.codemakers.net;

/**
 * Database
 *
 * @author Paul Hagedorn
 */
public abstract class Database {
    
    public abstract boolean connect(DatabaseInfo info);
    
    public abstract boolean disconnect();
    
    public abstract boolean isConnected();

}
