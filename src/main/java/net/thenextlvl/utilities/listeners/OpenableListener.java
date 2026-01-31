package net.thenextlvl.utilities.listeners;

import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.Openable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public final class OpenableListener implements Listener {
    private final UtilitiesPlugin plugin;

    public OpenableListener(final UtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (!plugin.settingsController().isHandOpenable(event.getPlayer())) return;
        if (!EquipmentSlot.HAND.equals(event.getHand())) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (event.getItem() != null && event.getItem().getType().isBlock()) return;
        if (event.getItem() != null && event.getItem().getType().equals(Material.DEBUG_STICK)) return;
        final var block = event.getClickedBlock();
        if (block == null || (!block.getType().equals(Material.IRON_DOOR)
                && !block.getType().equals(Material.IRON_TRAPDOOR))) return;
        if (!(block.getBlockData() instanceof final Openable openable)) return;
        block.getWorld().playSound(block.getLocation(), getSound(openable, block.getType()), 1F, 1F);
        event.setUseItemInHand(Event.Result.DENY);
        openable.setOpen(!openable.isOpen());
        block.setBlockData(openable, false);
        event.getPlayer().swingMainHand();
        event.setCancelled(true);
    }

    private Sound getSound(final Openable openable, final Material material) {
        return openable.isOpen()
                ? (material.equals(Material.IRON_DOOR) ? Sound.BLOCK_IRON_DOOR_CLOSE : Sound.BLOCK_IRON_TRAPDOOR_CLOSE)
                : (material.equals(Material.IRON_DOOR) ? Sound.BLOCK_IRON_DOOR_OPEN : Sound.BLOCK_IRON_TRAPDOOR_OPEN);
    }
}
