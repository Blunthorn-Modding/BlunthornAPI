package net.wouterb.blunthornapi.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;
import net.wouterb.blunthornapi.api.context.EntityActionContext;

public interface EntityItemDropEvent {
    /**
     * Called when the player interacts with an entity.
     */
    Event<EntityItemDropEvent> EVENT = EventFactory.createArrayBacked(EntityItemDropEvent.class,
            (listeners) -> (entityActionContext) -> {
                if (entityActionContext.getPlayer().isSpectator()) return ActionResult.PASS;

                for (EntityItemDropEvent listener : listeners) {
                    ActionResult result = listener.interact(entityActionContext);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(EntityActionContext entityActionContext);

    /**
     * Calling this method triggers the event.
     * @param entityActionContext the context of the action
     * @return the ActionResult of the event.
     */
    static ActionResult emit(EntityActionContext entityActionContext) {
        return EVENT.invoker().interact(entityActionContext);
    }
}
