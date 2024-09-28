package net.wouterb.blunthornapi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.wouterb.blunthornapi.api.config.BlunthornConfig;
import net.wouterb.blunthornapi.api.config.ConfigManager;
import net.wouterb.blunthornapi.core.network.ConfigSyncHandlerClient;
import net.wouterb.blunthornapi.core.network.PermissionSyncHandlerClient;
import net.wouterb.blunthornapi.core.util.ItemLogger;

import java.util.Collections;

public class BlunthornAPIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlunthornAPI.LOGGER.info("Starting BlunthornAPI Client");

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> PermissionSyncHandlerClient.onUpdateReceived(client.player, null));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            for (BlunthornConfig config : Collections.list(ConfigManager.getAllConfigs())) {
                config.load();
            }
        });

        for (String configId : ConfigManager.getConfigIds()) {
            BlunthornAPI.LOGGER.info("Loading config with ID: {}", configId);
            ConfigSyncHandlerClient.registerConfigPacket(configId);
        }

        ItemLogger.register();
    }
}
