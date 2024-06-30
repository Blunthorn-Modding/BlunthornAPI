package net.wouterb.blunthornapi.api.context;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public abstract class ActionContext {
    protected final World world;
    protected final PlayerEntity player;

    protected final boolean isClient;

    protected ActionContext(World world, PlayerEntity player) {
        this.world = world;
        this.player = player;

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
     * @return whether the code is being run on the client or not.
     */
    public boolean isClient() {
        return isClient;
    }
}
