package vilgefortzz.edu.app.associative_structure;

import vilgefortzz.edu.app.database_connection.MySqlConnection;
import vilgefortzz.edu.app.database_results.Record;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agds {

    private Long generationTime;
    private boolean isGenerated;
    private Map<String, AgdsAttribute> agdsAttributes = new HashMap<>();

    public Agds(MySqlConnection mysql) throws SQLException {
        generate(mysql);
    }

    private void generate(MySqlConnection mysql) throws SQLException {

        if (mysql.isConnectedToDatabase()) {

            long startGeneration = System.currentTimeMillis();

            List<String> tables = mysql.getTables(mysql.getDbName());

            for (String tableName : tables) {

                List<String> columns = mysql.getColumns(mysql.getDbName(), tableName);
                List<Record> records = mysql.getRecords(mysql.getDbName(), tableName);

                for (String column : columns) {
                    agdsAttributes.put(column, new AgdsAttribute(column, records));
                }
            }

            long endGeneration = System.currentTimeMillis();
            generationTime = (endGeneration - startGeneration);
            isGenerated = true;
        }
    }

    public Long getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(Long generationTime) {
        this.generationTime = generationTime;
    }

    public boolean isGenerated() {
        return isGenerated;
    }

    public void setGenerated(boolean generated) {
        isGenerated = generated;
    }

    @Override
    public String toString() {
        return "Agds";
    }
}
