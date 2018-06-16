package servlets.control;

import debug.LocalLog;
import org.json.simple.JSONObject;
import services.answer.AnswerService;
import services.config.GetConfig;
import services.file.FileService;
import services.json.JSONHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@WebServlet("/service/game/install")
public class StartServerServlet extends HttpServlet {

    private GetConfig config = GetConfig.getInstance();
    private LocalLog LOG = new LocalLog();
    private AnswerService answer = new AnswerService();

    public void doPost(HttpServletRequest request, HttpServletResponse response){
        JSONObject json = new JSONHandler().getJSONFromRequest(request);
        String appID = String.valueOf(json.get("app_id"));
        String userID = String.valueOf(json.get("user_id"));
        String directory = String.valueOf(json.get("directory"));
        String pathExec = String.valueOf(json.get("path_exec"));

        Process process = null;
        StringBuilder copyTo = new StringBuilder();
        copyTo.append(config.getPATH_FOLDER_USERS()).append("user_").append(userID).append("/server_").append(appID);
        FileService service = new FileService();

        service.copyFolders(directory, copyTo.toString());
        StringBuilder serviceName = new StringBuilder();
        serviceName.append("gecp_").append(appID);
        /**
         * create win.service
         */
        try { //TODO change directory. Create win.service from user directory
            process = Runtime.getRuntime().exec("SC create "+serviceName.toString()+
                    " binPath= \""+copyTo.toString()+pathExec+"\" DisplayName=\""+serviceName.toString()+"\" type=\"own\" start=\"auto\""); //create windows service
            LOG.info("process info: "+process.info());
            BufferedReader reader = null;
            LOG.info("Process was startup, PID: "+process.pid());
            reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"Cp866"));
            String s;
            while ((s = reader.readLine()) != null) {
                LOG.info(s);
            }
            int status = process.waitFor();
            LOG.info("status process: "+status);
            if (status == 0){
                answer.sendToClient(response,200);
            }else{
                answer.sendToClient(response,500);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            answer.sendToClient(response,500);
        }
    }
}
