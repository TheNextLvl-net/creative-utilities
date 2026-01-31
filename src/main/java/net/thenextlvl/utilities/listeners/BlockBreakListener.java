package net.thenextlvl.utilities.listeners;

import net.thenextlvl.utilities.utils.Settings;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static org.bukkit.block.data.type.Slab.Type.BOTTOM;
import static org.bukkit.block.data.type.Slab.Type.TOP;

public final class BlockBreakListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSlabBreak(final BlockBreakEvent event) {
        if (!Settings.get(event.getPlayer(), Settings.SLAB_PART_BREAKING)) return;
        if (!(event.getBlock().getBlockData() instanceof final Slab slab)) return;
        if (!slab.getType().equals(Slab.Type.DOUBLE)) return;
        slab.setType(isTopHalf(event.getPlayer()) ? BOTTOM : TOP);
        event.getBlock().setBlockData(slab, false);
        event.setCancelled(true);
    }

    private boolean isTopHalf(final Player player) {
        final var range = player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE);
        final var result = player.rayTraceBlocks(range != null ? range.getValue() : 6);
        if (result == null || result.getHitBlockFace() == null) return false;
        if (result.getHitBlockFace().equals(BlockFace.DOWN)) return false;
        if (result.getHitBlockFace().equals(BlockFace.UP)) return true;
        return Math.round(result.getHitPosition().getY()) > result.getHitPosition().getY();
    }
}
