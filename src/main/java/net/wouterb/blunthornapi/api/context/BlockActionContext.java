package net.wouterb.blunthornapi.api.context;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockActionContext {
    private final World world;
    private final PlayerEntity player;
    private final BlockPos blockPos;
    private final String blockId;

    private final boolean isClient;

    public BlockActionContext(World world, PlayerEntity player, BlockPos blockPos, String blockId) {
        this.world = world;
        this.player = player;
        this.blockPos = blockPos;
        this.blockId = blockId;

        isClient = world.isClient();
    }

    /**
     * @return the world the player currently is in.
     */
    public World getWorld() {
        return world;
    }


    /**
     * @return the ServerWorld of the current player. Returns null if ran on the client.
     */
    public ServerWorld getServerWorld() {
        if (!isClient) return (ServerWorld) world;
        return null;
    }

    /**
     * @return the current player.
     */
    public PlayerEntity getPlayer() {
        return player;
    }

    /**
     * @return the ServerPlayerEntity of the current player. Returns null if ran on the client.
     */
    public ServerPlayerEntity getServerPlayer() {
        if (!isClient) return (ServerPlayerEntity) player;
        return null;
    }

    /**
     * @return the block position of the action performed.
     */
    public BlockPos getBlockPos() {
        return blockPos;
    }

    /**
     * @return the ID of the block in question.
     */
    public String getBlockId() {
        return blockId;
    }

    /**
     * @return whether the code is being run on the client or not.
     */
    public boolean isClient() {
        return isClient;
    }
}
