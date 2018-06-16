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


@WebServlet("/steamcmd/download/")
public class LoadServerServlet extends HttpServlet {

    private GetConfig config = GetConfig.getInstance();
    private LocalLog LOG = new LocalLog();
    private AnswerService answer = new AnswerService();

    public void doPost(HttpServletRequest request, HttpServletResponse response) { // 443030 Conan

        JSONObject json = new JSONHandler().getJSONFromRequest(request);
        String appID = String.valueOf(json.get("app_id"));
        String folder = String.valueOf(json.get("folder"));
        String login = String.valueOf(json.get("login"));
        String password = String.valueOf(json.get("password"));
        if (!appID.isEmpty() && !folder.isEmpty()) {
            try {
                Process process;
                if (!login.isEmpty() && !password.isEmpty()) {
                    process = new ProcessBuilder(config.getSTEAM_CMD_PATH(), "+login", "anonymous", "+force_install_dir",
                            config.getFORCE_INSTALL_DIR() + folder, "+app_update", appID, "+update", "+quit").start();
                } else {
                    process = new ProcessBuilder(config.getFORCE_INSTALL_DIR(), "+login", login, password, "+force_install_dir",
                            config.getFORCE_INSTALL_DIR() + folder, "+app_update", appID, "+update", "+quit").start();
                }
                LOG.info("process info: " + process.info());
                LOG.info("Process was startup, PID: " + process.pid());
                LOG.console(new BufferedReader(new InputStreamReader(process.getInputStream(), "Cp866")));

                int status = process.waitFor();
                LOG.info("status process: " + status);
                answer.sendToClient(response, 200);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                answer.sendToClient(response, 500);
            }
        }
    }
}