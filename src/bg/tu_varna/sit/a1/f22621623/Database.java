package bg.tu_varna.sit.a1.f22621623;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Database {
    private String databaseName;
    private HashMap<String, Table> databaseTables;
    public Database(String databaseName) {
        this.databaseName = databaseName;
        this.databaseTables = new LinkedHashMap<>();
    }

    public HashMap<String, Table> getDatabaseTables() {
        return databaseTables;
    }
    public void addTable(String tableName, Table table) {
        if(databaseTables.containsKey(tableName)) {
            throw new IllegalArgumentException("Table already exists!");
        }
        databaseTables.put(tableName, table);
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
