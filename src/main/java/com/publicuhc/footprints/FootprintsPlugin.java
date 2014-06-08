package com.publicuhc.footprints;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class FootprintsPlugin extends JavaPlugin {

    public static final String LEAVE_FOOTPRINT_PERMISSION = "Footprint.leavePrint";

    @Override
    public void onEnable() {
        FileConfiguration configuration = getConfig();
        configuration.options().copyDefaults(true);
        saveConfig();

        double minDistBetween = configuration.getDouble("minDistBetween");
        double maxRenderDist = configuration.getDouble("maxRenderDist");
        int tickTime = configuration.getInt("tickTime");
        int ticksToLast = configuration.getInt("ticksToLast");

        Ticker ticker = new Ticker(
                ProtocolLibrary.getProtocolManager(),
                minDistBetween,
                ticksToLast,
                maxRenderDist
        );

        ticker.runTaskTimer(this, 0, tickTime);
    }
}
