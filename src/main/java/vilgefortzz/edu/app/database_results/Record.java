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

    public void addColumnValue(String columnName, String value) {
        values.put(columnName, value);
    }

    public String getColumnValue(String columnName) { return values.get(columnName); }

    @Override
    public String toString() {
        return "Record";
    }
}
