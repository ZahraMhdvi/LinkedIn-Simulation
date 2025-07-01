package Panel;

import DataStructures.Table.Table;

import java.util.ArrayList;
import java.util.Map;

public class DynamicTable<K extends Comparable<K>, T> extends Table<K, T> {
    private Map<String, DataType> columnDataTypes;
    private int nextId;

    public DynamicTable(String tableName, Map<String, DataType> columnDataTypes) {
        super(tableName, new ArrayList<>(columnDataTypes.keySet()));
        this.columnDataTypes = columnDataTypes;
        this.nextId = 1;
    }

    public int getNextId() {
        return nextId++;
    }

    public Map<String, DataType> getColumnDataTypes() {
        return columnDataTypes;
    }

    public void insert(K key, T value) {
        super.insert(key, value);
    }

    public void delete(K key) {
        super.delete(key);
    }

    public void displayTable() {
        super.displayTable();
    }
}
