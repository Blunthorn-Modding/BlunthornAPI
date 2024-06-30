package net.wouterb.blunthornapi.core.mixin.drop;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.wouterb.blunthornapi.api.context.EntityActionContext;
import net.wouterb.blunthornapi.api.context.ItemActionContext;
import net.wouterb.blunthornapi.api.event.EntityItemDropEvent;
import net.wouterb.blunthornapi.api.event.ItemUseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnreachableCode")
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "drop", at=@At("HEAD"), cancellable = true)
    public void drop(DamageSource source, CallbackInfo ci) {
        Entity sourceAttacker = source.getAttacker();
        Entity entity = (LivingEntity) (Object) this;

        if (sourceAttacker instanceof PlayerEntity player){
            EntityActionContext entityActionContext = new EntityActionContext(player.getWorld(), player, entity);
            ActionResult result = EntityItemDropEvent.emit(entityActionContext);
            if (result == ActionResult.FAIL)
                ci.cancel();
        }
    }

    @Inject(method = "tryUseTotem", at=@At("HEAD"), cancellable = true)
    private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> ci) {
        if (((Object)this) instanceof PlayerEntity player) {
            ItemActionContext itemActionContext = new ItemActionContext(player.getWorld(), player, Items.TOTEM_OF_UNDYING.getDefaultStack());
            ActionResult result = ItemUseEvent.emit(itemActionContext);
            if (result == ActionResult.FAIL)
                ci.setReturnValue(false);
        }
    }
}
