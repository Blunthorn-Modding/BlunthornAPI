package net.wouterb.blunthornapi.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;
import net.wouterb.blunthornapi.api.context.BlockActionContext;

public interface BlockBreakEvent {
    /**
     * Called before a block is broken. Allows for cancelling of the block breaking.
     */
    Event<BlockBreakEvent> BEFORE = EventFactory.createArrayBacked(BlockBreakEvent.class,
            (listeners) -> (blockActionContext) -> doBlockBreakEvent(blockActionContext, listeners));

    /**
     * Called after a block is successfully broken.
     */
    Event<BlockBreakEvent> AFTER = EventFactory.createArrayBacked(BlockBreakEvent.class,
            (listeners) -> (blockActionContext) -> doBlockBreakEvent(blockActionContext, listeners));

    /**
     * Called when the player starts mining a block.
     */
    Event<BlockBreakEvent> ATTACK = EventFactory.createArrayBacked(BlockBreakEvent.class,
            (listeners) -> (blockActionContext) -> doBlockBreakEvent(blockActionContext, listeners));

    private static ActionResult doBlockBreakEvent(BlockActionContext blockActionContext, BlockBreakEvent[] listeners){
        if (blockActionContext.getPlayer().isSpectator()) return ActionResult.PASS;

        for (BlockBreakEvent listener : listeners) {
            ActionResult result = listener.interact(blockActionContext);

            if (result != ActionResult.PASS) {
                return result;
            }
        }
        return ActionResult.PASS;
    }

    ActionResult interact(BlockActionContext blockActionContext);

    /**
     * Calling this method triggers the event.
     * @param blockActionContext the context of the action
     * @return the ActionResult of the event.
     */
    static ActionResult emit(Event<BlockBreakEvent> event, BlockActionContext blockActionContext) {
        return event.invoker().interact(blockActionContext);
    }
}
