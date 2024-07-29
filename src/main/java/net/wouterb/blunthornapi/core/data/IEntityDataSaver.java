package net.wouterb.blunthornapi.core.data;

import net.minecraft.nbt.NbtCompound;
import net.wouterb.blunthornapi.api.data.IPersistentPlayerData;

public interface IEntityDataSaver {

    NbtCompound blunthornapi$getPersistentData(String mod_id);

    void blunthornapi$addPersistentData(String mod_id, NbtCompound data);

    default void resetPersistentData(String mod_id, IPersistentPlayerData persistentModData, boolean wipe) {
        if (wipe)
            blunthornapi$setEmptyValues(mod_id);
        else
            blunthornapi$setDefaultValues(persistentModData);
    }

    void blunthornapi$setDefaultValues(IPersistentPlayerData persistentModData);

    void blunthornapi$setEmptyValues(String mod_id);
}
