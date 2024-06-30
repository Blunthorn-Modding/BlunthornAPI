package net.wouterb.blunthornapi.api.context;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemActionContext extends ActionContext{
    private final Hand hand;
    private final ItemStack itemStack;
    private final String itemId;

    public ItemActionContext(World world, PlayerEntity player, Hand hand) {
        this(world, player, hand, player.getStackInHand(hand));
    }

    public ItemActionContext(World world, PlayerEntity player, ItemStack itemStack) {
        this(world, player, null, itemStack);
    }

    public ItemActionContext(World world, PlayerEntity player, Hand hand, ItemStack itemStack) {
        super(world, player);
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
}
