package net.wouterb.blunthornapi.core.mixin.screen;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.wouterb.blunthornapi.core.util.ItemLogger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public class HandledScreenMixin extends Screen {

    @Shadow
    protected Slot focusedSlot;

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!ItemLogger.logItemKeyBind.matchesKey(keyCode, scanCode)) return;
        if (this.focusedSlot == null) return;
        if (!this.focusedSlot.hasStack()) return;
        if (this.client == null) return;
        if (this.client.player == null) return;
        if (!this.client.player.isCreative()) return;

        String itemId = Registries.ITEM.getId(this.focusedSlot.getStack().getItem()).toString();
        ItemLogger.sendClickableMessage(this.client.player, "Logging ID: ", itemId);
        ItemLogger.logItemId(this.client, itemId);
    }
}
