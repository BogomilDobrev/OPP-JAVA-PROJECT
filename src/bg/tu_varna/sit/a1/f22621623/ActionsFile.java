package bg.tu_varna.sit.a1.f22621623;

import javax.swing.plaf.synth.SynthSeparatorUI;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ActionsFile {
    private final String FILE_PATH = "Tables.txt";
    private HashMap<String,List< List<String>>> rows= new LinkedHashMap<>();
    private String tableName;
    public Map<String,List<List<String>>> read() {
        try(FileReader fileReader = new FileReader(FILE_PATH)){
            Scanner scanner = new Scanner(fileReader);
            while (scanner.hasNext()){
                String line = scanner.nextLine();
                String[] tokens = line.split("\\s+");
                if(tokens.length == 1){
                    tableName = tokens[0];
                    rows.putIfAbsent(tokens[0],new ArrayList<>());
                }else{
                    List<String> row = new ArrayList<>(Arrays.asList(tokens));
                    rows.get(tableName).add(row);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return rows;
    }
    public void write(){
        try(FileWriter fileWriter = new FileWriter(FILE_PATH,true)){
            for (Map.Entry<String, List<List<String>>> m : rows.entrySet()) {
                fileWriter.write(m.getKey());
                fileWriter.write(System.lineSeparator());
                for (List<String> strings : m.getValue()) {
                    for (String string : strings) {
                        fileWriter.write(string);
                        fileWriter.write(" ");
                    }
                    fileWriter.write(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
