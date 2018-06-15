package servlets.maneger;

import degub.LocalLog;
import memcache.ProcessCache;
import model.ServerGameModel;
import org.json.simple.JSONObject;
import services.GetConfig;
import services.answer.AnswerService;
import services.db.SelectQueryDB;
import services.json.JsonHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

@WebServlet("/steamcmd/download/")
public class LoadServerServlet extends HttpServlet {

    private ProcessCache cache = ProcessCache.getInstance();
    private GetConfig config = GetConfig.getInstance();
    private LocalLog LOG = LocalLog.getInstance();
    private AnswerService answer = new AnswerService();


    public void doPost(HttpServletRequest request, HttpServletResponse response){ // 443030 Conan

        JSONObject json = new JsonHandler().getJsonFromRequest(request);
        String appID = String.valueOf(json.get("app_id"));
        String folder = String.valueOf(json.get("folder"));
        String login = String.valueOf(json.get("login"));
        String password = String.valueOf(json.get("password"));


        if (!appID.isEmpty() && !folder.isEmpty()){
            SelectQueryDB select = new SelectQueryDB();
            ServerGameModel model = select.getServerGame(appID);
            if (model == null){
                try {
                    Process process;
                    if (!login.isEmpty() && !password.isEmpty()){
                        process = new ProcessBuilder(config.getSteamCMDPath(), "+login", "anonymous", "+force_install_dir", config.getPathFolderServers()+folder,"+app_update",
                                appID, "+update", "+quit").start();
                    }else{
                        process = new ProcessBuilder(config.getSteamCMDPath(), "+login", login, password, "+force_install_dir", config.getPathFolderServers()+folder, "+app_update",
                                appID, "+update", "+quit").start();
                    }

                    cache.addProcess(process);
                    LOG.info("process info: "+process.info());
                    LOG.info("Process was startup, PID: "+process.pid());
                    LOG.console(new BufferedReader(new InputStreamReader(process.getInputStream(),"Cp866")));

                    int status = process.waitFor();
                    LOG.info("status process: "+status);
                    cache.deleteProcess(process.pid());
                    ServerGameModel model = new ServerGameModel(String.valueOf(UUID.randomUUID()), appID, folder, , );
                    answer.sendToClient(response,200);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                    answer.sendToClient(response,500);
                }
            }else{
                answer.sendToClient(response,200);
            }


        }
    }

}
