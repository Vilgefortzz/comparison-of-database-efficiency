package vilgefortzz.edu.app.database_results;

import java.util.HashMap;
import java.util.List;
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

    public boolean checkConditions(Map<String, List<String>> conditions) {

        for (Map.Entry<String, List<String>> condition : conditions.entrySet()) {
            for (String columnValue : condition.getValue()) {
                if (!getColumnValue(condition.getKey()).equals(columnValue)) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean checkGroupedConditions(Map<String, List<String>> groupedConditions) {

        if (groupedConditions.isEmpty()) return true;

        return checkConditions(groupedConditions);
    }

    @Override
    public String toString() {
        return "Record";
    }
}
