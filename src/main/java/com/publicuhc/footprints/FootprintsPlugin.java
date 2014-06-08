package com.publicuhc.footprints;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class FootprintsPlugin extends JavaPlugin {

    public static final String LEAVE_FOOTPRINT_PERMISSION = "Footprint.leavePrint";

    @Override
    public void onEnable() {
        FileConfiguration configuration = getConfig();
        configuration.options().copyDefaults(true);
        saveConfig();
    }
}
