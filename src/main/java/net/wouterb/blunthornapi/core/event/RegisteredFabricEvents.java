package net.wouterb.blunthornapi.core.event;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.wouterb.blunthornapi.api.context.BlockActionContext;
import net.wouterb.blunthornapi.api.context.EntityActionContext;
import net.wouterb.blunthornapi.api.context.ItemActionContext;
import net.wouterb.blunthornapi.api.event.BlockBreakEvent;
import net.wouterb.blunthornapi.api.event.BlockUseEvent;
import net.wouterb.blunthornapi.api.event.EntityUseEvent;
import net.wouterb.blunthornapi.api.event.ItemUseEvent;
import org.jetbrains.annotations.Nullable;

import static net.wouterb.blunthornapi.api.Util.getBlockId;


/**
 * Class that redirects built-in FabricAPI events to the BlunthornAPI events. If desired, the FabricAPI events can
 * still be used for the same purpose, if one wants to implement their own permission system.
 */
public class RegisteredFabricEvents {
    public static ActionResult onBlockAttack(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
        BlockActionContext blockActionContext = new BlockActionContext(world, player, pos, getBlockId(world, pos));
        return BlockBreakEvent.emit(BlockBreakEvent.ATTACK, blockActionContext);
    }

    public static boolean onBlockBreakBefore(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        BlockActionContext blockActionContext = new BlockActionContext(world, playerEntity, blockPos, getBlockId(blockState));
        return BlockBreakEvent.emit(BlockBreakEvent.BEFORE, blockActionContext) != ActionResult.FAIL;
    }

    public static void onBlockBreakAfter(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        BlockActionContext blockActionContext = new BlockActionContext(world, playerEntity, blockPos, getBlockId(blockState));
        BlockBreakEvent.emit(BlockBreakEvent.AFTER, blockActionContext);
    }

    public static TypedActionResult<ItemStack> onUseItem(PlayerEntity player, World world, Hand hand) {
        ItemActionContext itemActionContext = new ItemActionContext(world, player, hand);
        return new TypedActionResult<>(ItemUseEvent.emit(itemActionContext), player.getStackInHand(hand));
    }

    public static ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos blockPos = hitResult.getBlockPos();
        BlockActionContext blockActionContext = new BlockActionContext(world, player, blockPos, getBlockId(world, blockPos));
        return BlockUseEvent.emit(blockActionContext);
    }

    public static ActionResult onAttackEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        ItemActionContext itemActionContext = new ItemActionContext(world, player, hand);
        return ItemUseEvent.emit(itemActionContext);
    }

    public static ActionResult onUseEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        EntityActionContext entityActionContext = new EntityActionContext(world, player, entity);
        return EntityUseEvent.emit(entityActionContext);
    }
}
