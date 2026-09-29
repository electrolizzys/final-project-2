package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/**
 * 400 validation-error body, e.g.
 * { "title": "One or more validation errors occurred.", "status": 400,
 *   "errors": { "Iso2": ["Iso2 must be 3 characters long."] } }
 * errors maps each invalid parameter to its messages.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationErrorResponse {

    private String title;
    private int status;
    private Map<String, List<String>> errors;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }
}
