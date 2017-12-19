package de.codemakers.net;

/**
 * DatabaseInfo
 *
 * @author Paul Hagedorn
 */
public class DatabaseInfo {

    private String hostname;
    private String database;
    private String username;
    private byte[] password;

    public DatabaseInfo(String hostname, String database, String username, String password) {
        this(hostname, database, username, password == null ? null : password.getBytes());
    }

    public DatabaseInfo(String hostname, String database, String username, byte[] password) {
        this.hostname = hostname;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public final String getHostname() {
        return hostname;
    }

    public final DatabaseInfo setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public final String getDatabase() {
        return database;
    }

    public final DatabaseInfo setDatabase(String database) {
        this.database = database;
        return this;
    }

    public final String getUsername() {
        return username;
    }

    public final DatabaseInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    public final byte[] getPassword() {
        return password;
    }

    public final DatabaseInfo setPassword(byte[] password) {
        this.password = password;
        return this;
    }

    public final DatabaseInfo setPassword(String password) {
        if (password == null) {
            this.password = null;
            return this;
        }
        return setPassword(password.getBytes());
    }

}
