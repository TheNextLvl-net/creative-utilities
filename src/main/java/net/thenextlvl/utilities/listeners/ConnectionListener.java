package net.thenextlvl.utilities.listeners;

import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public final class ConnectionListener implements Listener {
    private final UtilitiesPlugin plugin;

    public ConnectionListener(final UtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        plugin.settingsController().setHandOpenable(event.getPlayer(), true);
        plugin.settingsController().setSlabPartBreaking(event.getPlayer(), true);
        Optional.ofNullable(event.getPlayer().getAttribute(Attribute.ATTACK_SPEED)).ifPresent(attribute -> {
            final var value = plugin.config().fixAttackSpeed() ? 1024 : attribute.getDefaultValue();
            attribute.setBaseValue(value);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        AdvancedFlyListener.lastVelocity.remove(event.getPlayer());
        AdvancedFlyListener.slower1.remove(event.getPlayer());
        AdvancedFlyListener.slower2.remove(event.getPlayer());
        AirPlacingListener.targetBlocks.remove(event.getPlayer());
        plugin.settingsController().purge(event.getPlayer());
    }
}
