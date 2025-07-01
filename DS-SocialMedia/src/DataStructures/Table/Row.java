package DataStructures.Table;

import java.util.HashMap;
import java.util.Map;

public class Row<T> {
    private Map<String, T> data;

    public Row(Map<String, T> data) {
        this.data = new HashMap<>(data);
    }

    public Map<String, T> getData() {
        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}