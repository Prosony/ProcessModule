package services.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GetConfig {

    private static GetConfig instance = new GetConfig();
    public static GetConfig getInstance() {
        return instance;
    }

    private String STEAM_CMD_PATH;
    private String PATH_FOLDER_SERVERS;
    private boolean DEBUG;
    private int PORT;

    private GetConfig(){
        try {
            Properties properties = new Properties();
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String globalConfigPath = rootPath + "global-config.properties";
            properties.load(new FileInputStream(globalConfigPath));

            STEAM_CMD_PATH = String.valueOf(properties.getProperty("STEAM_CMD_PATH"));
            PATH_FOLDER_SERVERS = String.valueOf(properties.getProperty("PATH_FOLDER_SERVERS"));
            DEBUG = Boolean.valueOf(properties.getProperty("DEBUG"));
            PORT = Integer.valueOf(properties.getProperty("PORT"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSTEAM_CMD_PATH() {
        return STEAM_CMD_PATH;
    }
    public String getPATH_FOLDER_SERVERS() {
        return PATH_FOLDER_SERVERS;
    }
    public boolean isDEBUG() {
        return DEBUG;
    }
    public int getPORT() {
        return PORT;
    }
}

