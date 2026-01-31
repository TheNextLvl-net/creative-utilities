package net.thenextlvl.utilities.listeners;

import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public final class WorldListener implements Listener {
    private final UtilitiesPlugin plugin;

    public WorldListener(final UtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockFade(final BlockFadeEvent event) {
        event.setCancelled(plugin.config().disableFading());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(final EntityExplodeEvent event) {
        event.setCancelled(plugin.config().disableExplosions());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLeavesDecay(final LeavesDecayEvent event) {
        event.setCancelled(plugin.config().disableLeavesDecay());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWeatherChange(final WeatherChangeEvent event) {
        event.setCancelled(plugin.config().disableWeatherChanges());
    }
}
