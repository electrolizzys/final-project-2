package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Body the locations page POSTs to atmsAndBranches/list, e.g.
 * { "filter": [], "locale": "en-US", "keyword": "Rustavi", "myLocation": {...} }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationSearchRequest {

    private List<String> filter;
    private String locale;
    private String keyword;

    public List<String> getFilter() {
        return filter;
    }

    public void setFilter(List<String> filter) {
        this.filter = filter;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
