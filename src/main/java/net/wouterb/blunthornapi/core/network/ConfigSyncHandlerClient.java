package net.wouterb.blunthornapi.core.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.wouterb.blunthornapi.api.config.BlunthornConfig;
import net.wouterb.blunthornapi.api.config.ConfigManager;

import static net.wouterb.blunthornapi.core.network.ConfigSyncHandler.PACKET_NAMESPACE;

@Environment(EnvType.CLIENT)
public class ConfigSyncHandlerClient {
    public static void registerConfigPacket(String configId) {
        Identifier identifier = new Identifier(PACKET_NAMESPACE, configId);
        ClientPlayNetworking.registerGlobalReceiver(identifier, ConfigSyncHandlerClient::onConfigReceived);
    }

    private static void onConfigReceived(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String configId = buf.readString();
        String fieldName = buf.readString();
        byte type = buf.readByte();

        System.out.println("Received config: " + configId);
        System.out.println("field: " + fieldName);

        Object value = switch (type) {
            case ConfigSyncHandler.DataType.BOOL -> buf.readBoolean();
            case ConfigSyncHandler.DataType.INT -> buf.readInt();
            case ConfigSyncHandler.DataType.FLOAT -> buf.readFloat();
            case ConfigSyncHandler.DataType.STRING -> buf.readString();
            default -> null;
        };

        BlunthornConfig config = ConfigManager.getConfig(configId);
        if (config != null) {
            config.setFieldValue(fieldName, value);
        }
    }
}
