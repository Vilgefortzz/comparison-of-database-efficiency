package vilgefortzz.edu.app.database_connection;

import javafx.scene.control.TableView;
import vilgefortzz.edu.app.associative_structure.Agds;
import vilgefortzz.edu.app.associative_structure.AgdsAttribute;
import vilgefortzz.edu.app.associative_structure.AgdsValue;
import vilgefortzz.edu.app.database_query.AssociativeStructureQuery;
import vilgefortzz.edu.app.database_results.Record;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssociativeStructureConnection extends Connection {

    private AssociativeStructureQuery query;
    private Agds agds;

    @Override
    public boolean connectToServer() {
        return true;
    }

    @Override
    public boolean disconnectFromServer() {
        return true;
    }

    @Override
    public boolean connectToDatabase(String dbName) throws IOException, InterruptedException {
        return true;
    }

    @Override
    public String showDatabases() throws IOException, InterruptedException {
        return "";
    }

    @Override
    public TableView query() {

        if (agds.isGenerated()) {

            List<Record> records = new ArrayList<>();

            long startQuery = System.currentTimeMillis();
            if (query.getCombinedConditions().isEmpty()) {
                Map<String, AgdsAttribute> agdsAttributes = agds.agdsAttributes;
                for (Map.Entry<String, AgdsAttribute> agdsAttribute : agdsAttributes.entrySet()) {
                    for (Map.Entry<String, List<AgdsValue>> agdsValueList : agdsAttribute.getValue().agdsValues.entrySet()) {
                        for (AgdsValue agdsValue : agdsValueList.getValue()) {
                            Record record = agdsValue.getRecord();
                            if (!records.contains(record)) {
                                records.add(record);
                            }
                        }
                    }
                }
            } else {

                if (!query.getAndConditions().isEmpty()) {
                    Map.Entry<String, List<String>> firstAndCondition =
                            query.getAndConditions().entrySet().iterator().next();
                    AgdsAttribute agdsAttribute = agds.agdsAttributes.get(firstAndCondition.getKey());
                    for (String columnValue : firstAndCondition.getValue()) {
                        List<AgdsValue> agdsValues = agdsAttribute.agdsValues.get(columnValue);
                        for (AgdsValue agdsValue : agdsValues) {
                            Record record = agdsValue.getRecord();
                            if (record.checkConditions(query.getAndConditions())) {
                                records.add(record);
                            }
                        }
                    }
                }
                if (!query.getOrConditions().isEmpty()) {
                    for (Map.Entry<String, List<String>> orCondition : query.getOrConditions().entrySet()) {
                        AgdsAttribute agdsAttribute = agds.agdsAttributes.get(orCondition.getKey());
                        for (String columnValue : orCondition.getValue()) {
                            List<AgdsValue> agdsValues = agdsAttribute.agdsValues.get(columnValue);
                            for (AgdsValue agdsValue : agdsValues) {
                                records.add(agdsValue.getRecord());
                            }
                        }
                    }
                }
            }
            long endQuery = System.currentTimeMillis();

            query.setTime(endQuery - startQuery);

            return resultsFormatter.prepareResultsForAssociativeStructure(records, query);
        }

        return null;
    }

    public void generateAgds(MySqlConnection mysql) throws SQLException {
        agds = new Agds(mysql);
    }

    public AssociativeStructureQuery getQuery() {
        return query;
    }

    public void setQuery(AssociativeStructureQuery query) {
        this.query = query;
    }

    public void clearQuery() {
        query = null;
    }

    public Agds getAgds() {
        return agds;
    }

    public void setAgds(Agds agds) {
        this.agds = agds;
    }

    @Override
    public String toString() {
        return "AssociativeStructureConnection";
    }
}
