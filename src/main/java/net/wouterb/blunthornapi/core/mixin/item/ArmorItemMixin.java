package net.wouterb.blunthornapi.core.mixin.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.wouterb.blunthornapi.api.context.ItemActionContext;
import net.wouterb.blunthornapi.api.event.ItemUseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ArmorItem.class)
public class ArmorItemMixin {
    @Inject(method = "dispenseArmor", at=@At("HEAD"), cancellable = true)
    private static void dispenseArmor(BlockPointer pointer, ItemStack armor, CallbackInfoReturnable<Boolean> ci) {
        BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
        List<LivingEntity> list = pointer.getWorld().getEntitiesByClass(LivingEntity.class, new Box(blockPos), EntityPredicates.EXCEPT_SPECTATOR.and(new EntityPredicates.Equipable(armor)));
        if (list.isEmpty()) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)list.get(0);

        if (livingEntity instanceof PlayerEntity player) {
            ItemActionContext context = new ItemActionContext(player.getWorld(), player, armor);
            ActionResult result = ItemUseEvent.emit(context);
            if (result == ActionResult.FAIL)
                ci.cancel();
        }
    }
}
