package com.example.rakta_seva.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodGroupCompatibility {

    private static final Map<String, List<String>> COMPATIBILITY_MAP = new HashMap<>();

    static {
        COMPATIBILITY_MAP.put("O+", Arrays.asList("O+", "O-"));
        COMPATIBILITY_MAP.put("O-", Arrays.asList("O-"));
        COMPATIBILITY_MAP.put("A+", Arrays.asList("A+", "A-", "O+", "O-"));
        COMPATIBILITY_MAP.put("A-", Arrays.asList("A-", "O-"));
        COMPATIBILITY_MAP.put("B+", Arrays.asList("B+", "B-", "O+", "O-"));
        COMPATIBILITY_MAP.put("B-", Arrays.asList("B-", "O-"));
        COMPATIBILITY_MAP.put("AB+", Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        COMPATIBILITY_MAP.put("AB-", Arrays.asList("A-", "B-", "AB-", "O-"));
    }

    /**
     * Returns a list of compatible donor blood groups for a given recipient group.
     */
    public static List<String> getCompatibleDonors(String recipientGroup) {
        List<String> compatible = COMPATIBILITY_MAP.get(recipientGroup);
        if (compatible != null) {
            return compatible;
        }
        // If unknown group, return all groups
        return Arrays.asList("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-");
    }

    /**
     * Checks if a donor blood group is compatible with a recipient group.
     */
    public static boolean isCompatible(String donorGroup, String recipientGroup) {
        List<String> compatible = getCompatibleDonors(recipientGroup);
        return compatible.contains(donorGroup);
    }
}
