package net.wouterb.blunthornapi.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;
import net.wouterb.blunthornapi.api.context.BlockActionContext;

public interface BlockPlacementEvent {
    /**
     * Called when the player places a block.
     */
    Event<BlockPlacementEvent> EVENT = EventFactory.createArrayBacked(BlockPlacementEvent.class,
            (listeners) -> (blockActionContext) -> {
                if (blockActionContext.getPlayer().isSpectator()) return ActionResult.PASS;

                for (BlockPlacementEvent listener : listeners) {
                    ActionResult result = listener.interact(blockActionContext);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(BlockActionContext blockActionContext);

    static ActionResult emit(BlockActionContext blockActionContext) {
        return EVENT.invoker().interact(blockActionContext);
    }
}

