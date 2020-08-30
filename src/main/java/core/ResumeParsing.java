package core;

import com.mashape.unirest.http.exceptions.UnirestException;
import dataimportservices.AdzunaExtractService;
import dataimportservices.CareerjetExtractService;
import dataimportservices.GithubExtractService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class ResumeParsing {

    private Map<String, Integer> skillMetric = new HashMap<String, Integer>();
    private PriorityQueue<JobEntity> pq = new PriorityQueue<JobEntity>();

    public PriorityQueue<JobEntity> fetchMyResult(String skills, String resume, String keyword, String location) throws UnirestException, IOException {

        for (String s: skills.split("\\s*,\\s*")){
            skillMetric.put(s.toLowerCase(), 0);
        }

        for (String s: resume.split(" |\\s|\\t|\\s*,\\s*|\\s*;\\s*|\\.|\\?|!|\\s*-\\s*|\\s*:\\s*|@|\\s*[\\s*|\\s*]\\s*|\\(|\\)|\\{|\\}|_|/")){
            System.out.println(s);
            if (skillMetric.containsKey(s.toLowerCase())){
                skillMetric.replace(s.toLowerCase(), skillMetric.get(s.toLowerCase())+1);
            }
        }

        for (String s: skillMetric.keySet()){
            System.out.println(s+" -- "+skillMetric.get(s.toLowerCase()));
        }

        AdzunaExtractService aes = new AdzunaExtractService();
        JSONObject adzunaJsonObject = aes.adzunaExtract(keyword, location);

        JSONArray adzunaResultArray = (JSONArray) adzunaJsonObject.get("results");
        for (int i = 0; i < adzunaResultArray.length(); i++) {
            JSONObject jsonobject = adzunaResultArray.getJSONObject(i);
            String jobId = jsonobject.getString("id");
            String description = jsonobject.getString("description");
            String redirect_url = jsonobject.getString("redirect_url");

            System.out.println(jobId +"   "+description+"   "+redirect_url);
            int weight = 0;
            for (String s: description.split(" |\\s|\\t|\\s*,\\s*|\\s*;\\s*|\\.|\\?|!|\\s*-\\s*|\\s*:\\s*|@|\\s*[\\s*|\\s*]\\s*|\\(|\\)|\\{|\\}|_|/|\\s*<\\s*|\\s*>\\s*")){
                if (skillMetric.containsKey(s.toLowerCase())){
                    weight = weight + skillMetric.get(s.toLowerCase());
                }
            }
            if (weight > 0){
                JobEntity jobEntry = new JobEntity(redirect_url, weight, "Adzuna");
                pq.add(jobEntry);
            }
        }

        GithubExtractService ges = new GithubExtractService();
        JSONArray gitResultArray = ges.githubExtract(keyword, location);

        for (int i = 0; i < gitResultArray.length(); i++) {
            JSONObject jsonobject = gitResultArray.getJSONObject(i);
            String title = jsonobject.getString("title");
            String description = jsonobject.getString("description");
            String url = jsonobject.getString("url");

            System.out.println(title +"   "+description+"   "+url);
            int weight = 0;
            for (String s: description.split(" |\\s|\\t|\\s*,\\s*|\\s*;\\s*|\\.|\\?|!|\\s*-\\s*|\\s*:\\s*|@|\\s*[\\s*|\\s*]\\s*|\\(|\\)|\\{|\\}|_|/|\\s*<\\s*|\\s*>\\s*")){
                if (skillMetric.containsKey(s.toLowerCase())){
                    weight = weight + skillMetric.get(s.toLowerCase());
                }
            }
            if (weight > 0){
                JobEntity jobEntry = new JobEntity(url, weight, "Github");
                pq.add(jobEntry);
            }
        }

        CareerjetExtractService cjes = new CareerjetExtractService();
        org.json.simple.JSONObject cjJsonObject = cjes.careerjetExtract(skills.split("\\s*,\\s*")[0], location);

        org.json.simple.JSONArray cjResultArray = (org.json.simple.JSONArray) cjJsonObject.get("jobs");
        for (int i = 0; i < cjResultArray.size(); i++) {
            org.json.simple.JSONObject jsonobject = (org.json.simple.JSONObject) cjResultArray.get(i);
            String title = (String) jsonobject.get("title");
            String description = (String) jsonobject.get("description");
            String url = (String) jsonobject.get("url");

            System.out.println(title +"   "+description+"   "+url);
            int weight = 0;
            for (String s: description.split(" |\\s|\\t|\\s*,\\s*|\\s*;\\s*|\\.|\\?|!|\\s*-\\s*|\\s*:\\s*|@|\\s*[\\s*|\\s*]\\s*|\\(|\\)|\\{|\\}|_|/|\\s*<\\s*|\\s*>\\s*")){
                if (skillMetric.containsKey(s.toLowerCase())){
                    weight = weight + skillMetric.get(s.toLowerCase());
                }
            }
            if (weight > 0){
                JobEntity jobEntry = new JobEntity(url, weight, "Github");
                pq.add(jobEntry);
            }
        }

        return pq;
    }
}
