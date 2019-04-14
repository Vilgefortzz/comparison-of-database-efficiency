package vilgefortzz.edu.app.database_results;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.bson.Document;
import org.jongo.MongoCursor;
import vilgefortzz.edu.app.database_query.AssociativeStructureQuery;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultsFormatter {

    private final static String id = "_id";
    private final static String objectId = "$oid";
    private final static int recordsAmount = 1000;

    public TableView<ObservableList<String>> prepareResultsForMySql(ResultSet resultSet) throws SQLException {

        TableView<ObservableList<String>> table = new TableView<>();

        if (resultSet.next()) {
            ArrayList<Row> rows = createRowsForMysql(resultSet);
            addDataForMysql(resultSet, rows, table);

            return table;
        } else {
            System.out.println("No database records found");
        }

        return null;
    }

    private ArrayList<Row> createRowsForMysql(ResultSet resultSet) throws SQLException {

        int recordsCounter = 0;
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numberOfColumns = metaData.getColumnCount();

        ArrayList<Row> rows = new ArrayList<>();

        do {
            Row row = new Row();
            for (int i = 1; i <= numberOfColumns; i++) {
                row.addColumnValue(String.valueOf(resultSet.getObject(i)));
            }
            rows.add(row);
            recordsCounter++;
        } while (resultSet.next() && recordsCounter < recordsAmount);

        return rows;
    }

    private void addDataForMysql(ResultSet resultSet, ArrayList<Row> rows, TableView table) throws SQLException {

        ResultSetMetaData metaData = resultSet.getMetaData();
        int numberOfColumns = metaData.getColumnCount();

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        for (Row row : rows) {
            data.add(FXCollections.observableArrayList(row.getColumns()));
        }

        table.setItems(data);

        for (int i = 0; i < numberOfColumns; i++) {
            final int index = i;
            final TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    metaData.getColumnLabel(i+1)
            );
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(index))
            );
            table.getColumns().add(column);
        }
    }

    public TableView<ObservableList<String>> prepareResultsForMongoDb(MongoCursor<Document> documents) {

        TableView<ObservableList<String>> table = new TableView<>();

        if (documents.hasNext()) {
            ArrayList<Row> rows = createRowsForMongoDb(documents);
            addDataForMongoDb(documents, rows, table);

            return table;
        } else {
            System.out.println("No database records found");
        }

        return null;
    }

    private ArrayList<Row> createRowsForMongoDb(MongoCursor<Document> documents) {

        int recordsCounter = 0;
        ArrayList<Row> rows = new ArrayList<>();

        for (Document document: documents) {

            Row row = new Row();

            for (String column : document.keySet()) {

                String jsonDocument = document.toJson();
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(jsonDocument, JsonObject.class);

                JsonElement jsonElement = jsonObject.get(column);
                while (jsonElement != null && jsonElement.isJsonObject()) {
                    if (column.equals(id)) {
                        column = objectId;
                    }
                    jsonElement = jsonElement.getAsJsonObject().get(column);
                }

                String value = null;
                if (jsonElement != null) {
                    value = jsonElement.getAsString();
                }

                row.addColumnValue(value);
            }

            rows.add(row);
            recordsCounter++;

            if (recordsCounter >= recordsAmount) break;
        }

        return rows;
    }

    private void addDataForMongoDb(MongoCursor<Document> documents, ArrayList<Row> rows, TableView table) {

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        for (Row row : rows) {
            data.add(FXCollections.observableArrayList(row.getColumns()));
        }

        table.setItems(data);

        Document document = documents.next();

        int i = 0;
        for (String key : document.keySet()) {
            prepareTableColumn(table, key, i++);
        }
    }

    public TableView<ObservableList<String>> prepareResultsForAssociativeStructure
            (List<Record> records, AssociativeStructureQuery query) {

        TableView<ObservableList<String>> table = new TableView<>();

        if (!records.isEmpty()) {
            ArrayList<Row> rows = createRowsForAssociativeStructure(records, query);
            addDataForAssociativeStructure(records, rows, table, query);

            return table;
        } else {
            System.out.println("No database records found");
        }

        return null;
    }

    private ArrayList<Row> createRowsForAssociativeStructure(List<Record> records, AssociativeStructureQuery query) {

        int recordsCounter = 0;
        ArrayList<Row> rows = new ArrayList<>();

        for (Record record: records) {

            Row row = new Row();

            if (query.isSelectedAll()) {
                for (Map.Entry<String, String> column : record.getValues().entrySet()) {
                    row.addColumnValue(column.getValue());
                }
            } else {
                for (String columnName : query.getColumns()) {
                    row.addColumnValue(record.getColumnValue(columnName));
                }
            }

            rows.add(row);
            recordsCounter++;

            if (recordsCounter >= recordsAmount) break;
        }

        return rows;
    }

    private void addDataForAssociativeStructure(List<Record> records, ArrayList<Row> rows,
                                                TableView table, AssociativeStructureQuery query) {

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        for (Row row : rows) {
            data.add(FXCollections.observableArrayList(row.getColumns()));
        }

        table.setItems(data);

        Record record = records.get(0);

        int i = 0;
        if (query.isSelectedAll()) {
            for (String key : record.getValues().keySet()) {
                prepareTableColumn(table, key, i++);
            }
        } else {
            for (String key : query.getColumns()) {
                prepareTableColumn(table, key, i++);
            }
        }
    }

    private void prepareTableColumn(TableView table, String key, int counter) {

        final int index = counter;
        final TableColumn<ObservableList<String>, String> column = new TableColumn<>(key);
        column.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue().get(index))
        );
        table.getColumns().add(column);
    }

    @Override
    public String toString() {
        return "ResultsFormatter";
    }
}
