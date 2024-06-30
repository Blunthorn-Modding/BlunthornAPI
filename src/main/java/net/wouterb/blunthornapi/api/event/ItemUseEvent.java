package net.wouterb.blunthornapi.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;
import net.wouterb.blunthornapi.api.context.ItemActionContext;

public interface ItemUseEvent {
    /**
     * Called when the player places a block.
     */
    Event<ItemUseEvent> EVENT = EventFactory.createArrayBacked(ItemUseEvent.class,
            (listeners) -> (itemActionContext) -> {
                for (ItemUseEvent listener : listeners) {
                    ActionResult result = listener.interact(itemActionContext);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(ItemActionContext itemActionContext);

    /**
     * Calling this method triggers the event.
     * @param itemActionContext the context of the action
     * @return the ActionResult of the event.
     */
    static ActionResult emit(ItemActionContext itemActionContext) {
        return EVENT.invoker().interact(itemActionContext);
    }
}
