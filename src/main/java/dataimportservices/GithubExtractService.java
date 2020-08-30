package dataimportservices;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

import java.io.FileWriter;
import java.io.IOException;

public class GithubExtractService {

    public JSONArray githubExtract(String keyword, String location) throws UnirestException {

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

        HttpResponse<String> response = Unirest.get("https://jobs.github.com/positions.json?description="+keywordStr+"&location="+locStr)
                                            .asString();

        JSONArray gitResponseArray = new JSONArray(response.getBody().toString());
        String fileName = "github_jobs.json";
        try{
            FileWriter file = new FileWriter(fileName);
            file.write(gitResponseArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gitResponseArray;

    }
}
