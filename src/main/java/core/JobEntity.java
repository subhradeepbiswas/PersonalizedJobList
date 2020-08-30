package core;

public class JobEntity implements Comparable<JobEntity>{
    String url;
    int weight;
    String source;

    public JobEntity(String url, int weight, String source){
        this.url = url;
        this.weight = weight;
        this.source = source;
    }

    @Override
    public int compareTo(JobEntity o1) {
        if (o1.weight > this.weight)
            return  1;
        if (o1.weight < this.weight)
            return  -1;
        return 0;
    }
    public String getUrl(){
        return this.url;
    }

    public int getWeight(){
        return this.weight;
    }
}
