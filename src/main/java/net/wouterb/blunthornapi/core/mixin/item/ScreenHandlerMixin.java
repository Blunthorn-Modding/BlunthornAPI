package net.wouterb.blunthornapi.core.mixin.item;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.wouterb.blunthornapi.api.context.ItemActionContext;
import net.wouterb.blunthornapi.api.event.ItemUseEvent;
import net.wouterb.blunthornapi.api.permission.LockType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnreachableCode")
@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Shadow
    private ItemStack cursorStack;

    @Inject(method = "internalOnSlotClick", at = @At("HEAD"), cancellable = true)
    public void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        ScreenHandler screenHandler = (ScreenHandler)(Object) this;
        if (slotIndex > 0){
            ItemStack item = screenHandler.getSlot(slotIndex).getStack();
            if (8 - MobEntity.getPreferredEquipmentSlot(cursorStack).getEntitySlotId() == slotIndex) {
                ItemActionContext context = new ItemActionContext(player.getWorld(), player, item, LockType.ITEM_USAGE);
                ActionResult result = ItemUseEvent.emit(context);
                if (result == ActionResult.FAIL)
                    ci.cancel();
            } else if (actionType == SlotActionType.QUICK_MOVE && MobEntity.getPreferredEquipmentSlot(item).isArmorSlot()) {
                ItemActionContext context = new ItemActionContext(player.getWorld(), player, item, LockType.ITEM_USAGE);
                ActionResult result = ItemUseEvent.emit(context);
                if (result == ActionResult.FAIL)
                    ci.cancel();
            }
        }
    }
}
