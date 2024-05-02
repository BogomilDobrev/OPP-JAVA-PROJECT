package bg.tu_varna.sit.a1.f22621623;

import java.util.HashMap;
import java.util.List;

public class Database {
    private HashMap<String, List<Table>> database;
    public Database() {
        database = new HashMap<>();
    }

    public HashMap<String, List<Table>> getDatabase() {
        return database;
    }

    public void setDatabase(HashMap<String, List<Table>> database) {
        this.database = database;
    }
}
