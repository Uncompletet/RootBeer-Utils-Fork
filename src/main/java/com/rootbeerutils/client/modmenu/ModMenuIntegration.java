package com.rootbeerutils.client.modmenu;

import com.rootbeerutils.client.RootBeerUtilsClient;
import com.rootbeerutils.client.config.RootBeerUtilsConfig;
import com.rootbeerutils.client.shield.ShieldBlockingConfig;

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
        RootBeerUtilsConfig options = RootBeerUtilsClient.CONFIG;
        boolean[] shieldBlockingEnabled = {ShieldBlockingConfig.isEnabled()};
        boolean[] shieldDelayEnabled = {ShieldBlockingConfig.factorsInShieldDelay()};
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("rootbeerutils.modmenu.title"))
                .setSavingRunnable(() -> {
                    options.writeChanges();
                    ShieldBlockingConfig.setEnabled(shieldBlockingEnabled[0]);
                    ShieldBlockingConfig.setFactorDelay(shieldDelayEnabled[0]);
                });
        ConfigEntryBuilder entries = builder.entryBuilder();

        ConfigCategory crosshair = builder.getOrCreateCategory(
                Component.translatable("rootbeerutils.crosshair.category"));
        addToggle(crosshair, entries, "rootbeerutils.crosshair.option.indicator",
                options.crosshair.indicator, true,
                value -> options.crosshair.indicator = value);

        ConfigCategory shield = builder.getOrCreateCategory(
                Component.translatable("rootbeerutils.shield.category"));
        addToggle(shield, entries, "rootbeerutils.shield.option.blocking_fix",
                shieldBlockingEnabled[0], true,
                value -> shieldBlockingEnabled[0] = value);
        addToggle(shield, entries, "rootbeerutils.shield.option.factor_delay",
                shieldDelayEnabled[0], false,
                value -> shieldDelayEnabled[0] = value);

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
