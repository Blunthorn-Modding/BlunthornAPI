package net.wouterb.blunthornapi;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.wouterb.blunthornapi.api.Util;
import net.wouterb.blunthornapi.api.config.BlunthornConfig;
import net.wouterb.blunthornapi.api.config.ConfigManager;
import net.wouterb.blunthornapi.api.data.IPersistentPlayerData;
import net.wouterb.blunthornapi.api.event.*;
import net.wouterb.blunthornapi.api.permission.Permission;
import net.wouterb.blunthornapi.core.ConfigTest;
import net.wouterb.blunthornapi.core.ModTest;
import net.wouterb.blunthornapi.core.data.IEntityDataSaver;
import net.wouterb.blunthornapi.core.data.ModRegistries;
import net.wouterb.blunthornapi.core.event.RegisteredFabricEvents;
import net.wouterb.blunthornapi.core.network.ConfigSyncHandler;
import net.wouterb.blunthornapi.core.network.PermissionSyncHandler;
import net.wouterb.blunthornapi.core.util.ClientServerLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static net.wouterb.blunthornapi.core.data.ModRegistries.registerMod;

public class BlunthornAPI implements ModInitializer {
	public static final String MOD_ID = "blunthornapi";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Starting Blunthorn API");
		registerFabricEvents();
	}

	private static void registerFabricEvents() {
		AttackBlockCallback.EVENT.register(RegisteredFabricEvents::onBlockAttack);
		PlayerBlockBreakEvents.BEFORE.register(RegisteredFabricEvents::onBlockBreakBefore);
		PlayerBlockBreakEvents.AFTER.register(RegisteredFabricEvents::onBlockBreakAfter);
		UseItemCallback.EVENT.register(RegisteredFabricEvents::onUseItem);
		UseBlockCallback.EVENT.register(RegisteredFabricEvents::onUseBlock);
		AttackEntityCallback.EVENT.register(RegisteredFabricEvents::onAttackEntity);
		UseEntityCallback.EVENT.register(RegisteredFabricEvents::onUseEntity);

		ServerPlayConnectionEvents.JOIN.register(BlunthornAPI::onPlayerJoin);
		ServerPlayerEvents.AFTER_RESPAWN.register(BlunthornAPI::onPlayerRespawn);
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(BlunthornAPI::onPlayerWorldChange);
	}

	private static void onPlayerWorldChange(ServerPlayerEntity serverPlayer, ServerWorld origin, ServerWorld destination) {
		MinecraftServer server = serverPlayer.getServer();
		if (server == null) return;

		if (server.isSingleplayer() || server.isDedicated()) {
			Util.updateAllClientPermissions(serverPlayer);
			Util.updateAllClientConfigs(serverPlayer);
		}
	}

	private static void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server){
		ServerPlayerEntity player = handler.getPlayer();
		for (String modId : ModRegistries.getRegisteredModIds()) {
			NbtCompound data = ((IEntityDataSaver) player).blunthornapi$getPersistentData(modId);
			if (data.isEmpty()) {
				LOGGER.info("Player without {} permission data joined, assigning default values...", modId);
				IPersistentPlayerData modPersistentData = ModRegistries.getModPersistentData(modId);
				((IEntityDataSaver) player).blunthornapi$setDefaultValues(modPersistentData);
			}

			if (server.isSingleplayer() || server.isDedicated()) {
				Util.updateAllClientPermissions(player);
				Util.updateAllClientConfigs(player);
			}

		}
	}

	private static void onPlayerRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		List<String> modIds = ModRegistries.getRegisteredModIds();
		IEntityDataSaver oldDataSaver = (IEntityDataSaver) oldPlayer;
		IEntityDataSaver newDataSaver = (IEntityDataSaver) newPlayer;

		for (String modId : modIds) {
			NbtCompound oldNbt = oldDataSaver.blunthornapi$getPersistentData(modId);
			newDataSaver.blunthornapi$addPersistentData(modId, oldNbt);
			PermissionSyncHandler.updateAllClientPermissions(newPlayer);
		}
	}
}