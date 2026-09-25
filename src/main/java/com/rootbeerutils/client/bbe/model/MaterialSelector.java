/*
 * Derived from BetterBlockEntities (LGPL-3.0). See com.rootbeerutils.client.bbe.BBE for details.
 */
package com.rootbeerutils.client.bbe.model;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SpriteMapper;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.CopperChestBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class MaterialSelector {

    private MaterialSelector() {
    }

    private static final ConcurrentHashMap<Identifier, SpriteId> BANNER_MATERIALS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Identifier, SpriteId> DECORATED_POT_MATERIALS = new ConcurrentHashMap<>();

    public static SpriteId getBannerMaterial(Holder<BannerPattern> holder) {
        Identifier id = holder.value().assetId();
        SpriteMapper mapper = Sheets.BANNER_MAPPER;
        return BANNER_MATERIALS.computeIfAbsent(id, mapper::apply);
    }

    public static SpriteId getDPSideMaterial(Optional<ItemStackTemplate> optional) {
        if (optional.isPresent()) {
            Holder<DecoratedPotPattern> pattern = optional.get().get(DataComponents.PROVIDES_POTTERY_PATTERN);
            if (pattern != null) {
                Identifier assetId = pattern.value().assetId();
                return DECORATED_POT_MATERIALS.computeIfAbsent(
                        assetId, Sheets.DECORATED_POT_MAPPER::apply);
            }
        }

        return Sheets.DECORATED_POT_SIDE;
    }

    public static ChestRenderState.ChestMaterialType getChestMaterial(BlockEntity blockEntity, boolean christmas) {
        if (blockEntity instanceof EnderChestBlockEntity) {
            return ChestRenderState.ChestMaterialType.ENDER_CHEST;
        }

        if (christmas) {
            return ChestRenderState.ChestMaterialType.CHRISTMAS;
        }

        if (blockEntity instanceof TrappedChestBlockEntity) {
            return ChestRenderState.ChestMaterialType.TRAPPED;
        }

        if (blockEntity.getBlockState().getBlock() instanceof CopperChestBlock copperChestBlock) {
            return switch (copperChestBlock.getState()) {
                case UNAFFECTED -> ChestRenderState.ChestMaterialType.COPPER_UNAFFECTED;
                case EXPOSED -> ChestRenderState.ChestMaterialType.COPPER_EXPOSED;
                case WEATHERED -> ChestRenderState.ChestMaterialType.COPPER_WEATHERED;
                case OXIDIZED -> ChestRenderState.ChestMaterialType.COPPER_OXIDIZED;
            };
        }

        return ChestRenderState.ChestMaterialType.REGULAR;
    }
}
