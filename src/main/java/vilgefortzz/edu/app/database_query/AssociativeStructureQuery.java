package vilgefortzz.edu.app.database_query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssociativeStructureQuery extends Query {

    private final static String allColumns = "*";

    private String tableName;
    private List<String> columns = new ArrayList<>();
    private Map<String, String> conditions = new HashMap<>();
    private boolean selectedAll;

    public AssociativeStructureQuery(String query) {

        super(query);
        createTableName();
        createColumns();
        createConditions();
    }

    private void createTableName() {

        String tableName = query.split("(?i)FROM")[1].trim();

        this.tableName = tableName.split(";")[0];
    }

    private void createColumns() {

        String selectedColumns = query.split("(?i)SELECT")[1].trim();
        selectedColumns = selectedColumns.split("(?i)FROM")[0].trim();
        selectedAll = selectedColumns.equals(allColumns);

        if (!selectedAll) {
            String[] columns = selectedColumns.split(",");
            for (String column : columns) {
                this.columns.add(column.trim());
            }
        }
    }

    private void createConditions() {

        String conditions = null;
        String[] whereClause = query.split("(?i)WHERE");

        if (whereClause.length == 2) {
            conditions = whereClause[1].split(";")[0].trim();
        }

        if (conditions != null && !conditions.isEmpty()) {

            String column = conditions.split("=")[0];
            String value = conditions.split("=")[1];

            this.conditions.put(column, value);
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public Map<String, String> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, String> conditions) {
        this.conditions = conditions;
    }

    public boolean isSelectedAll() {
        return selectedAll;
    }

    public void setSelectedAll(boolean selectedAll) {
        this.selectedAll = selectedAll;
    }

    @Override
    public String toString() {
        return "AssociativeStructureQuery";
    }
}
