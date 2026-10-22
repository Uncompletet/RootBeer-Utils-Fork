package com.rootbeerutils.client.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
abstract class LivingEntityShieldUseMixin implements LivingEntityShieldUseAccessor {
    @Shadow
    protected ItemStack useItem;

    @Shadow
    protected int useItemRemaining;

    @Override
    public void rootbeerUtils$setActiveItem(ItemStack stack) {
        this.useItem = stack;
    }

    @Override
    public void rootbeerUtils$setItemUseTime(int time) {
        this.useItemRemaining = time;
    }
}
