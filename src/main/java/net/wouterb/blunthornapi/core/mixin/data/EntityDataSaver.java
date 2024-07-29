package net.wouterb.blunthornapi.core.mixin.data;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.wouterb.blunthornapi.api.data.IPersistentPlayerData;
import net.wouterb.blunthornapi.api.permission.LockType;
import net.wouterb.blunthornapi.core.data.IEntityDataSaver;
import net.wouterb.blunthornapi.core.data.ModRegistries;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

@Mixin(Entity.class)
public class EntityDataSaver implements IEntityDataSaver {
    @Unique
    private Dictionary<String, NbtCompound> persistentPlayerData = new Hashtable<>();


    @Override
    public NbtCompound blunthornapi$getPersistentData(@NotNull String mod_id) {
        NbtCompound data = persistentPlayerData.get(mod_id);
        if (data == null)
            data = new NbtCompound();
        return data;
    }

    @Override
    public void blunthornapi$addPersistentData(@NotNull String modId, NbtCompound data) {
        NbtCompound nbt = persistentPlayerData.get(modId);
        if (nbt != null) {
            data = mergePersistentData(modId, nbt, data);
        }

        persistentPlayerData.put(modId, data);
    }

    @Override
    public void blunthornapi$setDefaultValues(IPersistentPlayerData persistentModData) {
        blunthornapi$addPersistentData(persistentModData.getTargetModId(), persistentModData.getDefaultValues());
    }

    @Unique
    private NbtCompound mergePersistentData(String modId, NbtCompound oldData, NbtCompound newData) {
        NbtCompound result = new NbtCompound();

        for (LockType lockType : LockType.values()) {
            String lockId = lockType.toString();
            NbtList oldList = oldData.getList(lockId, NbtElement.STRING_TYPE);
            NbtList newList = newData.getList(lockId, NbtElement.STRING_TYPE);

            for (NbtElement element : newList) {
                if (!oldList.contains(element))
                    oldList.add(element);
            }
            result.put(lockId, oldList);
        }
        return result;
    }

    @Override
    public void blunthornapi$setEmptyValues(@NotNull String mod_id) {
        NbtCompound emptyData = new NbtCompound();
        for (LockType lockType : LockType.values()) {
            NbtList nbtList = new NbtList();
            emptyData.put(lockType.toString(), nbtList);
        }
        persistentPlayerData.put(mod_id, emptyData);
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void writeNbt(NbtCompound nbt, CallbackInfoReturnable<Boolean> info) {
        Enumeration<String> keys = persistentPlayerData.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            NbtCompound data = persistentPlayerData.get(key);
            nbt.put(key, data);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void readNbt(NbtCompound nbt, CallbackInfo info) {
        for (String mod_id : ModRegistries.getRegisteredModIds()) {
            if (nbt.contains(mod_id)) {
                this.blunthornapi$addPersistentData(mod_id, nbt.getCompound(mod_id));
            }
        }
    }
}
