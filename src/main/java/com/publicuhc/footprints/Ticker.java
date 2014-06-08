package com.publicuhc.footprints;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Ticker extends BukkitRunnable {

    public static final float REGULAR_OFFSET = 1.05F;
    public static final float SNOW_OFFSET = 1.13F;

    private final PacketContainer m_defaultPacket;

    private final HashMap<UUID, List<Footprint>> m_footprints = new HashMap<UUID, List<Footprint>>();
    private final ProtocolManager m_protocol;

    private final double m_minDistBetweenSquared;
    private final int m_ticksToLast;
    private final double m_maxRenderDistanceSquared;

    public Ticker(ProtocolManager protocolManager, double minDistBetween, int ticksToLast, double maxRenderDistance) {
        m_protocol = protocolManager;
        m_defaultPacket = m_protocol.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
        m_minDistBetweenSquared = minDistBetween * minDistBetween;
        m_ticksToLast = ticksToLast;
        m_maxRenderDistanceSquared = maxRenderDistance * maxRenderDistance;
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            //only run for noncreative with the permission
            if (!p.hasPermission(FootprintsPlugin.LEAVE_FOOTPRINT_PERMISSION) || p.getGameMode() == GameMode.CREATIVE) {
                continue;
            }

            //get the block below the player or if they're jumping the block below that
            Block block = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
            if (block.getType() == Material.AIR) {
                block = block.getRelative(BlockFace.DOWN);
            }

            Location playerLocation = p.getLocation();
            //if the block is air or water we dont want to leave a footprint
            //also check if the footprint isn't too close to another from the player
            if (block.getType() != Material.AIR && block.getType() != Material.WATER && isClearOfFootprints(playerLocation, m_minDistBetweenSquared, p.getUniqueId())) {
                playerLocation.setY(block.getLocation().getY() + (block.getType() == Material.SNOW ? SNOW_OFFSET : REGULAR_OFFSET));

                Footprint newFootstep = new Footprint(playerLocation, m_ticksToLast);
                List<Footprint> playerPrints = m_footprints.get(p.getUniqueId());
                if( null == playerPrints ) {
                    playerPrints = new ArrayList<Footprint>();
                    m_footprints.put(p.getUniqueId(), playerPrints);
                }
                playerPrints.add(newFootstep);
                sendFootstep(newFootstep);
            }
        }

        for(Map.Entry<UUID, List<Footprint>> entry : m_footprints.entrySet()) {
            //send all the footprints and cleanup as needed
            Iterator<Footprint> iterator = entry.getValue().iterator();
            while (iterator.hasNext()) {
                Footprint footstep = iterator.next();
                footstep.decrementTimeRemaining();
                if (footstep.getTimeRemaining() <= 0) {
                    iterator.remove();
                } else {
                    sendFootstep(footstep);
                }
            }
        }
    }

    /**
     * Checks for footprints within the distance of the location for the player
     * @param loc Location
     * @param distanceSquared int
     * @param playerID the player ID
     * @return boolean true if clear, false if not
     */
    private boolean isClearOfFootprints(Location loc, double distanceSquared, UUID playerID) {
        List<Footprint> footprints = m_footprints.get(playerID);
        if(null == footprints) {
            return true;
        }
        for (Footprint footstep : footprints) {
            if(footstep.getLocation().getWorld().equals(loc.getWorld())) {
                continue;
            }
            if (footstep.getLocation().distanceSquared(loc) < distanceSquared) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sends the footstep to nearby players
     * @param footstep the footstep to send
     */
    private void sendFootstep(Footprint footstep){
        m_defaultPacket.getStrings().write(0, "footstep");
        Location loc = footstep.getLocation();
        m_defaultPacket.getFloat()
                .write(0, (float) loc.getX())//x
                .write(1, (float) loc.getY())//y
                .write(2, (float) loc.getZ())
                .write(3, 0.0F)//offsetx
                .write(4, 0.0F)//offsety
                .write(5, 0.0F)//offsetz
                .write(6, 1.0F);
        m_defaultPacket.getIntegers().write(0, 1);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(p.getLocation().getWorld().equals(loc.getWorld())) {
                continue;
            }
            try {
                if (p.getLocation().distanceSquared(loc) < m_maxRenderDistanceSquared) {
                    m_protocol.sendServerPacket(p, m_defaultPacket);
                }
            } catch (InvocationTargetException ignored) {}
        }
    }
}
