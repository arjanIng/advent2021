package advent.advent2022.util;

import java.util.HashMap;
import java.util.Map;

public class BiMap<K, V> extends HashMap<K, V> {
    public Map<V, K> reverse() {
        Map<V, K> reverse = new HashMap<>();
        for(Map.Entry<K, V> entry : this.entrySet()){
            reverse.put(entry.getValue(), entry.getKey());
        }
        return reverse;
    }
}
