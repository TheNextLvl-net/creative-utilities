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
package net.thenextlvl.utilities.gui.banner;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.gui.inventory.ClickableItem;
import net.thenextlvl.utilities.gui.inventory.content.InventoryContents;
import net.thenextlvl.utilities.gui.inventory.content.InventoryProvider;
import net.thenextlvl.utilities.util.BannerUtil;
import net.thenextlvl.utilities.util.Items;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BannerPatternMenuProvider implements InventoryProvider {
    private final UtilitiesPlugin plugin;

    private static final ItemStack grayPane = Items
            .create(Material.GRAY_STAINED_GLASS_PANE, (short) 0, 1, "&7", "");
    private static final ItemStack randomizeHead = Items.createHead(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzk3OTU1NDYyZTRlNTc2NjY0NDk5YWM0YTFjNTcyZjYxNDNmMTlhZDJkNjE5NDc3NjE5OGY4ZDEzNmZkYjIifX19",
            1,
            "&7Click to randomise",
            ""
    );
    //    private static final ItemStack currentColor = BannerUtil.createBanner("&a", 1, DyeColor.WHITE, "");
    private static final ItemStack closeButton = Items
            .create(Material.BARRIER, (short) 0, 1, "&cClick to close", "");
    private static final ItemStack whiteBanner = Items
            .create(Material.WHITE_BANNER, (short) 0, 1, "&c", "");

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(grayPane));
        contents.set(0, 1, ClickableItem.of(randomizeHead, inventoryClickEvent -> selectRandomPattern(player)));
        contents.set(
                0,
                4,
                ClickableItem.of(BannerUtil.currentBanner.get(player.getUniqueId()), inventoryClickEvent -> getBanner(player))
        );
        contents.set(0, 7, ClickableItem.of(closeButton, inventoryClickEvent -> contents.inventory().close(player)));

        int row = 1;
        int column = 0;
        for (PatternType pattern : BannerUtil.allPatterns) {
            List<Pattern> patterns = new ArrayList<>();
            patterns.add(new Pattern(BannerUtil.selectedColor.get(player.getUniqueId()), pattern));
            contents.set(
                    row,
                    column,
                    ClickableItem.of(BannerUtil.createBanner(
                            "&3" + StringUtils.capitalize(pattern
                                    .toString()
                                    .toLowerCase()
                                    .replace("_", " ")),
                            BannerUtil.getOppositeBaseColor(BannerUtil.selectedColor.get(player.getUniqueId())),
                            "&7__&7click to select",
                            patterns
                    ), inventoryClickEvent -> selectPattern(player, pattern))
            );
            column++;
            if (column == 9) {
                column = 0;
                row++;
            }
        }

//        contents.set(0, 1, ClickableItem.of(randomizeHead, NULL));
//        contents.set(0, 4, ClickableItem.empty(currentColor));
//        contents.set(0, 7, ClickableItem.of(closeButton, inventoryClickEvent -> contents.inventory().close(player)));
//        for (int x = 19; x < 27; x++) {
//            ItemStack banner = BannerUtil.createBanner("&3" + BannerUtil.allColors.get(x - 19).toString().toLowerCase().replace("_", " "), 1, BannerUtil.allColors.get(x - 19), "&7__&7click to select");
//            contents.set(x / 9, x % 9, ClickableItem.of(banner, NULL));
//        }
//        for (int x = 28; x < 36; x++) {
//            ItemStack banner = BannerUtil.createBanner("&3" + BannerUtil.allColors.get(x - 28 + 8).toString().toLowerCase().replace("_", " "), 1, BannerUtil.allColors.get(x - 28 + 8), "&7__&7click to select");
//            contents.set(x / 9, x % 9, ClickableItem.of(banner, NULL));
//        }
    }

    private void selectRandomPattern(Player player) {
        selectPattern(player, BannerUtil.getRandomPattern());
    }

    private void selectPattern(Player player, PatternType patternType) {
        ItemStack banner = BannerUtil.currentBanner.get(player.getUniqueId());

        BannerUtil.currentBanner.put(player.getUniqueId(), BannerUtil.addPattern(
                banner, new Pattern(BannerUtil.selectedColor.get(player.getUniqueId()), patternType)));
        if (BannerUtil.getPatterns(banner).size() == 15) {
            getBanner(player);
        } else {
            plugin.bannerColorMenu.open(player);
        }
    }

    private void getBanner(Player player) {
        ItemStack banner = BannerUtil.currentBanner.get(player.getUniqueId());
        ItemMeta meta = banner.getItemMeta();
        meta.setDisplayName("");
        meta.setLore(new ArrayList<>());
        banner.setItemMeta(meta);
        player.getInventory().addItem(banner);
        player.closeInventory();
        BannerUtil.currentBanner.remove(player.getUniqueId());
    }

}
