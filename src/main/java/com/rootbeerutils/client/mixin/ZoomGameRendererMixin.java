package com.rootbeerutils.client.mixin;

import com.rootbeerutils.client.zoom.ZoomClient;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class ZoomGameRendererMixin {

    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void rbutils$hideHandWhileZooming(CameraRenderState cameraState,
                                              PlayerRenderState playerState,
                                              GpuTextureView lightmap,
                                              CallbackInfo ci) {
        if (!ZoomClient.isZoomActive()) {
            return;
        }

        ci.cancel();
    }
}
