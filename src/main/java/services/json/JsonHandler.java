package services.json;

/**
 * @author Prosony
 * @since 0.0.1
 */
import degub.LocalLog;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class JsonHandler {

    private LocalLog LOG = LocalLog.getInstance();

    public JSONObject getJsonFromRequest(HttpServletRequest request){

        StringBuilder builder = new StringBuilder();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            if (!builder.toString().isEmpty()){
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new String(builder.toString().getBytes(),"UTF-8")); //TODO rewrite this shit
                JSONObject jsonObject = (JSONObject) obj;
                LOG.info("[JsonHandler] jsonObject: "+jsonObject);
                return jsonObject;
            }
            return null;
        } catch (IOException | ParseException e) {
//            LOG.error();
            e.printStackTrace();
        }
        return null;

    }
}