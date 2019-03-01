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

    public void addColumn(String columnName) {
        columns.add(columnName);
    }
}
