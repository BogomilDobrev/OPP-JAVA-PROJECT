package bg.tu_varna.sit.a1.f22621623;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Table table=new Table("klienti");
        Database database = new Database("purva");
        TableFunctions tableFunctions = new TableFunctions(new ActionsFile(), database, database.getDatabaseName());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter something");
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("exit")) {
                break;
            }
            String[] split = command.split("\\s+");
            split[0] = split[0].toLowerCase();
            switch (split[0]) {
                case "open":
                    tableFunctions.importTables(split[1]);
                    break;
                case "close":
                    tableFunctions.getActionsFile().close();
                    break;
                case "save":
                    tableFunctions.getActionsFile().write();
                    break;
                case "saveas":
                    tableFunctions.getActionsFile().write(split[1]);
                    break;
                case "help":
                    tableFunctions.help();
                    break;
                case "showtables":
                    tableFunctions.showTables();
                    break;
                case "describe":
                    tableFunctions.describe(split[1]);
                    break;
                case "import":
                    tableFunctions.importTables(split[1]);
                    break;
                case "print":
                    tableFunctions.print(split[1]);
                    break;
                case "export":
                    tableFunctions.export(split[1], split[2]);
                    break;
                case "select":
                    tableFunctions.select(Integer.parseInt(split[1]), split[2], split[3]);
                    break;
                case "addcolumn":
                    tableFunctions.addColumn(split[1], split[2], split[3]);
                    break;
                case "update":
                    tableFunctions.update(split[1], Integer.parseInt(split[2]), split[3], Integer.parseInt(split[4]), split[5]);
                    break;
                case "delete":
                    tableFunctions.delete(split[1], Integer.parseInt(split[2]), split[3]);
                    break;
                case "insert": {
                    List<String> strings = new ArrayList<>(List.of(split));
                    strings.remove(0);
                    strings.remove(0);
                    tableFunctions.insert(split[1], strings);
                }
                break;
                case "innerjoin":
                    tableFunctions.innerJoin(split[1], Integer.parseInt(split[2]), split[3], Integer.parseInt(split[4]));
                    break;
                case "rename":
                    tableFunctions.rename(split[1], split[2]);
                    break;
                case "count":
                    tableFunctions.count(split[1], Integer.parseInt(split[2]), split[3]);
                    break;
                case "aggregate":
                    tableFunctions.aggregate(split[1], Integer.parseInt(split[2]), split[3], Integer.parseInt(split[4]), split[5]);
                    break;
            }
        }
    }
}

