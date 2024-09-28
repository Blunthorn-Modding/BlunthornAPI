package net.wouterb.blunthornapi.core.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.wouterb.blunthornapi.BlunthornAPI;
import net.wouterb.blunthornapi.core.data.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;

import static net.wouterb.blunthornapi.core.network.PermissionSyncHandler.PACKET_NAMESPACE;

@Environment(EnvType.CLIENT)
public class PermissionSyncHandlerClient {
    private static Dictionary<String, NbtCompound> storedPersistentData = new Hashtable<>();


    public static void onUpdateReceived(PlayerEntity player, @Nullable PacketByteBuf buf) {
        BlunthornAPI.LOGGER.info("API: Received permission update");
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
        BlunthornAPI.LOGGER.info("Registering permission packet for mod: {}", modId);
        Identifier identifier = new Identifier(PACKET_NAMESPACE, modId);

        ClientPlayNetworking.registerGlobalReceiver(identifier, ((client, handler, buf, responseSender) -> {
            PermissionSyncHandlerClient.onUpdateReceived(client.player, buf);
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
