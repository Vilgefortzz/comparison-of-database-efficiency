# comparison-of-database-efficiency

Tool for comparison database efficiency.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development purposes.

### Prerequisites

1. MySQL
2. MongoDB

### Installing

1. Download JavaFX library for specific system and JDK version: https://gluonhq.com/products/javafx/

2. Add lib folder from JavaFX to classpath of the project

3. Copy .env.example to .env

4. Provide correct data for your environment file ( .env )

For example:

        # MySQL configuration
        MYSQL_HOST=localhost
        MYSQL_PORT=3306
        MYSQL_USER=root
        MYSQL_PASSWORD=root
        
        # MongoDB configuration
        MONGODB_HOST=localhost
        MONGODB_PORT=27017