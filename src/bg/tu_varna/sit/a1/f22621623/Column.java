package bg.tu_varna.sit.a1.f22621623;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private String columnName;
    private List<Cell> cell;
    private String columnType;

    public Column(String columnName, String columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.cell = new ArrayList<>();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public List<Cell> getCell() {
        return cell;
    }

    public void setCell(List<Cell> cell) {
        this.cell = cell;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }
}

