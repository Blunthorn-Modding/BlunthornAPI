package net.wouterb.blunthornapi.core.data;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.wouterb.blunthornapi.api.data.IPersistentPlayerData;
import net.wouterb.blunthornapi.core.network.PermissionSyncHandlerClient;

import java.util.*;

public class ModRegistries {
    private static Dictionary<String, IPersistentPlayerData> registeredMods = new Hashtable<>();

    public static void registerMod(String modId, IPersistentPlayerData modPersistentData) {
        registeredMods.put(modId, modPersistentData);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            PermissionSyncHandlerClient.registerPermissionPacket(modId);
    }

    public static IPersistentPlayerData getModPersistentData(String modId) {
        return registeredMods.get(modId);
    }

    public static List<String> getRegisteredModIds() {
        return Collections.list(registeredMods.keys());
    }
}
