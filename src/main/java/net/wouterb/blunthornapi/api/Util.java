package net.wouterb.blunthornapi.api;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Utility class. These methods you may need often, so use these to simplify your code.
 */
public class Util {

    /**
     * @param world the world to check in.
     * @param blockPos the position to check.
     * @return the BlockState at a given position in a world.
     */
    public static BlockState getBlockState(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos);
    }

    /**
     * @param world the world the block is in.
     * @param blockPos the position of the block.
     * @return the string ID of the block at the given position.
     */
    public static String getBlockId(World world, BlockPos blockPos) {
        return getBlockId(getBlockState(world, blockPos));
    }

    /**
     * @param blockState the BlockState to get the ID from
     * @return the string ID of the given BlockState.
     */
    public static String getBlockId(BlockState blockState) {
        return Registries.BLOCK.getId(blockState.getBlock()).toString();
    }

    /**
     * @param playerEntity the player of which the inventory needs to be updated.
     *                     Will only run on the server.
     * @see Util#updateInventory(ServerPlayerEntity)
     */
    public static void updateInventory(PlayerEntity playerEntity) {
        if (playerEntity.getWorld().isClient) return;

        ServerPlayerEntity player = (ServerPlayerEntity) playerEntity;
        updateInventory(player);
    }

    /**
     * Updates the inventory of a certain player. Use when cancelling actions that affect the player's inventory,
     * such as placing blocks.
     * @param player the player of which the inventory needs to be updated.
     *               Useful.
     */
    public static void updateInventory(ServerPlayerEntity player) {
        if (player == null) return;

        ScreenHandler screenHandler = player.currentScreenHandler;

        DefaultedList<ItemStack> updatedStacks = DefaultedList.ofSize(screenHandler.slots.size(), ItemStack.EMPTY);
        for (int i = 0; i < updatedStacks.size(); i++) {
            updatedStacks.set(i, screenHandler.getSlot(i).getStack());
        }

        InventoryS2CPacket inventoryUpdatePacket = new InventoryS2CPacket(screenHandler.syncId, screenHandler.nextRevision(), updatedStacks, ItemStack.EMPTY);
        player.networkHandler.sendPacket(inventoryUpdatePacket);
    }
}
