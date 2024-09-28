package net.wouterb.blunthornapi.core.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.wouterb.blunthornapi.core.data.IEntityDataSaver;
import net.wouterb.blunthornapi.core.data.ModRegistries;

import java.util.List;

public class PermissionSyncHandler {
    protected static final String PACKET_NAMESPACE = "blunthorn_permission_sync";
    public static void updateAllClientPermissions(ServerPlayerEntity player) {
        List<String> modIds = ModRegistries.getRegisteredModIds();

        for(String modId : modIds) {
            updateModClientPermissions(player, modId);
        }
    }

    public static void updateModClientPermissions(ServerPlayerEntity player, String modId) {
        IEntityDataSaver dataSaver = (IEntityDataSaver) player;
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbtData = dataSaver.blunthornapi$getPersistentData(modId);
        buf.writeString(modId);
        buf.writeNbt(nbtData);
        ServerPlayNetworking.send(player, new Identifier(PACKET_NAMESPACE, modId), buf);
    }
}
