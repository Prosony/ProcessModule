package services.config;

import debug.LocalLog;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GetConfig {

    private static GetConfig instance = new GetConfig();
    public static GetConfig getInstance(){
        return instance;
    }


    private String STEAM_CMD_PATH;
    private String FORCE_INSTALL_DIR;
    private boolean DEBUG;
    private  String PATH_FOLDER_USERS;

    public GetConfig(){
        try {
            Properties properties = new Properties();
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String globalConfigPath = rootPath + "global-config.properties";
            properties.load(new FileInputStream(globalConfigPath));
            STEAM_CMD_PATH = String.valueOf(properties.getProperty("STEAM_CMD_PATH"));
            PATH_FOLDER_USERS = String.valueOf(properties.getProperty("PATH_FOLDER_USERS"));
            FORCE_INSTALL_DIR = String.valueOf(properties.getProperty("FORCE_INSTALL_DIR"));
            DEBUG = Boolean.valueOf(properties.getProperty("DEBUG"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSTEAM_CMD_PATH() {
        return STEAM_CMD_PATH;
    }
    public String getFORCE_INSTALL_DIR() {
        return FORCE_INSTALL_DIR;
    }
    public boolean isDEBUG() {
        return DEBUG;
    }

    public String getPATH_FOLDER_USERS() {
        return PATH_FOLDER_USERS;
    }
}

