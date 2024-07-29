package net.wouterb.blunthornapi;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
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
//		registerTestConfig();
//		setupTestLocks();
//		registerTestEvents();
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
			PermissionSyncHandler.updateClient(player);
			ConfigSyncHandler.updateClient(player);
		}
	}

	private static void onPlayerRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		List<String> modIds = ModRegistries.getRegisteredModIds();

		for (String modId : modIds) {
			NbtCompound oldNbt = ((IEntityDataSaver) oldPlayer).blunthornapi$getPersistentData(modId);
			((IEntityDataSaver) newPlayer).blunthornapi$setPersistentData(modId, oldNbt);
			PermissionSyncHandler.updateClient(newPlayer);
		}
	}

	static BlunthornConfig config;

	public static void registerTestConfig() {
		config = new ConfigTest();
//		System.out.println(((ConfigTest)config).getTesting());
//		System.out.println(((ConfigTest)config).getBooltest());
		ConfigManager.registerConfig(config);
	}

	private static void setupTestLocks() {
		registerMod("test", new ModTest());

//		ServerPlayConnectionEvents.JOIN.register((serverPlayNetworkHandler, sender, server) -> {
//			ServerPlayerEntity player = serverPlayNetworkHandler.getPlayer();
//			((IEntityDataSaver) player).blunthornapi$setPersistentData("test", nbtData);
//		});


	}

	private static void registerTestEvents() {
		BlockBreakEvent.ATTACK.register(blockActionContext -> {
			ClientServerLogger.info("Attack block event!", blockActionContext.isClient());
			if (Permission.isObjectLocked(blockActionContext, "test"))
				return ActionResult.FAIL;
			return ActionResult.PASS;
		});

		BlockBreakEvent.BEFORE.register(blockActionContext -> {
			ClientServerLogger.info("Before block break event!", blockActionContext.isClient());
			if (Permission.isObjectLocked(blockActionContext, "test"))
				return ActionResult.FAIL;
			return ActionResult.PASS;
		});

		BlockBreakEvent.AFTER.register(blockActionContext -> {
			ClientServerLogger.info("After block break event!", blockActionContext.isClient());
			if (Permission.isObjectLocked(blockActionContext, "test"))
				return ActionResult.FAIL;
			return ActionResult.PASS;
		});

		BlockPlaceEvent.EVENT.register(blockActionContext -> {
			ClientServerLogger.info("Block placement event!", blockActionContext.isClient());
			return ActionResult.PASS;
		});

		BlockUseEvent.EVENT.register(blockActionContext -> {
			ClientServerLogger.info("Block use event!", blockActionContext.isClient());
			return ActionResult.PASS;
		});

		ItemUseEvent.EVENT.register(itemActionContext -> {
			ClientServerLogger.info("Item use event!", itemActionContext.isClient());
			return ActionResult.PASS;
		});

		EntityUseEvent.EVENT.register(entityActionContext -> {
			ClientServerLogger.info("Entity use event!", entityActionContext.isClient());
			return ActionResult.PASS;
		});

		ObjectCraftedEvent.EVENT.register(itemActionContext -> {
			ClientServerLogger.info("Object crafted event!", itemActionContext.isClient());
			return ActionResult.PASS;
		});

		EntityItemDropEvent.EVENT.register(entityActionContext -> {
			ClientServerLogger.info("Entity item drop event!", entityActionContext.isClient());
			return ActionResult.PASS;
		});

	}
}