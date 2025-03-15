package io.daxcess.spectatorchat.mixin;

import de.maxhenkel.voicechat.configbuilder.entry.AbstractConfigEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractConfigEntry.class)
public abstract class AbstractConfigEntryMixin<T> {

    @Shadow
    protected String key;

    @Inject(method = "get", at = @At("HEAD"), cancellable = true, remap = false)
    private void onGet(CallbackInfoReturnable<T> cir) {
        if (key.equals("enable_groups")) {
            cir.setReturnValue((T)Boolean.FALSE);
            cir.cancel();
        }
    }
}