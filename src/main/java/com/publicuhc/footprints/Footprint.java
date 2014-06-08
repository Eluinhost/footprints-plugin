package com.publicuhc.footprints;

import org.bukkit.Location;

import java.util.UUID;

public class Footprint {

    private final Location m_location;
    private int m_timeRemaining;
    private final UUID m_playerID;

    /**
     * Make a new footprint
     * @param loc the location to display it
     * @param timeToLast the amount of 'footstep ticks' to last
     * @param playerID the player that left the footprint
     */
    public Footprint(Location loc, int timeToLast, UUID playerID) {
        m_playerID = playerID;
        m_location = loc;
        m_timeRemaining = timeToLast;
    }

    /**
     * @return amount of time left
     */
    public int getTimeRemaining() {
        return m_timeRemaining;
    }

    /**
     * lower the amount of time left for the footprint to remain
     */
    public void decrementTimeRemaining() {
        m_timeRemaining -= 1;
    }

    /**
     * @return the location the footprint displays at
     */
    public Location getLocation() {
        return m_location;
    }

    /**
     * @return the name of the player who left the footprint
     */
    public UUID getPlayerID() {
        return m_playerID;
    }
}
