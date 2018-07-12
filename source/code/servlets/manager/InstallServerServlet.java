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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        JSONObject json = new JSONHandler().getJSONFromRequest(request);
        String appID = String.valueOf(json.get("app_id"));
        String userID = String.valueOf(json.get("user_id"));
        String directory = String.valueOf(json.get("directory_steamcmd"));//C:\Users\proso\Desktop\steamcmd\steamapps\common\Conan\
        String pathExec = String.valueOf(json.get("server_exec_path"));//ConanSandboxServer.exe

        Process process = null;
        StringBuilder copyTo = new StringBuilder();
        copyTo.append(config.getPATH_FOLDER_USERS()).append("user_").append(userID).append("\\server_").append(appID).append("\\");
        FileService service = new FileService();
        boolean isInstall = service.copyFolders(directory, copyTo.toString());

        if (isInstall){ //create win.service
            String serviceDisplayName = "gecp_"+appID;
            try { //TODO change directory. Create win.service from user directory
                Process checkService = Runtime.getRuntime().exec("SC query "+serviceDisplayName);
                int isAlreadyInstall = checkService.waitFor();
                if (isAlreadyInstall != 0){
                    StringBuilder builderQuery = new StringBuilder();
                    builderQuery.append("cmd /C ").append(config.getNSSM_EXEC_PATH()).append(" install ").append(serviceDisplayName).append(" ").append(copyTo.toString()).append(pathExec);
                    LOG.info("[InstallServerServlet] query was built, builderQuery: "+builderQuery.toString());
                    process = Runtime.getRuntime().exec(builderQuery.toString());
                    LOG.info("process info: "+process.info());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"Cp866"));
                    LOG.info("Process was startup, PID: "+process.pid());
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
                    LOG.info("service already install, DisplayName: "+serviceDisplayName);
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
