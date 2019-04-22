package vilgefortzz.edu.app.database_query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssociativeStructureQuery extends Query {

    private final static String allColumns = "*";

    private String tableName;
    private List<String> columns = new ArrayList<>();

    /**
     * Usual logical conditions
     */
    private Map<String, List<String>> andConditions = new HashMap<>();
    private Map<String, List<String>> orConditions = new HashMap<>();

    /**
     * Grouped logical conditions
     */
    private Map<String, List<String>> groupedAndConditions = new HashMap<>();
    private Map<String, List<String>> groupedOrConditions = new HashMap<>();
    private String groupedConditionType = null;

    private boolean selectedAll;
    private boolean conditions;

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

            this.conditions = true;
            String[] combinedConditions = conditions.split(" ");
            String combinedConditionType = null;
            boolean isGroup = false;

            for (String combinedCondition : combinedConditions) {

                if (combinedCondition.contains("AND")) {
                    combinedConditionType = "AND";
                } else if (combinedCondition.contains("OR")) {
                    combinedConditionType = "OR";
                } else if (combinedCondition.contains("(")) {
                    groupedConditionType = combinedConditionType;
                    isGroup = true;
                } else if (combinedCondition.contains(")")) {
                    isGroup = false;
                } else {

                    String column = combinedCondition.split("=")[0];
                    String value = combinedCondition.split("=")[1];

                    if (combinedConditionType != null) {

                        if (combinedConditionType.equals("AND")) {
                            List<String> andConditionColumnValues;
                            if (isGroup) {
                                andConditionColumnValues = groupedAndConditions.get(column);
                            } else {
                                andConditionColumnValues = andConditions.get(column);
                            }
                            setConditions(andConditionColumnValues, column, value, true, isGroup);
                        } else {
                            List<String> orConditionColumnValues;
                            if (isGroup) {
                                orConditionColumnValues = groupedOrConditions.get(column);
                            } else {
                                orConditionColumnValues = orConditions.get(column);
                            }
                            setConditions(orConditionColumnValues, column, value, false, isGroup);
                        }
                    } else {
                        List<String> newAndConditionColumnValues = new ArrayList<>();
                        newAndConditionColumnValues.add(value);
                        this.andConditions.put(column, newAndConditionColumnValues);
                    }
                }
            }
        }
    }

    private void setConditions(List<String> conditionColumnValues, String column,
                               String value, boolean isAndCondition, boolean isGroup) {

        if (conditionColumnValues != null) {
            conditionColumnValues.add(value);
            groupConditions(conditionColumnValues, column, isAndCondition, isGroup);
        } else {
            List<String> newConditionColumnValues = new ArrayList<>();
            newConditionColumnValues.add(value);
            groupConditions(newConditionColumnValues, column, isAndCondition, isGroup);
        }
    }

    private void groupConditions(List<String> conditionColumnValues, String column,
                                 boolean isAndCondition, boolean isGroup) {

        if (isGroup) {
            if (isAndCondition) {
                this.groupedAndConditions.put(column, conditionColumnValues);
            } else {
                this.groupedOrConditions.put(column, conditionColumnValues);
            }
        } else {
            if (isAndCondition) {
                this.andConditions.put(column, conditionColumnValues);
            } else {
                this.orConditions.put(column, conditionColumnValues);
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

    public Map<String, List<String>> getGroupedAndConditions() {
        return groupedAndConditions;
    }

    public void setGroupedAndConditions(Map<String, List<String>> groupedAndConditions) {
        this.groupedAndConditions = groupedAndConditions;
    }

    public Map<String, List<String>> getGroupedOrConditions() {
        return groupedOrConditions;
    }

    public void setGroupedOrConditions(Map<String, List<String>> groupedOrConditions) {
        this.groupedOrConditions = groupedOrConditions;
    }

    public String getGroupedConditionType() {
        return groupedConditionType;
    }

    public void setGroupedConditionType(String groupedConditionType) {
        this.groupedConditionType = groupedConditionType;
    }

    public boolean isSelectedAll() {
        return selectedAll;
    }

    public void setSelectedAll(boolean selectedAll) {
        this.selectedAll = selectedAll;
    }

    public boolean areConditions() {
        return conditions;
    }

    public void setConditions(boolean conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return "AssociativeStructureQuery";
    }
}
