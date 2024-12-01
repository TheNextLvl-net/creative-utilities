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
package net.thenextlvl.utilities.model;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class NoClipManager {
    private final UtilitiesPlugin plugin;

    public void start() {
        Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, task -> plugin.settingsController()
                .getNoClip().forEach(this::updateNoClip), 1, 1);
    }

    public boolean checkSurrounding(Player player) {
        return player.getLocation().add(+0.4, 0, 0).getBlock().isCollidable()
               || player.getLocation().add(-0.4, 0, 0).getBlock().isCollidable()
               || player.getLocation().add(0, 0, +0.4).getBlock().isCollidable()
               || player.getLocation().add(0, 0, -0.4).getBlock().isCollidable()
               || player.getLocation().add(+0.4, 1, 0).getBlock().isCollidable()
               || player.getLocation().add(-0.4, 1, 0).getBlock().isCollidable()
               || player.getLocation().add(0, 1, +0.4).getBlock().isCollidable()
               || player.getLocation().add(0, 1, -0.4).getBlock().isCollidable()
               || player.getLocation().add(0, +1.9, 0).getBlock().isCollidable();
    }

    public void updateNoClip(Player player) {
        if (!player.getGameMode().isInvulnerable()) return;

        boolean noClip;

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            if (player.getLocation().add(0, -0.1, 0).getBlock().isCollidable() && player.isSneaking()) {
                noClip = true;
            } else {
                noClip = checkSurrounding(player);
            }

            if (noClip) player.setGameMode(GameMode.SPECTATOR);

        } else if (player.getGameMode().equals(GameMode.SPECTATOR)) {
            if (player.getLocation().add(0, -0.1, 0).getBlock().isCollidable()) {
                noClip = true;
            } else {
                noClip = checkSurrounding(player);
            }

            if (!noClip) player.setGameMode(GameMode.CREATIVE);
        }
    }

}
