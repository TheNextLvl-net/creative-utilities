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

public class DeformRotateAliasCommand implements ICommand {

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("builders.util.aliases")) {
            if (Settings.sendErrorMessages) {
                player.sendMessage(UtilitiesPlugin.MSG_NO_PERMISSION + "builders.util.aliases");
            }
            return;
        }

        if (args.length != 2) {
            player.sendMessage(UtilitiesPlugin.MSG_PREFIX + ChatColor.RED + "//derot [axis] [degrees]");
            return;
        }

        int degrees;
        try {
            degrees = Integer.parseInt(args[1]);
        } catch (Exception e) {
            player.sendMessage(UtilitiesPlugin.MSG_PREFIX + ChatColor.RED + "//derot [axis] [degrees]");
            return;
        }
//            Old:
//            float radian = (float) (((float) degrees / (float) 360) * 2 * Math.PI);

        //Use the degree to radian conversion number: "0.0174533" radians per degree
        float radiansPerDegree = 0.0174533f;
        float radian = degrees * radiansPerDegree;

        if (args[0].equalsIgnoreCase("x")) {
            UtilitiesPlugin.getInstance().getServer().dispatchCommand(player, "/deform rotate(y,z," + radian + ")");
        } else if (args[0].equalsIgnoreCase("y")) {
            UtilitiesPlugin.getInstance().getServer().dispatchCommand(player, "/deform rotate(x,z," + radian + ")");
        } else if (args[0].equalsIgnoreCase("z")) {
            UtilitiesPlugin.getInstance().getServer().dispatchCommand(player, "/deform rotate(x,y," + radian + ")");
        } else {
            player.sendMessage(UtilitiesPlugin.MSG_PREFIX + ChatColor.RED + "//derot [axis] [degrees]");
        }
    }

}