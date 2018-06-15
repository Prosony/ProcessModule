package services.db;

import model.ServerGameModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectQueryDB {

    private DataBaseService service = DataBaseService.getInstance();

    public ServerGameModel getServerGame(String appID){
        Connection connection = service.retrieve();

        try {
            PreparedStatement select = connection.prepareStatement("select * from server-games where server-games.app_id = ?");
            select.setString(1,appID);
            ResultSet result = select.executeQuery();
            ServerGameModel model = null;
            while(result.next()){
                model = new ServerGameModel(result.getString("id"), result.getInt("app_id")
                        , result.getString("folder"), result.getString("path_exec"), result.getTimestamp("timestamp"));
            }
            service.putback(connection);
            return model;
        } catch (SQLException e) {
            service.putback(connection);
            e.printStackTrace();
        }
        return null;
    }



}
