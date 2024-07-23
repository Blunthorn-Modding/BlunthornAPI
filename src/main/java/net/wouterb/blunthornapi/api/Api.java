package net.wouterb.blunthornapi.api;

import net.wouterb.blunthornapi.api.data.IPersistentPlayerData;
import net.wouterb.blunthornapi.core.data.ModRegistries;

public class Api {

    public static void registerPermissionMod(String mod_id, IPersistentPlayerData modPersistentData) {
        ModRegistries.registerPermissionMod(mod_id, modPersistentData);
    }
}
