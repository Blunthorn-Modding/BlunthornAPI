package net.wouterb.blunthornapi.core.mixin.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends Entity {

    @Unique
    private static final TagKey<Block> NO_TELEPORT_BLOCKS_TAG = TagKey.of(Registries.BLOCK.getKey(), new Identifier("c", "no_enderman_teleport"));

    public EndermanEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @SuppressWarnings("UnreachableCode")
    @Inject(method="teleportTo(DDD)Z", at = @At("HEAD"), cancellable = true)
    public void onTeleport(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        EndermanEntity enderman = (EndermanEntity) (Object) this;
        World world = enderman.getWorld();

        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
        while (mutable.getY() > world.getBottomY() && !world.getBlockState(mutable).blocksMovement()) {
            mutable.move(Direction.DOWN);
        }

        BlockState blockState = world.getBlockState(mutable);
        if (blockState.isIn(NO_TELEPORT_BLOCKS_TAG)) {
            cir.setReturnValue(false);
        }
    }
}
