package net.thenextlvl.utilities.listener;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class BlockPhysicsListener implements Listener {
    private final UtilitiesPlugin plugin;

    @EventHandler
    public void onPhysics(BlockPhysicsEvent event) {
        if (event.getSourceBlock().isEmpty() && event.getChangedType().isEmpty()) {
            if (event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.GRASS_BLOCK)) {
                return;
            }
        }
        if (event.getSourceBlock().getType().equals(Material.SNOW)
            || event.getSourceBlock().getType().equals(Material.POWDER_SNOW)
            || event.getSourceBlock().getType().equals(Material.SNOW_BLOCK)) {
            if (event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.GRASS_BLOCK)) {
                return;
            }
        }
        if (event.getChangedType().name().toLowerCase().contains("chest") ||
            event.getChangedType().name().toLowerCase().contains("stair") ||
            event.getChangedType().name().toLowerCase().contains("fence") ||
            event.getChangedType().name().toLowerCase().contains("pane") ||
            event.getChangedType().name().toLowerCase().contains("wall") ||
            event.getChangedType().name().toLowerCase().contains("bar") ||
            event.getChangedType().name().toLowerCase().contains("door")) {
            return;
        }
        if (!plugin.config().disableRedstone()) {
            if (event.getChangedType().name().toLowerCase().contains("redstone") ||
                event.getChangedType().name().toLowerCase().contains("daylight") ||
                event.getChangedType().name().toLowerCase().contains("diode") ||
                event.getChangedType().name().toLowerCase().contains("note") ||
                event.getChangedType().name().toLowerCase().contains("lever") ||
                event.getChangedType().name().toLowerCase().contains("button") ||
                event.getChangedType().name().toLowerCase().contains("command") ||
                event.getChangedType().name().toLowerCase().contains("tripwire") ||
                event.getChangedType().name().toLowerCase().contains("plate") ||
                event.getChangedType().name().toLowerCase().contains("string") ||
                event.getChangedType().name().toLowerCase().contains("piston") ||
                event.getChangedType().name().toLowerCase().contains("observer")) {
                if (!event.getBlock().isEmpty()) return;
            }
        }

        event.setCancelled(event.getChangedType().hasGravity()
                ? plugin.config().disableGravity()
                : plugin.config().disablePhysics());
    }
}
