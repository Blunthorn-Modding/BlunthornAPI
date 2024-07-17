package net.wouterb.blunthornapi.api.context;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.wouterb.blunthornapi.api.permission.LockType;

public class ItemActionContext extends ActionContext{
    private final Hand hand;
    private final ItemStack itemStack;
    private final String itemId;

    public ItemActionContext(World world, PlayerEntity player, Hand hand, LockType lockType) {
        this(world, player, hand, player.getStackInHand(hand), lockType);
    }

    public ItemActionContext(World world, PlayerEntity player, ItemStack itemStack, LockType lockType) {
        this(world, player, null, itemStack, lockType);
    }

    public ItemActionContext(World world, PlayerEntity player, Hand hand, ItemStack itemStack, LockType lockType) {
        super(world, player, lockType);
        this.hand = hand;
        this.itemStack = itemStack;

        this.itemId = Registries.ITEM.getId(itemStack.getItem()).toString();

    }

    /**
     * @return the hand that was used to perform an action.
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * @return the ItemStack that was used to perform this action.
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * @return the ItemId of the item that was used to perform this action.
     */
    public String getItemId() {
        return itemId;
    }

    @Override
    public boolean isObjectInTag(String tag) {
        Registry<Item> registry = Registries.ITEM;
        TagKey<Item> entryTagKey = TagKey.of(registry.getKey(), new Identifier(tag));
        return itemStack.isIn(entryTagKey);
    }

    @Override
    public String getObjectId() {
        return getItemId();
    }
}
