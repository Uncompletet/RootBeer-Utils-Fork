package com.rootbeerutils.client.modmenu;

import com.rootbeerutils.client.bbe.BBE;
import com.rootbeerutils.client.bbe.config.BBEGameOptions;
import com.rootbeerutils.client.bbe.config.BBEMenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public final class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return null;
        }
        return ModMenuIntegration::createConfigScreen;
    }

    private static Screen createConfigScreen(Screen parent) {
        BBEGameOptions options = BBE.GlobalScope.options;
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("rootbeerutils.modmenu.title"))
                .setSavingRunnable(() -> BBEMenu.applyChanges(options));
        ConfigEntryBuilder entries = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(
                Component.translatable("rootbeerutils.bbe.category.general"));
        addToggle(general, entries, "rootbeerutils.bbe.option.enabled",
                options.optimizations.enabled, false,
                value -> options.optimizations.enabled = value);
        addToggle(general, entries, "rootbeerutils.bbe.option.skip_vanilla",
                options.optimizations.skipVanillaForDedicated, false,
                value -> options.optimizations.skipVanillaForDedicated = value);
        addToggle(general, entries, "rootbeerutils.bbe.option.christmas_chests",
                options.optimizations.christmasChests, false,
                value -> options.optimizations.christmasChests = value);

        ConfigCategory perBlock = builder.getOrCreateCategory(
                Component.translatable("rootbeerutils.bbe.category.per_block"));
        addToggle(perBlock, entries, "rootbeerutils.bbe.option.optimize_chests",
                options.optimizations.optimizeChests, true,
                value -> options.optimizations.optimizeChests = value);
        addToggle(perBlock, entries, "rootbeerutils.bbe.option.optimize_shulkers",
                options.optimizations.optimizeShulkerBoxes, true,
                value -> options.optimizations.optimizeShulkerBoxes = value);
        addToggle(perBlock, entries, "rootbeerutils.bbe.option.optimize_signs",
                options.optimizations.optimizeSigns, true,
                value -> options.optimizations.optimizeSigns = value);
        addToggle(perBlock, entries, "rootbeerutils.bbe.option.optimize_bells",
                options.optimizations.optimizeBells, true,
                value -> options.optimizations.optimizeBells = value);
        addToggle(perBlock, entries, "rootbeerutils.bbe.option.optimize_banners",
                options.optimizations.optimizeBanners, true,
                value -> options.optimizations.optimizeBanners = value);
        addToggle(perBlock, entries, "rootbeerutils.bbe.option.optimize_decorated_pots",
                options.optimizations.optimizeDecoratedPots, true,
                value -> options.optimizations.optimizeDecoratedPots = value);
        addToggle(perBlock, entries, "rootbeerutils.bbe.option.optimize_copper_golem_statues",
                options.optimizations.optimizeCopperGolemStatues, true,
                value -> options.optimizations.optimizeCopperGolemStatues = value);

        ConfigCategory crosshair = builder.getOrCreateCategory(
                Component.translatable("rootbeerutils.crosshair.category"));
        addToggle(crosshair, entries, "rootbeerutils.crosshair.option.indicator",
                options.crosshair.indicator, true,
                value -> options.crosshair.indicator = value);

        return builder.build();
    }

    private static void addToggle(ConfigCategory category,
                                  ConfigEntryBuilder entries,
                                  String translationKey,
                                  boolean value,
                                  boolean defaultValue,
                                  Consumer<Boolean> setter) {
        category.addEntry(entries.startBooleanToggle(Component.translatable(translationKey), value)
                .setDefaultValue(defaultValue)
                .setSaveConsumer(setter)
                .build());
    }
}
