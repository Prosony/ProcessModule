package servlets.manager;

import debug.LocalLog;
import org.json.simple.JSONObject;
import services.answer.AnswerService;
import services.config.GetConfig;
import services.json.JSONHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@WebServlet("/service/game/remove")
public class RemoveServiceServlet extends HttpServlet {

    private GetConfig config = GetConfig.getInstance();
    private AnswerService answer = new AnswerService();
    private LocalLog LOG = new LocalLog();

    public void doPost(HttpServletRequest request, HttpServletResponse response){
        JSONObject json = new JSONHandler().getJSONFromRequest(request);
        String appID = String.valueOf(json.get("app_id"));
        String userID = String.valueOf(json.get("user_id"));

        try {
            String serviceDisplayName = "gecp_"+appID;
            Process checkService = Runtime.getRuntime().exec("SC query "+serviceDisplayName);
            int isAlreadyInstall = checkService.waitFor();
            if (isAlreadyInstall == 0) {
                StringBuilder builderQuery = new StringBuilder();
                builderQuery.append("cmd /C ").append(config.getNSSM_EXEC_PATH()).append(" remove ").append(serviceDisplayName).append(" confirm");
                LOG.info("[RemoveServiceServlet] query was built, builderQuery: "+builderQuery.toString());
                Process process = Runtime.getRuntime().exec(builderQuery.toString());
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"Cp866"));
                String s;
                while ((s = reader.readLine()) != null) {
                    LOG.info(s);
                }
                int status = process.waitFor();
                LOG.info("[RemoveServiceServlet] status process: "+status);
                if (status == 0){
//                    FileService service = new FileService();
//                    service.removeFolder(new File(config.getPATH_FOLDER_USERS() + "user_" + userID + "/server_" + appID));
                    answer.sendToClient(response,200);
                }
            }else{
                LOG.error("[RemoveServiceServlet] service does not exist!");
                answer.sendToClient(response, 500);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
