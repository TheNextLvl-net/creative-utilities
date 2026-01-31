package net.thenextlvl.utilities.listeners;

import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.WeakHashMap;

public class AirPlacingListener implements Listener {
    static final Map<Player, Location> targetBlocks = new WeakHashMap<>();
    private final BlockData blockData = Material.BARRIER.createBlockData();
    private final BlockData waterlogged = Material.BARRIER.createBlockData(data ->
            ((Waterlogged) data).setWaterlogged(true));
    private final UtilitiesPlugin plugin;

    public AirPlacingListener(final UtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        targetBlocks.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(final PlayerMoveEvent event) {
        final var player = event.getPlayer();

        if (!plugin.settingsController().isAirPlacing(player)) {
            hideBlock(player);
            return;
        }

        if (player.getInventory().getItemInMainHand().isEmpty()
                && player.getInventory().getItemInOffHand().isEmpty()) {
            hideBlock(player);
            return;
        }

        final var range = player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE);
        if (range == null || player.getTargetBlockExact((int) range.getValue()) != null) {
            hideBlock(player);
            return;
        }

        final var eyeLocation = player.getEyeLocation();
        final var targetBlock = eyeLocation.add(eyeLocation.getDirection().multiply(range.getValue()));

        final var lastTarget = targetBlocks.put(player, targetBlock);

        final var isWater = targetBlock.getBlock().getType().equals(Material.WATER);
        if ((!isWater && !targetBlock.getBlock().isEmpty()) || targetBlock.equals(lastTarget)) return;

        if (lastTarget != null) player.sendBlockChange(lastTarget, lastTarget.getBlock().getBlockData());

        if (event.getFrom().distanceSquared(event.getTo()) > 0.5) return;

        player.sendBlockChange(targetBlock, isWater ? waterlogged : blockData);
    }

    private void hideBlock(final Player player) {
        targetBlocks.computeIfPresent(player, (entry, location) -> {
            entry.sendBlockChange(location, location.getBlock().getBlockData());
            return null;
        });
    }
}
