package com.ilmm.jremotedesktop;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {
    public static Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new LinkedHashMap<>();
        if (query == null || query.isBlank()) {
            return params;
        }

        String cleanQuery = query.startsWith("?") ? query.substring(1) : query;

        for (String pair : cleanQuery.split("&")) {
            if (pair.isEmpty()) continue;

            String[] keyValue = pair.split("=", 2);
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "";

            params.put(key, value);
        }
        return params;
    }
}