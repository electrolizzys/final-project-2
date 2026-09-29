package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * One ATM/branch/CDM in the atmsAndBranches/list response, e.g.
 * { "id": 272, "type": "ATM", "address": "21st km, Rustavi Highway", "regionName": "Rustavi", ... }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankLocation {

    private int id;
    private String type;
    private String name;
    private String address;
    private String location;
    private String regionName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * Whether the keyword appears in any of the text fields the search matches on.
     */
    public boolean mentions(String keyword) {
        String needle = keyword.toLowerCase();
        return contains(address, needle) || contains(regionName, needle)
                || contains(location, needle) || contains(name, needle);
    }

    private static boolean contains(String field, String needle) {
        return field != null && field.toLowerCase().contains(needle);
    }

    @Override
    public String toString() {
        return type + " #" + id + " (" + address + ", " + regionName + ")";
    }
}
