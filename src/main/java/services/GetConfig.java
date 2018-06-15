package services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GetConfig {

    private static GetConfig instance = new GetConfig();
    public static GetConfig getInstance() {
        return instance;
    }

    private String steamCMDPath;
    private String pathFolderServers;
    private boolean debug;

    private String USER;
    private String PASSWORD;
    private String URL;

    private GetConfig(){
        try {
            Properties properties = new Properties();
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String globalConfigPath = rootPath + "global-config.properties";
            properties.load(new FileInputStream(globalConfigPath));

            steamCMDPath = String.valueOf(properties.getProperty("steam_cmd_path_exe"));
            pathFolderServers = String.valueOf(properties.getProperty("path_folder_servers"));
            debug = Boolean.valueOf(properties.getProperty("debug"));

            Properties propertiesDB = new Properties();

            String dbConfigPath = rootPath + "db.properties";
            propertiesDB.load(new FileInputStream(dbConfigPath));

            USER = propertiesDB.getProperty("user");
            PASSWORD = propertiesDB.getProperty("password");
            URL = propertiesDB.getProperty("url");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public String getPathFolderServers(){
        return pathFolderServers;
    }
    public String getSteamCMDPath() {
        return steamCMDPath;
    }
    public boolean isDebug() {
        return debug;
    }

    public String getUSER() {
        return USER;
    }
    public String getPASSWORD() {
        return PASSWORD;
    }
    public String getURL() {
        return URL;
    }
}
