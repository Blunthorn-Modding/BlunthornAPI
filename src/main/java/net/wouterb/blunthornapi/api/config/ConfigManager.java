package net.wouterb.blunthornapi.api.config;

import net.wouterb.blunthornapi.core.network.ConfigSyncHandler;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class ConfigManager {
    private static Dictionary<String, BlunthornConfig> modConfigs = new Hashtable<>();

    public static void registerConfig(String modId, BlunthornConfig config) {
        String configId = modId + "_" + config.getConfigName();

        modConfigs.put(configId, config);

        ConfigSyncHandler.registerConfigPacket(configId);
    }

    public static Enumeration<BlunthornConfig> getAllConfigs() {
        return modConfigs.elements();
    }

    public static BlunthornConfig getConfig(String configId) {
        return modConfigs.get(configId);
    }
}
