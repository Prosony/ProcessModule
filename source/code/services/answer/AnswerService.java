package services.answer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AnswerService {

    public void sendToClient(HttpServletResponse response, int code) {
        try {

            response.setCharacterEncoding("UTF-8");
            switch (code){
                case 200:
                    response.sendError(HttpServletResponse.SC_OK);
                    break;
                case 204:
                    response.sendError(HttpServletResponse.SC_NO_CONTENT);
                    break;
                case 400:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    break;
                case 401:
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    break;
                case 500:
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(HttpServletResponse response, String list){
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

