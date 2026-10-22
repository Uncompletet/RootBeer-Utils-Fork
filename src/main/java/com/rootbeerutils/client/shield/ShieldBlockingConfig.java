package com.rootbeerutils.client.shield;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ShieldBlockingConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("rootbeerutils/shield");
    private static final String FILE_NAME = "rootbeerutils-shield.properties";
    private static final String KEY_ENABLED = "enabled";
    private static final String KEY_FACTOR_DELAY = "factor_delay";

    private static boolean enabled = true;
    private static boolean factorDelay = true;
    private static boolean loaded;

    private ShieldBlockingConfig() {
    }

    public static synchronized boolean isEnabled() {
        load();
        return enabled;
    }

    public static synchronized void setEnabled(boolean value) {
        load();
        enabled = value;
        save();
    }

    public static synchronized boolean factorsInShieldDelay() {
        load();
        return factorDelay;
    }

    public static synchronized void setFactorDelay(boolean value) {
        load();
        factorDelay = value;
        save();
    }

    private static void load() {
        if (loaded) {
            return;
        }

        Path path = configPath();
        if (!Files.exists(path)) {
            loaded = true;
            return;
        }

        Properties properties = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            properties.load(reader);
        } catch (IOException exception) {
            LOGGER.error("Failed to read {}", path, exception);
            loaded = true;
            return;
        }

        String value = properties.getProperty(KEY_ENABLED);
        if (value != null) {
            enabled = Boolean.parseBoolean(value);
        }
        String delayValue = properties.getProperty(KEY_FACTOR_DELAY);
        if (delayValue != null) {
            factorDelay = Boolean.parseBoolean(delayValue);
        }
        loaded = true;
    }

    private static void save() {
        Path path = configPath();
        Properties properties = new Properties();
        properties.setProperty(KEY_ENABLED, Boolean.toString(enabled));
        properties.setProperty(KEY_FACTOR_DELAY, Boolean.toString(factorDelay));
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                properties.store(writer, "RootBeer-Utils shield blocking settings");
            }
        } catch (IOException exception) {
            LOGGER.error("Failed to write {}", path, exception);
        }
    }

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
    }
}
