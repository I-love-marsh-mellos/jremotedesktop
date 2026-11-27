package com.ilmm.jremotedesktop;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
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

    public static String hashPassword(String password) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100_000, 256);
        byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();

        Base64.Encoder enc = Base64.getEncoder();
        return enc.encodeToString(salt) + ":" + enc.encodeToString(hash);
    }

    public static boolean verifyPassword(String password, String stored) throws Exception {
        String[] parts = stored.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100_000, 256);
        byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();

        return MessageDigest.isEqual(hash, expectedHash);
    }
}