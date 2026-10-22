package com.rootbeerutils.client.mixin;

import com.rootbeerutils.client.shield.ShieldBlockingConfig;
import com.rootbeerutils.client.shield.ShieldBlockingState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AvatarRenderer.class)
abstract class RemoteShieldBlockingMixin {
    @Inject(
            method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void rootbeerUtils$keepRemoteShieldPose(
            Avatar avatar,
            ItemStack itemInHand,
            InteractionHand hand,
            CallbackInfoReturnable<HumanoidModel.ArmPose> callback
    ) {
        if (!ShieldBlockingConfig.isEnabled()
                || avatar == Minecraft.getInstance().player
                || !(avatar instanceof Player player)
                || player.getUsedItemHand() != hand
                || !itemInHand.is(Items.SHIELD)
                || !player.isUsingItem()) {
            return;
        }

        callback.setReturnValue(ShieldBlockingState.isBlocking(player)
                ? HumanoidModel.ArmPose.BLOCK
                : HumanoidModel.ArmPose.ITEM);
    }
}
