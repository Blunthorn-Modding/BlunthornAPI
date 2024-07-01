package net.wouterb.blunthornapi.core.mixin.item;

import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.wouterb.blunthornapi.api.event.ItemUseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci){;
        ActionResult result = ItemUseEvent.emit(context);
        if (result == ActionResult.FAIL)
            ci.setReturnValue(result);
    }
}
