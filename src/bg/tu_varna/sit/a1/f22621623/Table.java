package bg.tu_varna.sit.a1.f22621623;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Row> rows;
    private List<Column> columns;
    private String databaseName;
    private String tableName;
    public Table(String databaseName, String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        rows = new ArrayList<>();
        columns = new ArrayList<>();
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

