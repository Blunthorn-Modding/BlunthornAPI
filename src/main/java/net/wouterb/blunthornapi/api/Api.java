package net.wouterb.blunthornapi.api;

import net.wouterb.blunthornapi.api.config.BlunthornConfig;
import net.wouterb.blunthornapi.api.config.ConfigManager;
import net.wouterb.blunthornapi.api.data.IPersistentPlayerData;
import net.wouterb.blunthornapi.core.data.ModRegistries;

public class Api {

    public static void registerMod(String mod_id, IPersistentPlayerData modPersistentData) {
        ModRegistries.registerMod(mod_id, modPersistentData);
    }

    public static void registerConfig(BlunthornConfig config) {
        ConfigManager.registerConfig(config);
    }
}
