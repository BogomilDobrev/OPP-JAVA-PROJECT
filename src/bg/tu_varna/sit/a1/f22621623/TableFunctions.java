package bg.tu_varna.sit.a1.f22621623;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TableFunctions {
    private ActionsFile actionsFile;
    private Database database;
    private String databaseName;
    public static final int limitRows = 10;

    public TableFunctions(ActionsFile actionsFile) {
        this.actionsFile = actionsFile;
    }

    public void showTables() {
        HashMap<String, List<Table>> db = database.getDatabase();
        for (Map.Entry<String, List<Table>> stringListEntry : db.entrySet()) {
            for (Table table : stringListEntry.getValue()) {
                System.out.println(table.getTableName());
            }
        }
    }

    public void importTables() {
        List<Table> tableArrayList = new ArrayList<>();
        Map<String, List<List<String>>> read = actionsFile.read();
        for (Map.Entry<String, List<List<String>>> stringListEntry : read.entrySet()) {
            String tableName = stringListEntry.getKey();
            List<List<String>> tables = stringListEntry.getValue();
            List<String> columnNames = tables.get(0);
            List<Row> rows = new ArrayList<>();
            for (int i = 1; i < tables.size(); i++) {
                Row row = new Row(i);
                for (int j = 0; j < tables.get(i).size(); j++) {
                    row.getRowValue().put(columnNames.get(j), tables.get(i).get(j));
                }
                rows.add(row);
            }
            List<String> values = tables.get(1);
            for (int i = 0; i < values.size(); i++) {
                if (checkDouble(values.get(i))) {
                    values.set(i, "Double");
                } else if (checkInteger(values.get(i))) {
                    values.set(i, "Integer");
                } else {
                    values.set(i, "String");
                }
            }
            List<Column> columnsList = new ArrayList<>();
            for (int i = 0; i < tables.size(); i++) {
                Column column = new Column(tables.get(0).get(i), values.get(i));
                for (int j = 0; j < tables.get(i).size(); j++) {
                    column.getCell().add(new Cell(tables.get(j).get(i)));
                }
                columnsList.add(column);
            }
            Table table = new Table(databaseName, tableName);
            table.setRows(rows);
            table.setColumns(columnsList);
            tableArrayList.add(table);
        }
    }

    public void describe(String tableName) {
        HashMap<String, List<Table>> database1 = this.database.getDatabase();
        for (Map.Entry<String, List<Table>> stringListEntry : database1.entrySet()) {
            for (Table table : stringListEntry.getValue()) {
                if (table.getTableName().equals(tableName)) {
                    List<Column> columns = table.getColumns();
                    for (Column column : columns) {
                        System.out.println(column.getColumnType());
                    }
                    return;
                }
            }
        }
    }

    public void print(String name) {
        HashMap<String, List<Table>> database1 = this.database.getDatabase();
        for (Map.Entry<String, List<Table>> stringListEntry : database1.entrySet()) {
            for (Table table : stringListEntry.getValue()) {
                if (table.getTableName().equals(name)) {
                    List<Row> rows = table.getRows();
                    int start = 0;
                    int limit = limitRows;
                    while (limit-- > 0) {
                        if (start < rows.size()) {
                            rows.get(start).printRowValue();
                        } else {
                            System.out.println("Not enough rows");
                            break;
                        }
                        start++;
                        if (limit == 1) {
                            System.out.println("Remaining " + (rows.size() - start));
                            Scanner scanner = new Scanner(System.in);
                            System.out.println("Previous page");
                            System.out.println("Next page");
                            System.out.println("Exit");
                            String answer = scanner.nextLine();
                            if (answer.equalsIgnoreCase("Previous")) {
                                start -= limitRows - 1;
                                limit = limitRows;
                            } else if (answer.equalsIgnoreCase("Next")) {
                                start++;
                                limit = limitRows;
                            } else if (answer.equalsIgnoreCase("Exit")) {
                                limit = 0;
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void export(String name, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName, true)) {
            HashMap<String, List<Table>> database1 = this.database.getDatabase();
            for (Map.Entry<String, List<Table>> stringListEntry : database1.entrySet()) {
                for (Table table : stringListEntry.getValue()) {
                    if (table.getTableName().equals(name)) {
                        fileWriter.write(name);
                        fileWriter.write(System.lineSeparator());
                        List<Column> columns = table.getColumns();
                        for (Column column : columns) {
                            fileWriter.write(column.getColumnName() + " ");
                        }
                        fileWriter.write(System.lineSeparator());

                        List<Row> rows = table.getRows();
                        for (int i = 0; i < rows.size(); i++) {
                            Map<String, String> rowValue = rows.get(i).getRowValue();
                            for (Map.Entry<String, String> kvp : rowValue.entrySet()) {
                                fileWriter.write(kvp.getValue() + " ");
                            }
                            fileWriter.write(System.lineSeparator());
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void select(int n, String value, String tableName) {
        HashMap<String, List<Table>> database1 = database.getDatabase();
        for (Map.Entry<String, List<Table>> stringListEntry : database1.entrySet()) {
            for (Table table : stringListEntry.getValue()) {
                if (table.getTableName().equals(tableName)) {
                    List<Row> rows = table.getRows();
                    for (Row row : rows) {
                        Map<String, String> rowValue = row.getRowValue();
                        int counter = 1;
                        Row currRow = null;
                        for (Map.Entry<String, String> stringStringEntry : rowValue.entrySet()) {
                            counter++;
                            if (counter == n) {
                                if (value.equals(stringStringEntry.getValue())) {
                                    currRow = row;
                                    break;
                                }
                            }
                        }
                        if (currRow == null) {
                            System.out.println("No value found!");
                        } else {
                            currRow.printRowValue();
                        }
                    }
                }
            }
        }


    }

    public void addColumn(String tableName, String columnName, String columnType) {
        HashMap<String, List<Table>> database1 = this.database.getDatabase();
        for (Map.Entry<String, List<Table>> stringListEntry : database1.entrySet()) {
            for (Table table : stringListEntry.getValue()) {
                if (table.getTableName().equals(tableName)) {
                    Column column = new Column(columnName, columnType);
                    int size = table.getColumns().size();
                    for (int i = 0; i < size; i++) {
                        column.getCell().add(new Cell(null));
                    }
                    table.getColumns().add(column);
                    for (int i = 0; i < size; i++) {
                        table.getRows().get(i).getRowValue().put(columnName, null);
                    }
                    return;
                }
            }
        }
    }

    public void update(String tableName, int n, String value, int targetColumn, String targetValue) {
        HashMap<String, List<Table>> database1 = this.database.getDatabase();
        for (Map.Entry<String, List<Table>> stringListEntry : database1.entrySet()) {
            for (Table table : stringListEntry.getValue()) {
                if (table.getTableName().equals(tableName)) {
                    List<Row> rows = table.getRows();
                    int counterForColum = 1;
                    boolean foundReal = false;
                    for (Row row : rows) {
                        counterForColum++;
                        Map<String, String> rowValue = row.getRowValue();
                        int counter = 1;
                        boolean found = false;
                        Row currRowForTarget = null;
                        for (Map.Entry<String, String> stringStringEntry : rowValue.entrySet()) {
                            counter++;
                            if (counter == n) {
                                if (value.equals(stringStringEntry.getValue())) {
                                    found = true;
                                }
                            }
                            if (counter == targetColumn) {
                                currRowForTarget = row;
                            }
                        }
                        counter = 1;
                        if (found) {
                            for (Map.Entry<String, String> stringStringEntry : rowValue.entrySet()) {
                                counter++;
                                if (counter == targetColumn) {
                                    stringStringEntry.setValue(targetValue);
                                }
                            }
                            foundReal = found;
                            break;
                        }
                    }
                    if (foundReal) {
                        table.getColumns().get(targetColumn).getCell().get(counterForColum).setValue(targetValue);
                    }
                }
            }
        }
    }

    public void delete(String tableName, int n, String value) {
        HashMap<String, List<Table>> database1 = this.database.getDatabase();
        for (Map.Entry<String, List<Table>> stringListEntry : database1.entrySet()) {
            for (Table table : stringListEntry.getValue()) {
                if (table.getTableName().equals(tableName)) {
                    List<Column> columns = table.getColumns();
                    List<Row> rows = table.getRows();
                    int counter = 1;
                    boolean flag = false;
                    for (Row row : rows) {
                        counter++;
                        Map<String, String> rowValue = row.getRowValue();
                        int counterForColum = 1;
                        for (Map.Entry<String, String> stringStringEntry : rowValue.entrySet()) {
                            counterForColum++;
                            if (counterForColum == n) {
                                if (value.equals(stringStringEntry.getValue())) {
                                    table.getRows().remove(row);
                                    flag = true;
                                    break;
                                }
                            }
                            if (flag) {
                                break;
                            }
                        }
                    }
                    table.getColumns().get(n).getCell().remove(counter);

                }
            }
        }
    }

    private boolean checkDouble(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean checkInteger(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}