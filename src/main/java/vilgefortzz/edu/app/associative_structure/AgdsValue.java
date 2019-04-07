package vilgefortzz.edu.app.associative_structure;

import vilgefortzz.edu.app.database_results.Record;

public class AgdsValue {

    private String name;
    private Record record;

    public AgdsValue(String name, Record record) {

        this.name = name;
        this.record = record;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "AgdsValue";
    }
}
