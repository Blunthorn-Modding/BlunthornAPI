package net.wouterb.blunthornapi.core.mixin.data;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.wouterb.blunthornapi.api.data.IPersistentPlayerData;
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
        return persistentPlayerData.get(mod_id);
    }

    @Override
    public void blunthornapi$setPersistentData(@NotNull String mod_id, NbtCompound data) {
        persistentPlayerData.put(mod_id, data);
    }

    @Override
    public void blunthornapi$setDefaultValues(IPersistentPlayerData persistentModData) {
        persistentPlayerData.put(persistentModData.getTargetModId(), persistentModData.getDefaultValues());
    }

    @Override
    public void blunthornapi$setEmptyValues(@NotNull String mod_id, IPersistentPlayerData persistentModData) {
        persistentPlayerData.put(mod_id, persistentModData.getEmptyValues());
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
                blunthornapi$setPersistentData(mod_id, nbt.getCompound(mod_id));
            }
        }
    }
}
