package net.wouterb.blunthornapi;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.util.ActionResult;
import net.wouterb.blunthornapi.api.event.*;
import net.wouterb.blunthornapi.core.event.RegisteredFabricEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlunthornAPI implements ModInitializer {
	public static final String MOD_ID = "blunthornapi";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Starting Blunthorn API");
		registerFabricEvents();
		registerTestEvents();
	}

	private static void registerFabricEvents() {
		AttackBlockCallback.EVENT.register(RegisteredFabricEvents::onBlockAttack);
		PlayerBlockBreakEvents.BEFORE.register(RegisteredFabricEvents::onBlockBreakBefore);
		PlayerBlockBreakEvents.AFTER.register(RegisteredFabricEvents::onBlockBreakAfter);
		UseItemCallback.EVENT.register(RegisteredFabricEvents::onUseItem);
		UseBlockCallback.EVENT.register(RegisteredFabricEvents::onUseBlock);
		AttackEntityCallback.EVENT.register(RegisteredFabricEvents::onAttackEntity);
		UseEntityCallback.EVENT.register(RegisteredFabricEvents::onUseEntity);
	}

	private static void registerTestEvents() {
		BlockBreakEvent.ATTACK.register(blockActionContext -> {
			LOGGER.info("Attack block event!");
			return ActionResult.PASS;
		});

		BlockBreakEvent.BEFORE.register(blockActionContext -> {
			LOGGER.info("Before block break event!");
			return ActionResult.PASS;
		});

		BlockBreakEvent.AFTER.register(blockActionContext -> {
			LOGGER.info("After block break event!");
			return ActionResult.PASS;
		});

		BlockPlacementEvent.EVENT.register(blockActionContext -> {
			LOGGER.info("Block placement event!");
			return ActionResult.PASS;
		});

		BlockUseEvent.EVENT.register(blockActionContext -> {
			LOGGER.info("Block use event!");
			return ActionResult.PASS;
		});

		ItemUseEvent.EVENT.register(itemActionContext -> {
			LOGGER.info("Item use event!");
			return ActionResult.FAIL;
		});

		EntityUseEvent.EVENT.register(entityActionContext -> {
			LOGGER.info("Entity use event!");
			return ActionResult.PASS;
		});

		ObjectCraftedEvent.EVENT.register(itemActionContext -> {
			LOGGER.info("Object crafted event!");
			return ActionResult.PASS;
		});

		EntityItemDropEvent.EVENT.register(entityActionContext -> {
			LOGGER.info("Entity item drop event!");
			return ActionResult.FAIL;
		});

	}
}