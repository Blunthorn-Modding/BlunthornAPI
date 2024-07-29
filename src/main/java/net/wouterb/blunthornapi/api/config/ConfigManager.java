package net.wouterb.blunthornapi.api.config;

import java.util.*;

public class ConfigManager {
    private static Dictionary<String, BlunthornConfig> modConfigs = new Hashtable<>();

    public static void registerConfig(BlunthornConfig config) {
        modConfigs.put(config.getConfigId(), config);
    }

    public static Enumeration<BlunthornConfig> getAllConfigs() {
        return modConfigs.elements();
    }

    public static BlunthornConfig getConfig(String configId) {
        return modConfigs.get(configId);
    }

    public static List<String> getConfigIds() {
        return Collections.list(modConfigs.keys());
    }
}