package net.wouterb.blunthornapi.api.context;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockActionContext extends ActionContext {
    private final BlockPos blockPos;
    private final String blockId;

    public BlockActionContext(World world, PlayerEntity player, BlockPos blockPos, String blockId) {
        super(world, player);

        this.blockPos = blockPos;
        this.blockId = blockId;
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
}
