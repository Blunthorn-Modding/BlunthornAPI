package net.wouterb.blunthornapi.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;
import net.wouterb.blunthornapi.api.context.BlockActionContext;

public interface BlockUseEvent {
    /**
     * Called when the player interacts with a block.
     */
    Event<BlockUseEvent> EVENT = EventFactory.createArrayBacked(BlockUseEvent.class,
            (listeners) -> (blockActionContext) -> {
                if (blockActionContext.getPlayer().isSpectator()) return ActionResult.PASS;

                for (BlockUseEvent listener : listeners) {
                    ActionResult result = listener.interact(blockActionContext);

                    if (result != ActionResult.PASS) {
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
