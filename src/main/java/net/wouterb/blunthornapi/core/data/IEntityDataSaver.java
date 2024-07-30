package net.wouterb.blunthornapi.core.data;

import net.minecraft.nbt.NbtCompound;
import net.wouterb.blunthornapi.api.data.IPersistentPlayerData;

public interface IEntityDataSaver {

    NbtCompound blunthornapi$getPersistentData(String mod_id);

    void blunthornapi$addPersistentData(String modId, NbtCompound data);

    void blunthornapi$setPersistentData(String modId, NbtCompound data);

    default void resetPersistentData(String mod_id, boolean wipe) {
        IPersistentPlayerData persistentModData = ModRegistries.getModPersistentData(mod_id);

        if (wipe)
            blunthornapi$setEmptyValues(mod_id);
        else
            blunthornapi$setDefaultValues(persistentModData);
    }

    void blunthornapi$setDefaultValues(IPersistentPlayerData persistentModData);

    void blunthornapi$setEmptyValues(String mod_id);
}
