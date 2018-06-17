package servlets.manager;

import org.json.simple.JSONObject;
import services.answer.AnswerService;
import services.config.GetConfig;
import services.file.FileService;
import services.json.JSONHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@WebServlet("/service/game/remove")
public class RemoveServiceServlet extends HttpServlet {

    private GetConfig config = GetConfig.getInstance();
    private AnswerService answer = new AnswerService();

    public void doPost(HttpServletRequest request, HttpServletResponse response){
        JSONObject json = new JSONHandler().getJSONFromRequest(request);
        String appID = String.valueOf(json.get("app_id"));
        String userID = String.valueOf(json.get("user_id"));

        FileService service = new FileService();
//        boolean isRemove =
        service.removeFolder(new File(config.getPATH_FOLDER_USERS() + "user_" + userID + "/server_" + appID));
        if (true){
            answer.sendToClient(response, 200);
        }else{
            answer.sendToClient(response, 500);
        }
    }
}
