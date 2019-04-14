package vilgefortzz.edu.app.database_results;

import java.util.ArrayList;

public class Row {

    private ArrayList<String> columns = new ArrayList<>();

    public ArrayList<String> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    public void addColumnValue(String columnValue) {
        columns.add(columnValue);
    }
}
