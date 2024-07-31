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
package net.thenextlvl.utilities.menus;

import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.menus.bannermenus.BannerColorMenuProvider;
import net.thenextlvl.utilities.menus.bannermenus.BannerMenuProvider;
import net.thenextlvl.utilities.menus.bannermenus.BannerPatternMenuProvider;
import net.thenextlvl.utilities.menus.inv.InventoryListener;
import net.thenextlvl.utilities.menus.inv.SmartInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class Menus {

    private static final InventoryListener<InventoryCloseEvent> removeGhostItemsListener =
            new InventoryListener<>(InventoryCloseEvent.class, inventoryCloseEvent -> {
                Bukkit.getScheduler().runTaskLater(UtilitiesPlugin.getInstance(), () -> {
                    ((Player) inventoryCloseEvent.getPlayer()).updateInventory();
                }, 1L);
            });

    public static final SmartInventory BANNER_MENU = SmartInventory.builder()
            .manager(UtilitiesPlugin.getInstance().getInventoryManager())
            .id("buildersutilsbanner")
            .provider(new BannerMenuProvider())
            .size(6, 9)
            .listener(removeGhostItemsListener)
            .title(ChatColor.BLUE + "Select a base color")
            .closeable(true)
            .build();

    public static final SmartInventory BANNER_MENU_COLOR = SmartInventory.builder()
            .manager(UtilitiesPlugin.getInstance().getInventoryManager())
            .id("buildersutilsbannercolor")
            .provider(new BannerColorMenuProvider())
            .size(6, 9)
            .listener(removeGhostItemsListener)
            .title(ChatColor.BLUE + "Select a color")
            .closeable(true)
            .build();

    public static final SmartInventory BANNER_MENU_PATTERN = SmartInventory.builder()
            .manager(UtilitiesPlugin.getInstance().getInventoryManager())
            .id("buildersutilsbannerpattern")
            .provider(new BannerPatternMenuProvider())
            .size(6, 9)
            .listener(removeGhostItemsListener)
            .title(ChatColor.BLUE + "Select a pattern")
            .closeable(true)
            .build();

    public static final SmartInventory COLOR_MENU = SmartInventory.builder()
            .manager(UtilitiesPlugin.getInstance().getInventoryManager())
            .id("buildersutilscolor")
            .provider(new ColorMenuProvider())
            .size(6, 9)
            .listener(removeGhostItemsListener)
            .title(ChatColor.BLUE + "Armor Color Creator")
            .closeable(true)
            .build();

    public static final SmartInventory SECRET_BLOCK_MENU = SmartInventory.builder()
            .manager(UtilitiesPlugin.getInstance().getInventoryManager())
            .id("buildersutilssecretblock")
            .provider(new SecretBlockMenuProvider())
            .size(1, 9)
            .listener(removeGhostItemsListener)
            .title(ChatColor.BLUE + "Secret Blocks")
            .closeable(true)
            .build();

    public static final SmartInventory TOGGLE_MENU = SmartInventory.builder()
            .manager(UtilitiesPlugin.getInstance().getInventoryManager())
            .id("buildersutilstoggle")
            .provider(new UtilitiesMenuProvider())
            .size(3, 9)
            .listener(removeGhostItemsListener)
            .title(ChatColor.BLUE + "Builder's Utilities")
            .closeable(true)
            .build();

}
