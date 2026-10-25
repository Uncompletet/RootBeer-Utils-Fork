package com.rootbeerutils.client;

import com.rootbeerutils.client.config.RootBeerUtilsConfig;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public final class RootBeerUtilsClient implements ClientModInitializer {
    public static RootBeerUtilsConfig CONFIG;

    @Override
    public void onInitializeClient() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        CONFIG = RootBeerUtilsConfig.load(
                new File(configDir, "rootbeer-utils-options.json"),
                new File(configDir, "rootbeer-utils-bbe-options.json")
        );
    }
}
