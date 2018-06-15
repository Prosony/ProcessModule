package servlets.control;

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

@WebServlet("/gameserver/control/start")
public class StartServerServlet extends HttpServlet {

    private GetConfig config = GetConfig.getInstance();
    private LocalLog LOG = LocalLog.getInstance();
    private AnswerService answer = new AnswerService();


    public void doPost(HttpServletRequest request, HttpServletResponse response){
        JSONObject json = new JSONHandler().getJSONFromRequest(request);
        String appID = String.valueOf(json.get("app_id"));
        String serverName = String.valueOf(json.get("server_name"));
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("SC create "+serverName+" binPath=\""+config.getPATH_FOLDER_SERVERS()
                    +"conan\\ConanSandboxServer.exe\" DisplayName=\"ConanServer\" type=\"own\" start=\"auto\""); //create windows service
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
