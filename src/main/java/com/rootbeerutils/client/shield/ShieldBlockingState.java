package com.rootbeerutils.client.shield;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class ShieldBlockingState {
    private static final int BLOCKING_DELAY_TICKS = 5;
    private static final int SHIELD_USE_DURATION = 72000;
    private static final Map<UUID, Integer> SHIELD_USE_TICKS = new HashMap<>();

    private ShieldBlockingState() {
    }

    public static void tickRemotePlayers() {
        if (!ShieldBlockingConfig.isEnabled()) {
            SHIELD_USE_TICKS.clear();
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            SHIELD_USE_TICKS.clear();
            return;
        }

        Set<UUID> activePlayers = new HashSet<>();
        for (Player player : minecraft.level.players()) {
            if (player == minecraft.player) {
                continue;
            }

            UUID uuid = player.getUUID();
            ItemStack usedItem = player.getItemInHand(player.getUsedItemHand());
            if (!player.isUsingItem() || !usedItem.is(Items.SHIELD)) {
                SHIELD_USE_TICKS.remove(uuid);
                continue;
            }

            activePlayers.add(uuid);
            int useTicks = SHIELD_USE_TICKS.merge(
                    uuid,
                    1,
                    (ticks, ignored) -> Math.min(ticks + 1, SHIELD_USE_DURATION - 1)
            );
            LivingEntityShieldUseAccessor shieldUse = (LivingEntityShieldUseAccessor) player;
            shieldUse.rootbeerUtils$setActiveItem(usedItem);
            shieldUse.rootbeerUtils$setItemUseTime(SHIELD_USE_DURATION - useTicks);
        }

        SHIELD_USE_TICKS.keySet().retainAll(activePlayers);
    }

    public static boolean isBlocking(Player player) {
        return ShieldBlockingConfig.isEnabled()
                && (!ShieldBlockingConfig.factorsInShieldDelay()
                || SHIELD_USE_TICKS.getOrDefault(player.getUUID(), 0) >= BLOCKING_DELAY_TICKS);
    }
}
