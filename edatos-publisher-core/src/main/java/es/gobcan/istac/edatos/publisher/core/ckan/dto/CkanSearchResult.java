package es.gobcan.istac.edatos.publisher.core.ckan.dto;

import java.util.List;

public class CkanSearchResult {

    private int count;
    private String sort;
    private List<CkanPackage> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<CkanPackage> getResults() {
        return results;
    }

    public void setResults(List<CkanPackage> results) {
        this.results = results;
    }
}
