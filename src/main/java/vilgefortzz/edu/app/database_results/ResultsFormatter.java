package vilgefortzz.edu.app.database_results;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class ResultsFormatter {

    public TableView<ObservableList<String>> prepareResultsForTable(ResultSet resultSet) throws IOException, SQLException {

        TableView<ObservableList<String>> table = new TableView<>();

        if (resultSet.next()) {
            ArrayList<Row> rows = createRows(resultSet);
            addData(resultSet, rows, table);

            return table;
        } else {
            System.out.println("No database records found");
        }

        return null;
    }

    private ArrayList<Row> createRows(ResultSet resultSet) throws SQLException {

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

    private void addData(ResultSet resultSet, ArrayList<Row> rows, TableView table) throws SQLException {

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

    @Override
    public String toString() {
        return "ResultsFormatter";
    }

}
