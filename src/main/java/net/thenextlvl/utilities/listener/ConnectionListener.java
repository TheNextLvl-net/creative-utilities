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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

@NullMarked
@RequiredArgsConstructor
public class ConnectionListener implements Listener {
    private final UtilitiesPlugin plugin;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.settingsController().setHandOpenable(event.getPlayer(), true);
        plugin.settingsController().setSlabPartBreaking(event.getPlayer(), true);
        Optional.ofNullable(event.getPlayer().getAttribute(Attribute.ATTACK_SPEED)).ifPresent(attribute -> {
            var value = plugin.config().fixAttackSpeed() ? 1024 : attribute.getDefaultValue();
            attribute.setBaseValue(value);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        AdvancedFlyListener.lastVelocity.remove(event.getPlayer());
        AdvancedFlyListener.slower1.remove(event.getPlayer());
        AdvancedFlyListener.slower2.remove(event.getPlayer());
        AirPlacingListener.targetBlocks.remove(event.getPlayer());
        plugin.settingsController().purge(event.getPlayer());
    }
}
