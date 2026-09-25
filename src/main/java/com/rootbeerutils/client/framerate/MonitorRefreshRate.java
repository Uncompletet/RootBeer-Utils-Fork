package com.rootbeerutils.client.framerate;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

public final class MonitorRefreshRate {

    private static final int FALLBACK_RATE = 60;

    private MonitorRefreshRate() {
    }

    /**
     * Returns the refresh rate (Hz) of the monitor currently containing the Minecraft window.
     */
    public static int forActiveWindow() {
        Window window = Minecraft.getInstance().getWindow();
        Monitor monitor = window.findBestMonitor();
        if (monitor == null) {
            return FALLBACK_RATE;
        }

        VideoMode mode = monitor.currentMode();
        if (mode == null) {
            return FALLBACK_RATE;
        }

        int rate = Math.round(mode.getRefreshRate());
        return rate > 0 ? rate : FALLBACK_RATE;
    }
}
