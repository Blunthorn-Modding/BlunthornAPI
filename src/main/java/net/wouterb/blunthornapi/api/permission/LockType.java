package net.wouterb.blunthornapi.api.permission;

public enum LockType {
    BREAKING,
    PLACEMENT,
    BLOCK_INTERACTION,
    ENTITY_INTERACTION,
    ENTITY_DROP,
    ITEM_USAGE,
    CRAFTING_RECIPE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
