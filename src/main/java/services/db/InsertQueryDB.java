package services.db;

import degub.LocalLog;
import model.ServerGameModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertQueryDB {

    private DataBaseService service = DataBaseService.getInstance();
    private LocalLog LOG = LocalLog.getInstance();

    public boolean setServerGame(ServerGameModel model){
        Connection connection = service.retrieve();

        try {
            PreparedStatement insert = connection.prepareStatement("insert into server-games(id, app_id, folder, path_exec, timestamp) " +
                    "values (?,?,?,?)");
            insert.setString(1, model.getId());
            insert.setInt(2, model.getAppID());
            insert.setString(3, model.getFolder());
            insert.setString(4, model.getPathExec());
            insert.setTimestamp(5, model.getTimestamp());
            insert.executeUpdate();
            LOG.success("[InsertQueryDB][setServerGame] add new record in table server-game");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        service.putback(connection);
        return false;
    }
}
