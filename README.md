footprints-plugin
=================

A (experimental) Bukkit plugin that leaves footprints behind players.

## Permissions

Footprint.leavePrint - allows the user to leave footprints, default true

## Configuration

    minDistBetween: 2         # Minimum distance between 2 footprints of the same player
    maxRenderDist: 50         # Maximum distance to send footprints to a player
    tickTime: 10              # Server ticks between footprint ticks
    ticksToLast: 120          # Number of footprint ticks to last

NOTE: The footprint particle decays over time so a tickTime set too high might end up weird