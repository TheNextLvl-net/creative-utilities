package net.thenextlvl.utilities.listener;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class PlayerInteractListener implements Listener {
    private final UtilitiesPlugin plugin;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDragonEggTeleport(BlockFromToEvent event) {
        if (!plugin.config().preventDragonEggTeleport()) return;
        if (!event.getBlock().getType().equals(Material.DRAGON_EGG)) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.config().disableSoilTrample()) return;
        if (!event.getAction().equals(Action.PHYSICAL)) return;
        var block = event.getClickedBlock();
        event.setCancelled(block != null && block.getType().equals(Material.FARMLAND));
    }
}
