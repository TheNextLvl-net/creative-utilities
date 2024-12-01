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
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class OpenableListener implements Listener {
    private final UtilitiesPlugin plugin;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.settingsController().isHandOpenable(event.getPlayer())) return;
        if (!EquipmentSlot.HAND.equals(event.getHand())) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (event.getItem() != null && event.getItem().getType().isBlock()) return;
        if (event.getItem() != null && event.getItem().getType().equals(Material.DEBUG_STICK)) return;
        var block = event.getClickedBlock();
        if (block == null || (!block.getType().equals(Material.IRON_DOOR)
                              && !block.getType().equals(Material.IRON_TRAPDOOR))) return;
        if (!(block.getBlockData() instanceof Openable openable)) return;
        block.getWorld().playSound(block.getLocation(), getSound(openable, block.getType()), 1F, 1F);
        event.setUseItemInHand(Event.Result.DENY);
        openable.setOpen(!openable.isOpen());
        block.setBlockData(openable, false);
        event.getPlayer().swingMainHand();
        event.setCancelled(true);
    }

    private Sound getSound(Openable openable, Material material) {
        return openable.isOpen()
                ? (material.equals(Material.IRON_DOOR) ? Sound.BLOCK_IRON_DOOR_CLOSE : Sound.BLOCK_IRON_TRAPDOOR_CLOSE)
                : (material.equals(Material.IRON_DOOR) ? Sound.BLOCK_IRON_DOOR_OPEN : Sound.BLOCK_IRON_TRAPDOOR_OPEN);
    }
}
