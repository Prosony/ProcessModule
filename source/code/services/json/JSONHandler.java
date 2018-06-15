package services.json;

import debug.LocalLog;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class JSONHandler {

        private LocalLog LOG = LocalLog.getInstance();

        public JSONObject getJSONFromRequest(HttpServletRequest request){
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
