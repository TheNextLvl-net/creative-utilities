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

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BannerUtil {

    public static final HashMap<UUID, ItemStack> currentBanner = new HashMap<>();
    public static final HashMap<UUID, DyeColor> selectedColor = new HashMap<>();

    public static final List<DyeColor> allColors = Arrays.stream(DyeColor.values())
            .toList();
    public static final List<PatternType> allPatterns = Arrays.stream(PatternType.values())
            .filter(patternType -> !patternType.equals(PatternType.BASE))
            .toList();
    private static final Random random = new Random();

    public static ItemStack createBanner(String name, DyeColor base, String lore, List<Pattern> patterns) {
        ItemStack item = Items.create(
                Material.matchMaterial(base.toString() + "_BANNER"),
                (short) 0,
                1,
                name,
                ""
        );
        BannerMeta meta = (BannerMeta) item.getItemMeta();

        meta.setPatterns(patterns);
        if (!lore.isEmpty()) {
            String[] loreListArray = lore.split("__");
            List<String> loreList = new ArrayList<>();
            for (String s : loreListArray) {
                loreList.add(s.replace('&', ChatColor.COLOR_CHAR));
            }
            meta.setLore(loreList);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createDye(String name, DyeColor base, String lore) {
        ItemStack item = Items.create(
                Material.matchMaterial(base.toString() + "_DYE"),
                (short) 0,
                1,
                name,
                ""
        );
        ItemMeta meta = item.getItemMeta();
        if (!lore.equals("")) {
            String[] loreListArray = lore.split("__");
            List<String> loreList = new ArrayList<>();
            for (String s : loreListArray) {
                loreList.add(s.replace('&', ChatColor.COLOR_CHAR));
            }
            meta.setLore(loreList);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createBanner(String name, DyeColor base, String lore) {
        ItemStack item = Items.create(
                Material.matchMaterial(base + "_BANNER"),
                (short) 0,
                1,
                name,
                ""
        );
        BannerMeta meta = (BannerMeta) item.getItemMeta();
        if (!lore.isEmpty()) {
            String[] loreListArray = lore.split("__");
            List<String> loreList = new ArrayList<>();
            for (String s : loreListArray) {
                loreList.add(s.replace('&', ChatColor.COLOR_CHAR));
            }
            meta.setLore(loreList);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createBanner(String name, int amount, DyeColor base, String lore, Pattern pat) {
        ItemStack item = Items.create(
                Material.matchMaterial(base.toString() + "_BANNER"),
                (short) 0,
                1,
                name,
                ""
        );
        BannerMeta meta = (BannerMeta) item.getItemMeta();
        meta.addPattern(pat);
        if (!lore.isEmpty()) {
            String[] loreListArray = lore.split("__");
            List<String> loreList = new ArrayList<>();
            for (String s : loreListArray) {
                loreList.add(s.replace('&', ChatColor.COLOR_CHAR));
            }
            meta.setLore(loreList);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addPattern(ItemStack i, Pattern pat) {
        return i.editMeta(BannerMeta.class, meta -> meta.addPattern(pat)) ? i : null;
    }

    public static List<Pattern> getPatterns(ItemStack i) {
        if (i.getItemMeta() instanceof BannerMeta meta) {
            return meta.getPatterns();
        }
        return new ArrayList<>();
    }

    public static DyeColor getRandomDye() {
        return allColors.get(random.nextInt(allColors.size()));
    }

    public static PatternType getRandomPattern() {
        return allPatterns.get(random.nextInt(allPatterns.size()));
    }

    public static DyeColor getOppositeBaseColor(DyeColor dyeColor) {
        return switch (dyeColor) {
            case RED, BLUE, CYAN, GRAY, BLACK, BROWN, GREEN, PURPLE -> DyeColor.WHITE;
            default -> DyeColor.BLACK;
        };
    }

}

