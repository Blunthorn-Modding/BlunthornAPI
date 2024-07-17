package net.wouterb.blunthornapi.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.wouterb.blunthornapi.api.context.ItemActionContext;
import net.wouterb.blunthornapi.api.permission.LockType;

import static net.wouterb.blunthornapi.api.Util.updateInventory;

public interface ItemUseEvent {
    /**
     * Called when the player places a block.
     */
    Event<ItemUseEvent> EVENT = EventFactory.createArrayBacked(ItemUseEvent.class,
            (listeners) -> (itemActionContext) -> {
                for (ItemUseEvent listener : listeners) {
                    ActionResult result = listener.interact(itemActionContext);

                    if (result != ActionResult.PASS) {
                        updateInventory(itemActionContext.getServerPlayer());
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

    /**
     * Calling this method triggers the event.
     * @param context the context of the action
     * @return the ActionResult of the event.
     */
    static ActionResult emit(ItemUsageContext context) {
        ItemActionContext itemActionContext = new ItemActionContext(context.getWorld(), context.getPlayer(), context.getHand(), context.getStack(), LockType.ITEM_USAGE);
        return EVENT.invoker().interact(itemActionContext);
    }
}
