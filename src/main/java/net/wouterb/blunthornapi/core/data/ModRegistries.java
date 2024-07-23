package net.wouterb.blunthornapi.core.data;

import net.wouterb.blunthornapi.api.data.IPersistentPlayerData;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ModRegistries {
    private static Dictionary<String, IPersistentPlayerData> registeredMods = new Hashtable<>();

    public static void registerPermissionMod(String mod_id, IPersistentPlayerData modPersistentData) {
        registeredMods.put(mod_id, modPersistentData);
    }

    public static IPersistentPlayerData getModPersistentData(String modId) {
        return registeredMods.get(modId);
    }

    public static List<String> getRegisteredModIds() {
        return Collections.list(registeredMods.keys());
    }
}
