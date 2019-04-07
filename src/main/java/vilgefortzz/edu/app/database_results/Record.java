package vilgefortzz.edu.app.database_results;

import java.util.HashMap;
import java.util.Map;

public class Record {

    private Map<String, String> values = new HashMap<>();

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public void addValue(String column, String value) {
        values.put(column, value);
    }

    public String getValue(String column) { return values.get(column); }

    @Override
    public String toString() {
        return "Record";
    }
}
