package net.wouterb.blunthornapi.api.context;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.wouterb.blunthornapi.api.permission.LockType;

public class BlockActionContext extends ActionContext {
    private final BlockPos blockPos;
    private final String blockId;

    public BlockActionContext(World world, PlayerEntity player, BlockPos blockPos, String blockId, LockType lockType) {
        super(world, player, lockType);

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


    @Override
    public boolean isObjectInTag(String tag) {
        Registry<Block> registry = Registries.BLOCK;
        Block block = registry.getOrEmpty(new Identifier(blockId)).orElse(null);
        if (block == null) return false;

        TagKey<Block> entryTagKey = TagKey.of(registry.getKey(), new Identifier(tag));
        return block.getDefaultState().isIn(entryTagKey);
    }

    @Override
    public String getObjectId() {
        return getBlockId();
    }
}
