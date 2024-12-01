package net.thenextlvl.utilities.listener;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class InventoryListener implements Listener {
    private final UtilitiesPlugin plugin;

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent event) {
        var player = (Player) event.getWhoClicked();

        var inventory = plugin.getInventoryManager().getInventory(player).orElse(null);
        if (inventory == null) return;

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            event.setCancelled(true);
        }

        if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            event.setCancelled(true);
            return;
        }

        if (event.getAction() == InventoryAction.NOTHING && event.getClick() != ClickType.MIDDLE) {
            event.setCancelled(true);
        }

        if (event.getClickedInventory() == player.getOpenInventory().getTopInventory()) {
            event.setCancelled(true);

            int row = event.getSlot() / 9;
            int column = event.getSlot() % 9;

            if (row < 0 || column < 0) {
                return;
            }

            if (row >= inventory.getRows() || column >= inventory.getColumns()) {
                return;
            }

            inventory.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryClickEvent.class)
                    .forEach(listener -> ((net.thenextlvl.utilities.gui.inventory.InventoryListener<InventoryClickEvent>) listener).accept(event));

            plugin.getInventoryManager().getContents(player)
                    .flatMap(inventoryContents -> inventoryContents.get(row, column))
                    .ifPresent(item -> item.run(event));

            player.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        var inventory = plugin.getInventoryManager().getInventory(player).orElse(null);
        if (inventory == null) return;

        for (int slot : event.getRawSlots()) {
            if (slot >= player.getOpenInventory().getTopInventory().getSize()) {
                continue;
            }

            event.setCancelled(true);
            break;
        }

        inventory.getListeners().stream()
                .filter(listener -> listener.getType() == InventoryDragEvent.class)
                .forEach(listener -> ((net.thenextlvl.utilities.gui.inventory.InventoryListener<InventoryDragEvent>) listener).accept(event));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        var inventory = plugin.getInventoryManager().getInventory(player).orElse(null);
        if (inventory == null) return;

        inventory.getListeners().stream()
                .filter(listener -> listener.getType() == InventoryOpenEvent.class)
                .forEach(listener -> ((net.thenextlvl.utilities.gui.inventory.InventoryListener<InventoryOpenEvent>) listener).accept(event));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClose(InventoryCloseEvent event) {
        var player = (Player) event.getPlayer();

        var inventory = plugin.getInventoryManager().getInventory(player).orElse(null);
        if (inventory == null) return;

        inventory.getListeners().stream()
                .filter(listener -> listener.getType() == InventoryCloseEvent.class)
                .forEach(listener -> ((net.thenextlvl.utilities.gui.inventory.InventoryListener<InventoryCloseEvent>) listener).accept(event));

        if (inventory.isCloseable()) {
            event.getInventory().clear();

            plugin.getInventoryManager().removeInventory(player);
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(event.getInventory()));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();

        var inventory = plugin.getInventoryManager().getInventory(player).orElse(null);
        if (inventory == null) return;

        inventory.getListeners().stream()
                .filter(listener -> listener.getType() == PlayerQuitEvent.class)
                .forEach(listener -> ((net.thenextlvl.utilities.gui.inventory.InventoryListener<PlayerQuitEvent>) listener).accept(event));

        plugin.getInventoryManager().removeInventory(player);
    }
}
