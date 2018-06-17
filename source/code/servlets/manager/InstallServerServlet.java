package servlets.manager;

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
import java.io.IOException;
import java.io.InputStreamReader;

@WebServlet("/service/game/install")
public class InstallServerServlet extends HttpServlet {

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
        boolean isInstall =
                service.copyFolders(directory, copyTo.toString());

        if (isInstall){ //create win.service
            StringBuilder serviceName = new StringBuilder();
            serviceName.append("gecp_").append(appID);
            try { //TODO change directory. Create win.service from user directory
                Process checkService = Runtime.getRuntime().exec("SC query "+serviceName.toString());
                int isAlreadyInstall = checkService.waitFor();
                if (isAlreadyInstall != 0){
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
                }else{
                    LOG.info("service already install, DisplayName: "+serviceName.toString());
                    answer.sendToClient(response,200);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                answer.sendToClient(response,500);
            }
        }else{
            LOG.error("something wrong with install server");
            answer.sendToClient(response,500);
        }
    }
}
