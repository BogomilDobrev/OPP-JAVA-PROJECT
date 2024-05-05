package bg.tu_varna.sit.a1.f22621623;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Row {
    private int row;
    private Map<String, String> rowValue;
    public Row(int row) {
        this.row = row;
        this.rowValue = new LinkedHashMap<>();
    }

    public int getRow() { return row; }

    public void setRow(int row) { this.row = row; }

    public Map<String, String> getRowValue() { return rowValue; }

    public void setRowValue(Map<String, String> rowValue) { this.rowValue = rowValue; }
    public void printRowValue(){
        for(Map.Entry<String, String> entry : rowValue.entrySet()){
            System.out.print(entry.getValue()+"\t\t");
        }
        System.out.println();
    }
}
