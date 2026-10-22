package com.rootbeerutils.client.mixin;

import com.rootbeerutils.client.shield.ShieldBlockingState;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
abstract class RemoteShieldBlockingTickMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void rootbeerUtils$syncRemoteShieldUse(CallbackInfo callback) {
        ShieldBlockingState.tickRemotePlayers();
    }
}
