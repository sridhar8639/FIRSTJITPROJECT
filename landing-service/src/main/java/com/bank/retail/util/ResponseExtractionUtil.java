package com.bank.retail.util;

import java.util.Map;

/**
 * Utility class for extracting data from nested Map structures
 * Commonly used for parsing XML responses converted to Map format
 */
public class ResponseExtractionUtil {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> findNestedMapByKey(Map<String, Object> parentMap, String key) {
        if (parentMap == null) return null;
 
        for (Map.Entry<String, Object> entry : parentMap.entrySet()) {
            Object value = entry.getValue();
            if (entry.getKey().equals(key) && value instanceof Map) {
                return (Map<String, Object>) value;
            }
            if (value instanceof Map) {
                Map<String, Object> found = findNestedMapByKey((Map<String, Object>) value, key);
                if (found != null) return found;
            }
        }
        return null;
    }

    /**
     * Extract value from a map that contains nested value structure
     * @param map the map to extract from
     * @param key the key to look for
     * @return the extracted value or null if not found
     */
    @SuppressWarnings("unchecked")
    public static String extractValue(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) {
            return null;
        }
        Object value = map.get(key);
        if (value instanceof Map) {
            Map<String, Object> valueMap = (Map<String, Object>) value;
            Object val = valueMap.get("value");
            return (val instanceof String) ? (String) val : String.valueOf(val);
        } else if (value instanceof String) {
            return (String) value;
        } else {
        	return ""+ value;
        }
    }
}