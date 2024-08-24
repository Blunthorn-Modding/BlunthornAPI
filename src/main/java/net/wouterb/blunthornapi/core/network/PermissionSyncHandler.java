package net.wouterb.blunthornapi.core.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.wouterb.blunthornapi.BlunthornAPI;
import net.wouterb.blunthornapi.core.data.IEntityDataSaver;
import net.wouterb.blunthornapi.core.data.ModRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class PermissionSyncHandler {
    private static final String PACKET_NAMESPACE = "blunthorn_permission_sync";
    private static Dictionary<String, NbtCompound> storedPersistentData = new Hashtable<>();

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

    public static void onUpdateReceived(PlayerEntity player, @Nullable PacketByteBuf buf) {
//        BlunthornAPI.LOGGER.info("API: Received permission update");
        if (player == null){
            // Update came from server, which only sends us the buffer for some reason, so we store it.
            if (buf != null)
                storePersistentData(buf.readString(), buf.readNbt());
            return;
        }
//        BlunthornAPI.LOGGER.info("API: PU - Player exists");

        if (buf == null) {
//            BlunthornAPI.LOGGER.info("API: PU - No buffer");

            for (String modId : Collections.list(storedPersistentData.keys())) {
                setPersistentData(player, modId, storedPersistentData.get(modId));
            }

        } else {
//            BlunthornAPI.LOGGER.info("API: PU - Buffer found");

            String modId = buf.readString();
            NbtCompound persistentData = buf.readNbt();
//            BlunthornAPI.LOGGER.info(persistentData.toString());
            setPersistentData(player, modId, persistentData);
        }

    }

    public static void registerPermissionPacket(String modId) {
//        BlunthornAPI.LOGGER.info("Registering permission packet for mod: {}", modId);
        Identifier identifier = new Identifier(PACKET_NAMESPACE, modId);

        ClientPlayNetworking.registerGlobalReceiver(identifier, ((client, handler, buf, responseSender) -> {
            PermissionSyncHandler.onUpdateReceived(client.player, buf);
        }));
    }

    private static void setPersistentData(PlayerEntity player, String modId, NbtCompound nbt) {
        IEntityDataSaver dataSaver = (IEntityDataSaver) player;
        dataSaver.blunthornapi$setPersistentData(modId, nbt);
    }

    private static void storePersistentData(String modId, NbtCompound data) {
        storedPersistentData.put(modId, data);
    }
}
