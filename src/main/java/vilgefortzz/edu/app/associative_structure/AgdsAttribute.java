package vilgefortzz.edu.app.associative_structure;

import vilgefortzz.edu.app.database_results.Record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgdsAttribute {

    private String name;

    private Map<String, List<AgdsValue>> agdsValues = new HashMap<>();

    public AgdsAttribute(String name, List<Record> records) throws SQLException {

        this.name = name;
        generate(records);
    }

    private void generate(List<Record> records) throws SQLException {

        for (Record record : records) {
            if (record.getValues().containsKey(name)) {
                String value = record.getValue(name);
                if (agdsValues.containsKey(value)) {
                    agdsValues.get(value).add(new AgdsValue(value, record));
                } else {
                    List<AgdsValue> agdsValuesList = new ArrayList<>();
                    agdsValuesList.add(new AgdsValue(value, record));
                    agdsValues.put(value, agdsValuesList);
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AgdsAttribute";
    }
}
