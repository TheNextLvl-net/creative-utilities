package net.thenextlvl.utilities.listeners;

import net.thenextlvl.utilities.utils.Permissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class TeleportListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTeleport(final PlayerTeleportEvent event) {
        if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) return;
        event.setCancelled(!event.getPlayer().hasPermission(Permissions.TPGM3));
    }
}
