Footprints
==========

A (experimental) Bukkit plugin that leaves footprints behind players.

## Example

![Example Image](http://puu.sh/9kN82/9a261da8df.jpg)

## Dependency

Requires [ProtocolLib](http://dev.bukkit.org/bukkit-plugins/protocollib/) to be installed to run. Built against ProtocolLib 3.3.1

## Permissions

Footprint.leavePrint - allows the user to leave footprints, default true

## Configuration

    minDistBetween: 2         # Minimum distance between 2 footprints of the same player
    maxRenderDist: 50         # Maximum distance to send footprints to a player
    tickTime: 10              # Server ticks between footprint ticks
    ticksToLast: 120          # Number of footprint ticks to last

NOTE: The footprint particle decays over time so a tickTime set too high might end up weird