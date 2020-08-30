package dataimportservices;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class AdzunaExtractService {

    public JSONObject adzunaExtract(String keyword, String location) throws UnirestException {

        String keywordStr = "";
        boolean kwbegin = true;
        for (String s: keyword.split(" ")){
            if (kwbegin == true){
                keywordStr = s;
                kwbegin = false;
            } else{
                keywordStr = keywordStr+"%20"+s;
            }
        }
        String locStr = "";
        boolean lbegin = true;
        for (String s: location.split(" ")){
            if (lbegin == true){
                locStr = s;
                lbegin = false;
            } else{
                locStr = locStr+"%20"+s;
            }
        }
        System.out.println(keywordStr);
        System.out.println(locStr);

        HttpResponse<JsonNode> response = Unirest.get("https://api.adzuna.com/v1/api/jobs/us/search/1?app_id="+ExtractConfig.adzuna_app_id+"&app_key="+ExtractConfig.adzuna_app_key+"&results_per_page=50&what="+keywordStr+"&where="+locStr+"&max_days_old=7")
                .header("x-rapidapi-host", ExtractConfig.adzuna_rapidApiHost)
                .header("x-rapidapi-key", ExtractConfig.adzuna_rapidApiKey)
                .asJson();

        //JSONObject myObj = request.getBody().getObject();
        //JSONObject jsonObj = response.getBody().getObject();
        //System.out.println(response.getBody().getObject().toString());

        JSONObject myObj = response.getBody().getObject();
        String fileName = "adzuna_jobs.json";
        try{
            FileWriter file = new FileWriter(fileName);
            file.write(myObj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myObj;

        // extract fields from the object
        //String count = myObj.getString("count");
        //String _class = myObj.getString("__CLASS__");
        //System.out.println(_class);
    }
}
