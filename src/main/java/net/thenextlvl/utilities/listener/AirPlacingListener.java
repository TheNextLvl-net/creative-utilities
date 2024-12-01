package net.thenextlvl.utilities.listener;

import lombok.RequiredArgsConstructor;
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
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.WeakHashMap;

@NullMarked
@RequiredArgsConstructor
public class AirPlacingListener implements Listener {
    static final Map<Player, Location> targetBlocks = new WeakHashMap<>();
    private final BlockData blockData = Material.BARRIER.createBlockData();
    private final BlockData waterlogged = Material.BARRIER.createBlockData(data ->
            ((Waterlogged) data).setWaterlogged(true));
    private final UtilitiesPlugin plugin;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        targetBlocks.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        var player = event.getPlayer();

        if (!plugin.settingsController().isAirPlacing(player)) {
            hideBlock(player);
            return;
        }

        if (player.getInventory().getItemInMainHand().isEmpty()
            && player.getInventory().getItemInOffHand().isEmpty()) {
            hideBlock(player);
            return;
        }

        var range = player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE);
        if (range == null || player.getTargetBlockExact((int) range.getValue()) != null) {
            hideBlock(player);
            return;
        }

        var eyeLocation = player.getEyeLocation();
        var targetBlock = eyeLocation.add(eyeLocation.getDirection().multiply(range.getValue()));

        var lastTarget = targetBlocks.put(player, targetBlock);

        var isWater = targetBlock.getBlock().getType().equals(Material.WATER);
        if ((!isWater && !targetBlock.getBlock().isEmpty()) || targetBlock.equals(lastTarget)) return;

        if (lastTarget != null) player.sendBlockChange(lastTarget, lastTarget.getBlock().getBlockData());

        if (event.getFrom().distanceSquared(event.getTo()) > 0.5) return;

        player.sendBlockChange(targetBlock, isWater ? waterlogged : blockData);
    }

    private void hideBlock(Player player) {
        targetBlocks.computeIfPresent(player, (entry, location) -> {
            entry.sendBlockChange(location, location.getBlock().getBlockData());
            return null;
        });
    }
}
