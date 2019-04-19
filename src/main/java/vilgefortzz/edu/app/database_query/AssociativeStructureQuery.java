package vilgefortzz.edu.app.database_query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssociativeStructureQuery extends Query {

    private final static String allColumns = "*";

    private String tableName;
    private List<String> columns = new ArrayList<>();
    private Map<String, List<String>> andConditions = new HashMap<>();
    private Map<String, List<String>> orConditions = new HashMap<>();
    private Map<String, Map<String, List<String>>> combinedConditions = new HashMap<>();

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

            String[] combinedConditions = conditions.split(" ");
            String combinedConditionType = null;

            for (String combinedCondition : combinedConditions) {

                if (combinedCondition.contains("AND")) {
                    combinedConditionType = "AND";
                } else if (combinedCondition.contains("OR")) {
                    combinedConditionType = "OR";
                } else {

                    String column = combinedCondition.split("=")[0];
                    String value = combinedCondition.split("=")[1];

                    if (combinedConditionType != null) {

                        if (combinedConditionType.equals("AND")) {
                            List<String> andConditionColumnValues = andConditions.get(column);
                            setConditions(andConditionColumnValues, column, value, true);
                        } else {
                            List<String> orConditionColumnValues = orConditions.get(column);
                            setConditions(orConditionColumnValues, column, value, false);
                        }
                    } else {
                        List<String> newAndConditionColumnValues = new ArrayList<>();
                        newAndConditionColumnValues.add(value);
                        this.andConditions.put(column, newAndConditionColumnValues);
                    }
                }
            }

            this.combinedConditions.put("AND", andConditions);
            this.combinedConditions.put("OR", orConditions);
        }
    }

    private void setConditions(List<String> conditionColumnValues,
                               String column, String value, boolean isAndCondition) {

        if (conditionColumnValues != null) {
            conditionColumnValues.add(value);
            if (isAndCondition) {
                this.andConditions.put(column, conditionColumnValues);
            } else {
                this.orConditions.put(column, conditionColumnValues);
            }
        } else {
            List<String> newConditionColumnValues = new ArrayList<>();
            newConditionColumnValues.add(value);
            if (isAndCondition) {
                this.andConditions.put(column, newConditionColumnValues);
            } else {
                this.orConditions.put(column, newConditionColumnValues);
            }
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

    public Map<String, Map<String, List<String>>> getCombinedConditions() {
        return combinedConditions;
    }

    public void setCombinedConditions(Map<String, Map<String, List<String>>> combinedConditions) {
        this.combinedConditions = combinedConditions;
    }

    public Map<String, List<String>> getAndConditions() {
        return andConditions;
    }

    public void setAndConditions(Map<String, List<String>> andConditions) {
        this.andConditions = andConditions;
    }

    public Map<String, List<String>> getOrConditions() {
        return orConditions;
    }

    public void setOrConditions(Map<String, List<String>> orConditions) {
        this.orConditions = orConditions;
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
