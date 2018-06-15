package services.db;

import degub.LocalLog;
import services.GetConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class DataBaseService {

    private final static DataBaseService instance = new DataBaseService();
    public static DataBaseService getInstance(){ //TODO create demons thread that's follow for connections and print available
        return instance;
    }

    private DataBaseService(){}

    private Vector<Connection> availableConnections = new Vector<>();
    private Vector<Connection> usedConnections = new Vector<>();

    private LocalLog LOG = LocalLog.getInstance();
    private GetConfig config = GetConfig.getInstance();

    public synchronized Connection retrieve() {
        Connection newConnection = null;
        if (availableConnections.size() == 0) {
            newConnection = getConnection();
        } else {
            newConnection = availableConnections.lastElement();
            availableConnections.removeElement(newConnection);
        }
        usedConnections.addElement(newConnection);
        return newConnection;
    }

    public synchronized void putback(Connection connection) throws NullPointerException {
        if (connection != null) {
            if (usedConnections.removeElement(connection)) {
                availableConnections.addElement(connection);
            } else {
                throw new NullPointerException("Connection not in the usedConnections");
            }
        }
    }
    private Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(config.getURL(),config.getUSER(),config.getPASSWORD());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    private void connectionClose(){
//        try {
//            connection.close();
//            if (connection.isClosed()) {
//                System.out.println("Connection is closed");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
    /**
     * @return the number of available connections
     */
    public void getAvailableConnections() {
        LOG.info("[DataBaseService][getAvailableConnections] availableConnections: " + availableConnections.size() + ", usedConnections: " + usedConnections.size());
    }
}