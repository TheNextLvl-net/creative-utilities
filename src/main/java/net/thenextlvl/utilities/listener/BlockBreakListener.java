/*
 * Builder's Utilities is a collection of a lot of tiny features that help with building.
 * Copyright (C) Arcaniax-Development
 * Copyright (C) Arcaniax team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.thenextlvl.utilities.listener;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
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

@RequiredArgsConstructor
public class BlockBreakListener implements Listener {
    private final UtilitiesPlugin plugin;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSlabBreak(BlockBreakEvent event) {
        if (!plugin.settingsController().isSlabPartBreaking(event.getPlayer())) return;
        if (!(event.getBlock().getBlockData() instanceof Slab slab)) return;
        if (!slab.getType().equals(Slab.Type.DOUBLE)) return;
        slab.setType(isTopHalf(event.getPlayer()) ? BOTTOM : TOP);
        event.getBlock().setBlockData(slab, false);
        event.setCancelled(true);
    }

    private boolean isTopHalf(Player player) {
        var range = player.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE);
        var result = player.rayTraceBlocks(range != null ? range.getValue() : 6);
        if (result == null || result.getHitBlockFace() == null) return false;
        if (result.getHitBlockFace().equals(BlockFace.DOWN)) return false;
        if (result.getHitBlockFace().equals(BlockFace.UP)) return true;
        return Math.round(result.getHitPosition().getY()) > result.getHitPosition().getY();
    }
}
