package app.dto.user;

import app.entities.Advertisement;

import java.util.ArrayList;

public class CustomSearchResponse {
    private ArrayList<Advertisement> results;
    private Long total;
    public CustomSearchResponse() {}
    public CustomSearchResponse(ArrayList<Advertisement> results, Long total) {
        this.results = results;
        this.total = total;
    }
    public ArrayList<Advertisement> getResults() {
        return results;
    }

    public Long getTotal() {
        return total;
    }

    public void setResults(ArrayList<Advertisement> results) {
        this.results = results;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
