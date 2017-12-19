package de.codemakers.net.sql;

import de.codemakers.logger.Logger;
import de.codemakers.net.Database;
import de.codemakers.net.DatabaseInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * SQLDatabase
 *
 * @author Paul Hagedorn
 */
public class SQLDatabase extends Database {

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            Logger.logErr("Could not load the MySQL Driver", ex);
        }
    }

    private DatabaseInfo info = null;
    private Connection connection = null;

    @Override
    public final boolean connect(DatabaseInfo info) {
        if ((info == null && this.info == null) || isConnected()) {
            return false;
        }
        if (info != null) {
            this.info = info;
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + this.info.getHostname() + "/" + this.info.getDatabase(), this.info.getUsername(), new String(this.info.getPassword()));
            return isConnected();
        } catch (Exception ex) {
            Logger.logErr("Error while opening connection", ex);
            return false;
        }
    }

    @Override
    public final boolean disconnect() {
        if (!isConnected()) {
            return false;
        }
        try {
            connection.close();
            connection = null;
            return !isConnected();
        } catch (Exception ex) {
            Logger.logErr("Error while closing connection", ex);
            return false;
        }
    }

    @Override
    public final boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (Exception ex) {
            Logger.logErr("Error while checking the connection state", ex);
            return false;
        }
    }

    private final boolean isConnectedFast() {
        return connection != null;
    }

    public final DatabaseInfo getInfo() {
        return info;
    }

    public final Connection getConnection() {
        return connection;
    }

    public final Statement createStatement() {
        if (!isConnectedFast()) {
            return null;
        }
        try {
            return connection.createStatement();
        } catch (Exception ex) {
            Logger.logErr("Error while creating statement", ex);
            return null;
        }
    }

    public final boolean execute(String sql) {
        if (sql == null || sql.isEmpty() || !isConnectedFast()) {
            return false;
        }
        try {
            final Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
            return true;
        } catch (Exception ex) {
            Logger.logErr("Error while executing", ex);
            return false;
        }
    }

    public final boolean executeLargeUpdate(String sql) {
        if (sql == null || sql.isEmpty() || !isConnectedFast()) {
            return false;
        }
        try {
            final Statement statement = connection.createStatement();
            statement.executeLargeUpdate(sql);
            statement.close();
            return true;
        } catch (Exception ex) {
            Logger.logErr("Error while executing large update", ex);
            return false;
        }
    }

    public final ResultSet executeQuery(String sql) {
        if (sql == null || sql.isEmpty() || !isConnectedFast()) {
            return null;
        }
        try {
            final Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (Exception ex) {
            Logger.logErr("Error while executing query", ex);
            return null;
        }
    }

    public final boolean executeUpdate(String sql) {
        if (sql == null || sql.isEmpty() || !isConnectedFast()) {
            return false;
        }
        try {
            final Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            return true;
        } catch (Exception ex) {
            Logger.logErr("Error while executing update", ex);
            return false;
        }
    }

}
