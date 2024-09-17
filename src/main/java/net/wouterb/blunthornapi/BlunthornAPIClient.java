package net.wouterb.blunthornapi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.wouterb.blunthornapi.api.config.ConfigManager;
import net.wouterb.blunthornapi.core.network.ConfigSyncHandler;
import net.wouterb.blunthornapi.core.network.PermissionSyncHandler;
import net.wouterb.blunthornapi.core.util.ItemLogger;

public class BlunthornAPIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlunthornAPI.LOGGER.info("Starting BlunthornAPI Client");

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> PermissionSyncHandler.onUpdateReceived(client.player, null));

        for (String configId : ConfigManager.getConfigIds()) {
            BlunthornAPI.LOGGER.info("Loading config with ID: {}", configId);
            ConfigSyncHandler.registerConfigPacket(configId);
        }

        ItemLogger.register();
    }
}
