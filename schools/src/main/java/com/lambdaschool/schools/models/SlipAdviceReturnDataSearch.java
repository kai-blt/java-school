package com.lambdaschool.schools.models;

import java.util.List;

public class SlipAdviceReturnDataSearch {
    private long total_results;
    private String query;
    private List<SlipSearch> slips;

    public long getTotal_results() {
        return total_results;
    }

    public void setTotal_results(long total_results) {
        this.total_results = total_results;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<SlipSearch> getSlips() {
        return slips;
    }

    public void setSlips(List<SlipSearch> slips) {
        this.slips = slips;
    }
}
