package net.wouterb.blunthornapi.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;
import net.wouterb.blunthornapi.api.context.BlockActionContext;

import static net.wouterb.blunthornapi.api.Util.updateInventory;

public interface BlockPlaceEvent {
    /**
     * Called when the player places a block.
     */
    Event<BlockPlaceEvent> EVENT = EventFactory.createArrayBacked(BlockPlaceEvent.class,
            (listeners) -> (blockActionContext) -> {
                if (blockActionContext.getPlayer().isSpectator()) return ActionResult.PASS;

                for (BlockPlaceEvent listener : listeners) {
                    ActionResult result = listener.interact(blockActionContext);

                    if (result != ActionResult.PASS) {
                        updateInventory(blockActionContext.getServerPlayer());
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(BlockActionContext blockActionContext);

    /**
     * Calling this method triggers the event.
     * @param blockActionContext the context of the action
     * @return the ActionResult of the event.
     */
    static ActionResult emit(BlockActionContext blockActionContext) {
        return EVENT.invoker().interact(blockActionContext);
    }
}

