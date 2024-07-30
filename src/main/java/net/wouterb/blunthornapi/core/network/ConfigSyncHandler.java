package net.wouterb.blunthornapi.core.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.wouterb.blunthornapi.BlunthornAPI;
import net.wouterb.blunthornapi.api.config.BlunthornConfig;
import net.wouterb.blunthornapi.api.config.ConfigManager;
import net.wouterb.blunthornapi.api.config.StoreInConfig;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class ConfigSyncHandler {
    private static final String PACKET_NAMESPACE = "blunthorn_config_sync";

    public static class DataType {
        public static final int BOOL = 0;
        public static final int INT = 1;
        public static final int FLOAT = 2;
        public static final int STRING = 3;

    }

    public static void updateAllClientConfigs(ServerPlayerEntity player) {
        List<String> configIds = ConfigManager.getConfigIds();
        for (String configId : configIds) {
            updateModClientConfigs(player, configId);
        }
    }

    public static void updateModClientConfigs(ServerPlayerEntity serverPlayer, String configId) {
        BlunthornConfig config = ConfigManager.getConfig(configId);
        Identifier identifier = new Identifier(PACKET_NAMESPACE, configId);
        Field[] fields = config.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (!field.isAnnotationPresent(StoreInConfig.class)) continue;

                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeString(field.getName());

                Object value = field.get(config);

                if (value instanceof String) {
                    buf.writeByte(DataType.STRING);
                    buf.writeString(value.toString());
                } else if (value instanceof Float) {
                    buf.writeByte(DataType.FLOAT);
                    buf.writeFloat(Float.parseFloat(value.toString()));
                } else if (value instanceof Integer) {
                    buf.writeByte(DataType.INT);
                    buf.writeInt(Integer.parseInt(value.toString()));
                } else if (value instanceof Boolean) {
                    buf.writeByte(DataType.BOOL);
                    buf.writeBoolean(Boolean.parseBoolean(value.toString()));
                }

                ServerPlayNetworking.send(serverPlayer, identifier, buf);
            } catch (IllegalAccessException e) {
                BlunthornAPI.LOGGER.error(e.toString());
            }
        }
    }


    public static void registerConfigPacket(String configId) {
        Identifier identifier = new Identifier(PACKET_NAMESPACE, configId);
        ClientPlayNetworking.registerGlobalReceiver(identifier, ((client, handler, buf, responseSender) -> {
            String fieldName = buf.readString();
            byte type = buf.readByte();

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
        }));
    }
}
