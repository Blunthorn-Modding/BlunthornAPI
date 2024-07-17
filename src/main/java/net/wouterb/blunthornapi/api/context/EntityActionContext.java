package net.wouterb.blunthornapi.api.context;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.wouterb.blunthornapi.api.permission.LockType;

public class EntityActionContext extends ActionContext{
    private final Entity entity;

    private final String entityId;

    public EntityActionContext(World world, PlayerEntity player, Entity entity, LockType lockType) {
        super(world, player, lockType);
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

    @Override
    public boolean isObjectInTag(String tag) {
        Registry<EntityType<?>> registry = Registries.ENTITY_TYPE;
        EntityType<?> entity = registry.getOrEmpty(new Identifier(entityId)).orElse(null);
        if (entity == null) return false;

        TagKey<EntityType<?>> entryTagKey = TagKey.of(registry.getKey(), new Identifier(tag));

        return entity.isIn(entryTagKey);
    }

    @Override
    public String getObjectId() {
        return getEntityId();
    }
}
