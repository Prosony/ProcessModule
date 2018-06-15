package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet("/")
public class WelcomeServlet extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response){
//        response.setContentType("application/json; charset=UTF-8");
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setStatus( HttpServletResponse.SC_OK );
        try {
            response.setContentType("text/plain");
            response.getWriter().write(new Date().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
