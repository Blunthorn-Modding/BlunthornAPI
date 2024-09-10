package net.wouterb.blunthornapi.core;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.wouterb.blunthornapi.api.data.IPersistentPlayerData;
import net.wouterb.blunthornapi.api.permission.LockType;

public class ModTest implements IPersistentPlayerData {
    @Override
    public NbtCompound getDefaultValues() {
        String[] lockedValues = {"minecraft:elytra", "minecraft:diamond_chestplate"};
        NbtCompound nbtData = new NbtCompound();
        NbtList nbtList = new NbtList();
        for (String id : lockedValues) {
            nbtList.add(NbtString.of(id));
        }
        nbtData.put(LockType.ITEM_USAGE.toString(), nbtList);
        return nbtData;
    }

    @Override
    public String getTargetModId() {
        return "test";
    }
}
