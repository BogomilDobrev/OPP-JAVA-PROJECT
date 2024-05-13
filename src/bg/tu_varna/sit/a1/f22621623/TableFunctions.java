package bg.tu_varna.sit.a1.f22621623;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TableFunctions {
    private ActionsFile actionsFile;
    private Database database;
    private String databaseName;
    public static final int limitRows = 10;

    public TableFunctions(ActionsFile actionsFile, Database database, String databaseName) {
        this.actionsFile = actionsFile;
        this.database = database;
        this.databaseName = databaseName;
    }

    public void showTables() {
        HashMap<String, Table> databaseTables = database.getDatabaseTables();
        for (Map.Entry<String, Table> stringTableEntry : databaseTables.entrySet()) {
            System.out.println("Table Name: " + stringTableEntry.getKey());
            Table table = stringTableEntry.getValue();
            List<Row> rows = table.getRows();
            List<Column> columns = table.getColumns();
            for (Column column : columns) {
                System.out.print(column.getColumnName() + "\t\t");
            }
            System.out.println();
            for (Row row : rows) {
                row.printRowValue();
            }
        }
    }

    public void importTables(String path ) {
        List<Table> tableArrayList = new ArrayList<>();
        Map<String, List<List<String>>> read = actionsFile.read(path);
        for (Map.Entry<String, List<List<String>>> stringListEntry : read.entrySet()) {
            String tableName = stringListEntry.getKey();
            List<List<String>> tables = stringListEntry.getValue();// redove i koloni
            List<String> columnNames = tables.get(0);
            List<Row> rows = new ArrayList<>();
            for (int i = 1; i < tables.size(); i++) {
                Row row = new Row(i);
                for (int j = 0; j < tables.get(i).size(); j++) {
                    row.getRowValue().put(columnNames.get(j), tables.get(i).get(j));
                }
                rows.add(row);
            }
            List<String> values = new ArrayList<>(tables.get(1));// vzemame parviya red za da proverim kakav tip e
            for (int i = 0; i < values.size(); i++) {
                if (checkInteger(values.get(i))) {
                    values.set(i, "Integer");
                } else if (checkDouble(values.get(i))) {
                    values.set(i, "Double");
                } else {
                    values.set(i, "String");
                }
            }
            List<Column> columnsList = new ArrayList<>();
            for (int i = 0; i < tables.get(0).size(); i++) {
                Column column = new Column(tables.get(0).get(i), values.get(i));
                for (int j = 0; j < tables.size(); j++) {
                    column.getCell().add(new Cell(tables.get(j).get(i)));
                }
                columnsList.add(column);
            }
            Table table = new Table(tableName);
            table.setRows(rows);
            table.setColumns(columnsList);
            tableArrayList.add(table);
            database.addTable(tableName, table);
        }
    }

    public void describe(String tableName) {
        HashMap<String, Table> databaseTables = database.getDatabaseTables();
        for (Map.Entry<String, Table> stringTableEntry : databaseTables.entrySet()) {
            Table table = stringTableEntry.getValue();
            if (table.getTableName().equals(tableName)) {
                List<Column> columns = table.getColumns();
                for (Column column : columns) {
                    System.out.println(column.getColumnType());
                }
                return;
            }
        }
    }

    public void print(String name) {
        HashMap<String, Table> databaseTables = database.getDatabaseTables();
        if (databaseTables.containsKey(name)) {
            Table table = databaseTables.get(name);
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
        } else {
            System.out.println("Table not found");
        }
    }

    public void export(String name, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName, true)) {
            HashMap<String, Table> databaseTables = database.getDatabaseTables();
            if (databaseTables.containsKey(name)) {
                Table table = databaseTables.get(name);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void select(int n, String value, String tableName) {
        HashMap<String, Table> databaseTables = database.getDatabaseTables();
        if (databaseTables.containsKey(tableName)) {
            Table table = databaseTables.get(tableName);
            List<Row> rows = table.getRows();
            String columnName = table.getColumns().get(n-1).getColumnName();
            for (Row row : rows) {
                if(row.getRowValue().containsKey(columnName)){
                    String valueFromRow = row.getRowValue().get(columnName);
                    if (value.equals(valueFromRow)) {
                        row.printRowValue();
                    }
                }
            }
        }
    }

    public void addColumn(String tableName, String columnName, String columnType) {
        HashMap<String, Table> databaseTables = database.getDatabaseTables();
        if (databaseTables.containsKey(tableName)) {
            Table table = databaseTables.get(tableName);
            Column column = new Column(columnName, columnType);
            int size = table.getColumns().size();
            column.getCell().add(new Cell(columnName));
            for (int i = 1; i < table.getRows().size(); i++) {
                column.getCell().add(new Cell(null));
            }
            table.getColumns().add(column);
            size = table.getColumns().size();
            for (int i = 0; i < size; i++) {
                table.getRows().get(i).getRowValue().put(columnName, null);
            }

            return;
        }
    }

    public void update(String tableName, int n, String value, int targetColumn, String targetValue) {
        HashMap<String, Table> databaseTables = database.getDatabaseTables();
        if (databaseTables.containsKey(tableName)) {
            boolean foundReal = false;
            Table table = databaseTables.get(tableName);
            List<Row> rows = table.getRows();
            String columnName = table.getColumns().get(n-1).getColumnName();
            String columnNameTarget = table.getColumns().get(targetColumn-1).getColumnName();

            for (int i =0;i< rows.size();i++) {
                if(rows.get(i).getRowValue().containsKey(columnName) && rows.get(i).getRowValue().get(columnName).equals(value)){
                    rows.get(i).getRowValue().put(columnNameTarget,targetValue);
                    foundReal = true;
                    table.getColumns().get(targetColumn-1).getCell().get(i+1).setValue(targetValue);
                }
            }
        }
    }

    public void delete(String tableName, int n, String value) {
        HashMap<String, Table> databaseTables = database.getDatabaseTables();
        if (databaseTables.containsKey(tableName)) {
            Table table = databaseTables.get(tableName);
            List<Column> columns = table.getColumns();
            List<Row> rows = table.getRows();
            String columnName = table.getColumns().get(n - 1).getColumnName();
            boolean flag = false;
            for (int i = 0; i <rows.size() ; i++) {
                if(rows.get(i).getRowValue().containsKey(columnName)
                        && rows.get(i).getRowValue().get(columnName).equals(value)){
                    rows.remove(rows.get(i));
                    for (int j = 0; j < table.getColumns().size(); j++) {
                        table.getColumns().get(j).getCell().remove(i+1);
                    }
                    i--;
                }
            }
        }
    }

    public void insert(String tableName, List<String> values) {
        HashMap<String, Table> databaseTables = database.getDatabaseTables();
        if (databaseTables.containsKey(tableName)) {
            Row row = new Row(databaseTables.get(tableName).getRows().size());
            List<Column> columns = databaseTables.get(tableName).getColumns();
            List<String> columnNames = new ArrayList<>();
            for (Column column : columns) {
                columnNames.add(column.getColumnName());
            }
            for (int i = 0; i < values.size(); i++) {
                row.getRowValue().put(columnNames.get(i), values.get(i));
                databaseTables.get(tableName).getColumns().get(i).getCell().add(new Cell(values.get(i)));
            }
            databaseTables.get(tableName).getRows().add(row);
        }
    }

    public void innerJoin(String table1, int n1, String table2, int n2) {
        HashMap<String, Table> databaseTables = database.getDatabaseTables();
        if (databaseTables.containsKey(table1) && databaseTables.containsKey(table2)) {
            Table table = databaseTables.get(table1);
            Table table3 = databaseTables.get(table2);
            String columnName = table.getColumns().get(n1-1).getColumnName();
            String columnName2 = table3.getColumns().get(n2-1).getColumnName();
            Table result = new Table("Result");
            List<Row> rows = table.getRows();
            List<Row> rows1 = table3.getRows();
            for (Row row : rows) {
                for (Row row1 : rows1) {
                    if (row.getRowValue().containsKey(columnName) && row1.getRowValue().containsKey(columnName2)) {
                        String valueTableOne = row.getRowValue().get(columnName);
                        String valueTableTwo = row1.getRowValue().get(columnName2);
                        if (valueTableOne.equals(valueTableTwo)) {
                            Map<String, String> rowValue = row.getRowValue();
                            Map<String, String> rowValue1 = row1.getRowValue();
                            rowValue.putAll(rowValue1);
                            Row rowwwww = new Row(result.getRows().size());
                            rowwwww.setRowValue(rowValue);
                            result.getRows().add(rowwwww);
                        }
                    }
                }
            }
            for (Row row : result.getRows()) {
                row.printRowValue();
            }
        } else throw new IllegalArgumentException("Tables dont exists!");

    }

    public void rename(String oldName, String newName) {
        HashMap<String, Table> databaseTables = database.getDatabaseTables();
        if (databaseTables.containsKey(oldName)) {
            if (databaseTables.containsKey(newName)) {
                throw new IllegalArgumentException("New name already exists!");
            }
            databaseTables.get(oldName).setTableName(newName);
            databaseTables.put(newName, databaseTables.get(oldName));
            databaseTables.remove(oldName);

        }
    }

    public int count(String tableName, int n, String value) {
        HashMap<String, Table> databaseTables = database.getDatabaseTables();
        int counter = 0;
        if (databaseTables.containsKey(tableName)) {
            Table table = databaseTables.get(tableName);
            for (Cell cell : table.getColumns().get(n-1).getCell()) {
                if (cell.getValue().equals(value)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public void aggregate(String tableName, int n, String searchValue, int targetN, String operation) {
        HashMap<String, Table> databaseTables = database.getDatabaseTables();
        if (databaseTables.containsKey(tableName)) {
            Table table = databaseTables.get(tableName);
            List<Row> rows = table.getRows();
            String columnName = table.getColumns().get(n-1).getColumnName();
            String columnNameTarget = table.getColumns().get(targetN - 1).getColumnName();
            List<String> values = new ArrayList<>();
            for (Row row : rows) {
                if(row.getRowValue().containsKey(columnName) && row.getRowValue().containsKey(columnNameTarget)){
                    if(row.getRowValue().get(columnName).equals(searchValue)) {
                        values.add(row.getRowValue().get(columnNameTarget));
                    }
                }
            }
            String columnType = table.getColumns().get(targetN-1).getColumnType();
            if (columnType.equalsIgnoreCase("String")) {
                throw new IllegalArgumentException("Wrong type!");
            }
            operation = operation.toLowerCase();
            if (columnType.equalsIgnoreCase("Integer")) {
                List<Integer> parsedValues = new ArrayList<>();
                for (String value : values) {
                    parsedValues.add(Integer.parseInt(value));
                }
                operation(parsedValues,operation);

            } else if (columnType.equalsIgnoreCase("Double")) {
                List<Double> parsedValues = new ArrayList<>();
                for (String value : values) {
                    parsedValues.add(Double.parseDouble(value));
                }

                operation(operation, parsedValues);
            }

        }


    }

    private static void operation(List<Integer> parsedValues,String operation) {
        switch (operation) {
            case "sum": {
                int result = 0;
                for (Integer parsedValue : parsedValues) {
                    result += parsedValue;
                }
                System.out.println("Sum is: " + result);
            }
            break;
            case "product": {
                int result = 1;
                for (Integer parsedValue : parsedValues) {
                    result *= parsedValue;
                }
                System.out.println("Product is: " + result);
            }
            break;
            case "maximum": {
                Collections.sort(parsedValues);
                System.out.println("Maximum is: " + parsedValues.get(parsedValues.size() - 1));
            }
            break;
            case "minimum": {
                Collections.sort(parsedValues);
                System.out.println("Minimum is: " + parsedValues.get(0));
            }
            break;
        }
    }

    private static void operation(String operation, List<Double> parsedValues) {
        switch (operation) {
            case "sum": {
                int result = 0;
                for (Double parsedValue : parsedValues) {
                    result += parsedValue;
                }
                System.out.println("Sum is: " + result);
            }
            break;
            case "product": {
                int result = 1;
                for (Double parsedValue : parsedValues) {
                    result *= parsedValue;
                }
                System.out.println("Product is: " + result);
            }
            break;
            case "maximum": {
                Collections.sort(parsedValues);
                System.out.println("Maximum is: " + parsedValues.get(parsedValues.size() - 1));
            }
            break;
            case "minimum": {
                Collections.sort(parsedValues);
                System.out.println("Minimum is: " + parsedValues.get(0));
            }
            break;
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
    public void help(){
        StringBuilder sb=new StringBuilder();
        sb.append("The following commands are supported: \n");
        sb.append("open \"file\"  opens file\n");
        sb.append("close \t closes the currently open file\n");
        sb.append("save \t saves the currently open file\n");
        sb.append("saveas \t save the currently opened file in <file>\n");
        sb.append("help \t prints this information\n");
        sb.append("exit \t exits the program\n");
        System.out.println(sb.toString());
    }
    public void open(String path){
        actionsFile.read(path);
    }

    public ActionsFile getActionsFile() {
        return actionsFile;
    }
}
