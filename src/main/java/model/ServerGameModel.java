package model;

import java.sql.Timestamp;

public class ServerGameModel {
    private String id;
    private int appID;
    private String folder;
    private String pathExec;
    private Timestamp timestamp;

    public ServerGameModel(String id, int appID, String path, String pathExec, Timestamp timestamp){
        this.id = id;
        this.appID = appID;
        this.folder = path;
        this.pathExec = pathExec;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public int getAppID() {
        return appID;
    }
    public void setAppID(int appID) {
        this.appID = appID;
    }



    public String getFolder() {
        return folder;
    }
    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getPathExec() {
        return pathExec;
    }
    public void setPathExec(String pathExec) {
        this.pathExec = pathExec;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }



}
