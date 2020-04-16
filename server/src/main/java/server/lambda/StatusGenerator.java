package server.lambda;

import java.util.ArrayList;
import java.util.List;

import model.domain.Status;

public class StatusGenerator {
    private static StatusGenerator instance;

    private StatusGenerator() {}

    public static StatusGenerator getInstance() {
        if (instance == null) {
            instance = new StatusGenerator();
        }
        return instance;
    }

    public List<Status> getNStatuses(int n){
        List<Status> statuses = new ArrayList<>();
        for(int i=0; i < n; i++){
            Status tmp = new Status();
            tmp.setMilliseconds();
            tmp.setMessage("message #"
                    + Integer.toString(i)
                    + " https://www.bannermediastudios.com/");
            statuses.add(tmp);
        }
        return statuses;
    }

}
