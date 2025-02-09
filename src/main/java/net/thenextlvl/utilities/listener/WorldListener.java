package net.thenextlvl.utilities.listener;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class WorldListener implements Listener {
    private final UtilitiesPlugin plugin;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        event.setCancelled(plugin.config().disableFading());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(plugin.config().disableExplosions());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(plugin.config().disableLeavesDecay());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(plugin.config().disableWeatherChanges());
    }
}
