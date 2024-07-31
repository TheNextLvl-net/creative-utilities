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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

@RequiredArgsConstructor
public class AdvancedFlyListener implements Listener {
    private final UtilitiesPlugin plugin;

    static final Map<Player, Double> lastVelocity = new WeakHashMap<>();

    static final Set<Player> slower1 = Collections.newSetFromMap(new WeakHashMap<>());
    static final Set<Player> slower2 = Collections.newSetFromMap(new WeakHashMap<>());

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getPlayer().isFlying()) return;

        if (!plugin.settingsController().isAdvancedFly(event.getPlayer())) return;

        if (Math.abs(event.getFrom().getYaw() - event.getTo().getYaw()) > 2.5) return;
        if (Math.abs(event.getFrom().getPitch() - event.getTo().getPitch()) > 2.5) return;

        var speed = event.getFrom().clone().add(0, -event.getFrom().getY(), 0)
                .distance(event.getTo().clone().add(0, -event.getTo().getY(), 0));

        double lastSpeed = lastVelocity.getOrDefault(event.getPlayer(), 0d);

        if (speed > lastSpeed) {
            lastVelocity.put(event.getPlayer(), speed);
            slower1.remove(event.getPlayer());
            slower2.remove(event.getPlayer());
            return;
        }

        if (speed * 1.2 >= lastSpeed) return;

        if (!slower1.add(event.getPlayer())) return;
        if (!slower2.add(event.getPlayer())) return;

        var vector = event.getPlayer().getVelocity().clone();
        vector.setX(0);
        vector.setZ(0);
        event.getPlayer().setVelocity(vector);

        lastVelocity.remove(event.getPlayer());
        slower1.remove(event.getPlayer());
        slower2.remove(event.getPlayer());
    }
}
