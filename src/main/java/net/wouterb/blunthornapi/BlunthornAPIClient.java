package net.wouterb.blunthornapi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.wouterb.blunthornapi.api.config.ConfigManager;
import net.wouterb.blunthornapi.core.network.ConfigSyncHandler;
import net.wouterb.blunthornapi.core.network.PermissionSyncHandler;

public class BlunthornAPIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlunthornAPI.LOGGER.info("Starting BlunthornAPI Client");

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> PermissionSyncHandler.onUpdateReceived(client.player, null));

        for (String configId : ConfigManager.getConfigIds()) {
            System.out.println(configId);
            ConfigSyncHandler.registerConfigPacket(configId);
        }
    }
}