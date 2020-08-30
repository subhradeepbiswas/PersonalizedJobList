package dataimportservices;

import com.careerjet.webservice.api.Client;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.*;
import java.util.HashMap;
import java.util.Map;

public class CareerjetExtractService {

    Client c = new Client("en_US");
    private final Map<String, String> reqArgs = new HashMap<String, String>();

    public JSONObject careerjetExtract(String keyword, String location) throws UnirestException {
        String keywordStr = "";
        boolean kwbegin = true;
        for (String s: keyword.split(" ")){
            if (kwbegin == true){
                keywordStr = s;
                kwbegin = false;
            } else{
                keywordStr = keywordStr+"+"+s;
            }
        }
        String locStr = "";
        boolean lbegin = true;
        for (String s: location.split(" ")){
            if (lbegin == true){
                locStr = s;
                lbegin = false;
            } else{
                locStr = locStr+"+"+s;
            }
        }
        System.out.println(keywordStr);
        System.out.println(locStr);

        reqArgs.put("keywords", keywordStr.toLowerCase());
        reqArgs.put("location", locStr.toLowerCase());
        reqArgs.put("affid", ExtractConfig.careerJet_affid);
        reqArgs.put("user_ip", ExtractConfig.careerJet_user_ip);
        reqArgs.put("user_agent", ExtractConfig.careerJet_user_agent);
        reqArgs.put("url", ExtractConfig.careerJet_url);

        JSONObject results = (JSONObject) c.search(reqArgs);

        return results;
    }
}
