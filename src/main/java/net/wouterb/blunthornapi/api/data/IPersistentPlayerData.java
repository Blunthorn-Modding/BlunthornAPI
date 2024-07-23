package net.wouterb.blunthornapi.api.data;

import net.minecraft.nbt.NbtCompound;

public interface IPersistentPlayerData {
    NbtCompound getDefaultValues();
    NbtCompound getEmptyValues();
    String getTargetModId();
}
