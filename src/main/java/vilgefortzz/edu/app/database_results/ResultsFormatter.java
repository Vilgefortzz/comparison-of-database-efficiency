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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class ResultsFormatter {

    private final static String id = "_id";
    private final static String objectId = "$oid";

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

        ResultSetMetaData metaData = resultSet.getMetaData();
        int numberOfColumns = metaData.getColumnCount();

        ArrayList<Row> rows = new ArrayList<>();

        do {
            Row row = new Row();
            for (int i = 1; i <= numberOfColumns; i++) {
                row.addColumn(String.valueOf(resultSet.getObject(i)));
            }
            rows.add(row);
        } while (resultSet.next());

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

                row.addColumn(value);
            }
            rows.add(row);
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
            final int index = i;
            final TableColumn<ObservableList<String>, String> column = new TableColumn<>(key);
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(index))
            );
            table.getColumns().add(column);
            i++;
        }
    }

    @Override
    public String toString() {
        return "ResultsFormatter";
    }
}
