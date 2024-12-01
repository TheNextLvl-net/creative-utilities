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
package net.thenextlvl.utilities.util;

import core.paper.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public class Items {
    public static ItemStack create(Material mat, String name, String lore) {
        return Items.create(mat, (short) 0, 1, name, lore);
    }

    public static ItemStack create(Material mat, short data, int amount, String name, String lore) {
        ItemStack is = new ItemStack(mat);
        is.setAmount(amount);
        ItemMeta meta = is.getItemMeta();
        if (!lore.equals("")) {
            String[] loreListArray = lore.split("__");
            List<String> loreList = new ArrayList<>();
            for (String s : loreListArray) {
                loreList.add(s.replace('&', ChatColor.COLOR_CHAR));
            }
            meta.setLore(loreList);
        }
        if (!name.equals("")) {
            meta.setDisplayName(name.replace('&', ChatColor.COLOR_CHAR));
        }
        is.setItemMeta(meta);
        is.setDurability(data);
        return is;
    }

    public static ItemStack createHead(String data, int amount, String name, String lore) {
        return new ItemBuilder(Material.PLAYER_HEAD, amount)
                .headValue(data)
                .modify(meta -> {
                    lore(lore, meta);
                    if (!name.isEmpty()) {
                        meta.setDisplayName(name.replace('&', ChatColor.COLOR_CHAR));
                    }
                });
    }

    static void lore(String lore, ItemMeta meta) {
        if (!lore.isEmpty()) {
            String[] loreListArray = lore.split("__");
            List<String> loreList = new ArrayList<>();
            for (String s : loreListArray) {
                loreList.add(s.replace('&', ChatColor.COLOR_CHAR));
            }
            meta.setLore(loreList);
        }
    }

}
