package net.wouterb.blunthornapi.api.context;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class EntityActionContext extends ActionContext{
    private final Entity entity;

    private final String entityId;

    public EntityActionContext(World world, PlayerEntity player, Entity entity) {
        super(world, player);
        this.entity = entity;

        this.entityId = EntityType.getId(entity.getType()).toString();
    }

    /**
     * @return the entity with which the interaction took place.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * @return the ID of the entity whith which the interaction took place.
     */
    public String getEntityId() {
        return entityId;
    }
}
