package net.wouterb.blunthornapi.core.event;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.wouterb.blunthornapi.api.context.BlockActionContext;
import net.wouterb.blunthornapi.api.event.BlockBreakEvent;

import static net.wouterb.blunthornapi.api.Util.getBlockId;

public class RegisteredFabricEvents {

    public static ActionResult onBlockAttack(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
        BlockActionContext blockActionContext = new BlockActionContext(world, player, pos, getBlockId(world, pos));
        return BlockBreakEvent.emit(BlockBreakEvent.ATTACK, blockActionContext);
    }

    public static boolean onBlockBreakBefore(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        BlockActionContext blockActionContext = new BlockActionContext(world, playerEntity, blockPos, getBlockId(blockState));
        return BlockBreakEvent.emit(BlockBreakEvent.BEFORE, blockActionContext) == ActionResult.FAIL;
    }

    public static void onBlockBreakAfter(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        BlockActionContext blockActionContext = new BlockActionContext(world, playerEntity, blockPos, getBlockId(blockState));
        BlockBreakEvent.emit(BlockBreakEvent.AFTER, blockActionContext);
    }
}
