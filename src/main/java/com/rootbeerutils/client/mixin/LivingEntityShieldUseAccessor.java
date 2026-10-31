package com.rootbeerutils.client.mixin;

import net.minecraft.world.item.ItemStack;

public interface LivingEntityShieldUseAccessor {
    void rootbeerUtils$setActiveItem(ItemStack stack);

    void rootbeerUtils$setItemUseTime(int time);
}
