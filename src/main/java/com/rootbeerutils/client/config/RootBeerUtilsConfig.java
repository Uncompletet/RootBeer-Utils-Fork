package com.rootbeerutils.client.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;

public final class RootBeerUtilsConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("rootbeerutils/config");
    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();

    public final CrosshairSettings crosshair = new CrosshairSettings();
    private File file;

    public static RootBeerUtilsConfig load(File file, File legacyFile) {
        RootBeerUtilsConfig config = read(file);
        if (config == null && legacyFile.exists()) {
            config = read(legacyFile);
        }
        if (config == null) {
            config = new RootBeerUtilsConfig();
        }

        config.file = file;
        config.writeChanges();
        return config;
    }

    private static RootBeerUtilsConfig read(File file) {
        if (!file.exists()) {
            return null;
        }

        try (var reader = Files.newBufferedReader(file.toPath())) {
            return GSON.fromJson(reader, RootBeerUtilsConfig.class);
        } catch (Exception exception) {
            LOGGER.error("Could not read RootBeer-Utils config {}", file, exception);
            return null;
        }
    }

    public void writeChanges() {
        try {
            File parent = file.getParentFile();
            if (parent != null) {
                Files.createDirectories(parent.toPath());
            }
            try (var writer = Files.newBufferedWriter(file.toPath())) {
                GSON.toJson(this, writer);
            }
        } catch (IOException exception) {
            LOGGER.error("Could not save RootBeer-Utils config {}", file, exception);
            throw new IllegalStateException("Could not save RootBeer-Utils config", exception);
        }
    }

    public static final class CrosshairSettings {
        public volatile boolean indicator = true;
    }
}
