package vilgefortzz.edu.app.database_configuration;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;

public class Configuration {

    public static String MYSQL_HOST;
    public static String MYSQL_PORT;
    public static String MYSQL_USER;
    public static String MYSQL_PASSWORD;

    public static String MONGODB_HOST;
    public static int MONGODB_PORT;

    public static void readConfiguration() {

        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .load();

        MYSQL_HOST = dotenv.get("MYSQL_HOST");
        MYSQL_PORT = dotenv.get("MYSQL_PORT");
        MYSQL_USER = dotenv.get("MYSQL_USER");
        MYSQL_PASSWORD = dotenv.get("MYSQL_PASSWORD");

        MONGODB_HOST = dotenv.get("MONGODB_HOST");
        MONGODB_PORT = Integer.parseInt(Objects.requireNonNull(dotenv.get("MONGODB_PORT")));
    }

}
