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
package net.thenextlvl.utilities.command.aliases;

import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.Settings;
import net.thenextlvl.utilities.command.system.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ScaleAliasCommand implements ICommand {

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("builders.util.aliases")) {
            if (Settings.sendErrorMessages) {
                player.sendMessage(UtilitiesPlugin.MSG_NO_PERMISSION + "builders.util.aliases");
            }
            return;
        }

        if (args.length != 1) {
            player.sendMessage(UtilitiesPlugin.MSG_PREFIX + ChatColor.RED + "//scale [size]");
            return;
        }

        double size;
        try {
            size = Double.parseDouble(args[0]);
        } catch (Exception e) {
            player.sendMessage(UtilitiesPlugin.MSG_PREFIX + ChatColor.RED + "//scale [size]");
            return;
        }

        UtilitiesPlugin.getInstance().getServer().dispatchCommand(player, "/deform x/=" + size + ";y/=" + size + ";z/=" + size);
    }

}