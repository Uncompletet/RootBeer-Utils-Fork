package com.rootbeerutils.main.customclientbrand;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "customclientbrand")
@Accessors(fluent = true)
@Getter
@Setter
public class CCBConfig implements ConfigData {
    private String customBrand = "Corven Client";
}
