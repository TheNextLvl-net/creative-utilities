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
package net.thenextlvl.utilities.gui;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.gui.inventory.ClickableItem;
import net.thenextlvl.utilities.gui.inventory.content.InventoryContents;
import net.thenextlvl.utilities.gui.inventory.content.InventoryProvider;
import net.thenextlvl.utilities.util.Items;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class UtilitiesMenuProvider implements InventoryProvider {
    private final UtilitiesPlugin plugin;

    private static final ItemStack ENABLED = Items.create(Material.GREEN_STAINED_GLASS_PANE, "&c", "");
    private static final ItemStack DISABLED = Items.create(Material.RED_STAINED_GLASS_PANE, "&c", "");
    private static final ItemStack NO_PERMISSION = Items.create(Material.ORANGE_STAINED_GLASS_PANE, "&c", "");

    private static final String ENABLED_LORE = "&a&lEnabled__&7__&7Click to toggle";
    private static final String DISABLED_LORE = "&c&lDisabled__&7__&7Click to toggle";

    private static final String IRON_TRAPDOOR_LORE = "__&c__&8&oActs like wooden trapdoors";
    private static final String SLAB_BREAKING_LORE = "__&c__&8&oHold any slab to break double slab blocks";
    private static final String AIR_PLACING_LORE = "__&c__&8&oPlace blocks inside the air";

    private static final String NIGHT_VISION_LORE = "__&c__&8&oSee in the dark";
    private static final String NO_CLIP_LORE = "__&c__&8&oFly through blocks with ease";
    private static final String ADVANCED_FLY_LORE = "__&c__&8&oRemoves velocity when you stop flying";

    @Override
    public void init(Player player, InventoryContents contents) {
        setIronTrapdoorItem(player, contents);
        setSlabItem(player, contents);
        setAirPlacingItem(player, contents);
        setNightVisionItem(player, contents);
        setNoClipItem(player, contents);
        setFlyItem(player, contents);
    }

    private void setIronTrapdoorItem(Player player, InventoryContents contents) {
        if (!player.hasPermission("builders.util.trapdoor")) {
            setNoPermission(1, contents);
            contents.set(1, 1, ClickableItem.empty(
                    Items.create(Material.IRON_TRAPDOOR, "&6Iron Trapdoor Interaction", "&7&lNo Permission")));
            return;
        }

        if (plugin.settingsController().isHandOpenable(player)) {
            setEnabledGlassPanes(1, true, contents);
            contents.set(1, 1, ClickableItem.of(
                    Items.create(Material.IRON_TRAPDOOR,
                            "&6Iron Trapdoor Interaction", ENABLED_LORE + IRON_TRAPDOOR_LORE
                    ),
                    inventoryClickEvent -> {
                        plugin.settingsController().setHandOpenable(player, false);
                        setIronTrapdoorItem(player, contents);
                    }
            ));
        } else {
            setEnabledGlassPanes(1, false, contents);
            contents.set(1, 1, ClickableItem.of(
                    Items.create(Material.IRON_TRAPDOOR,
                            "&6Iron Trapdoor Interaction", DISABLED_LORE + IRON_TRAPDOOR_LORE
                    ),
                    inventoryClickEvent -> {
                        plugin.settingsController().setHandOpenable(player, true);
                        setIronTrapdoorItem(player, contents);
                    }
            ));
        }
    }

    private void setSlabItem(Player player, InventoryContents contents) {
        if (!player.hasPermission("builders.util.slabs")) {
            setNoPermission(2, contents);
            contents.set(1, 2, ClickableItem.empty(
                    Items.create(Material.STONE_SLAB, "&6Custom Slab Breaking", "&7&lNo Permission")));
            return;
        }

        if (plugin.settingsController().isSlabPartBreaking(player)) {
            setEnabledGlassPanes(2, true, contents);
            contents.set(1, 2, ClickableItem.of(
                    Items.create(Material.STONE_SLAB,
                            "&6Custom Slab Breaking", ENABLED_LORE + SLAB_BREAKING_LORE
                    ),
                    inventoryClickEvent -> {
                        plugin.settingsController().setSlabPartBreaking(player, false);
                        setSlabItem(player, contents);
                    }
            ));
        } else {
            setEnabledGlassPanes(2, false, contents);
            contents.set(1, 2, ClickableItem.of(
                    Items.create(Material.STONE_SLAB,
                            "&6Custom Slab Breaking", DISABLED_LORE + SLAB_BREAKING_LORE
                    ),
                    inventoryClickEvent -> {
                        plugin.settingsController().setSlabPartBreaking(player, true);
                        setSlabItem(player, contents);
                    }
            ));
        }
    }

    private void setAirPlacingItem(Player player, InventoryContents contents) {
        if (!player.hasPermission("builders.util.air-placing")) {
            setNoPermission(3, contents);
            contents.set(1, 3, ClickableItem.empty(
                    Items.create(Material.STRUCTURE_VOID, "&6Air Placing", "&7&lNo Permission")));
            return;
        }

        if (plugin.settingsController().isAirPlacing(player)) {
            setEnabledGlassPanes(3, true, contents);
            contents.set(1, 3, ClickableItem.of(
                    Items.create(Material.STRUCTURE_VOID,
                            "&6Air Placing", ENABLED_LORE + AIR_PLACING_LORE
                    ),
                    inventoryClickEvent -> {
                        plugin.settingsController().setAirPlacing(player, false);
                        setAirPlacingItem(player, contents);
                    }
            ));
        } else {
            setEnabledGlassPanes(3, false, contents);
            contents.set(1, 3, ClickableItem.of(
                    Items.create(Material.STRUCTURE_VOID,
                            "&6Air Placing", DISABLED_LORE + AIR_PLACING_LORE
                    ),
                    inventoryClickEvent -> {
                        plugin.settingsController().setAirPlacing(player, true);
                        setAirPlacingItem(player, contents);
                    }
            ));
        }

    }

    private void setNightVisionItem(Player player, InventoryContents contents) {
        if (!player.hasPermission("builders.util.nightvision")) {
            setNoPermission(5, contents);
            contents.set(1, 5, ClickableItem.empty(
                    Items.create(Material.ENDER_EYE, "&6Night Vision", "&7&lNo Permission")));
            return;
        }

        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            setEnabledGlassPanes(5, true, contents);
            contents.set(1, 5, ClickableItem.of(
                    Items.create(Material.ENDER_EYE,
                            "&6Night Vision", ENABLED_LORE + NIGHT_VISION_LORE
                    ),
                    inventoryClickEvent -> {
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        setNightVisionItem(player, contents);
                    }
            ));
        } else {
            setEnabledGlassPanes(5, false, contents);
            contents.set(1, 5, ClickableItem.of(
                    Items.create(Material.ENDER_EYE,
                            "&6Night Vision", DISABLED_LORE + NIGHT_VISION_LORE
                    ),
                    inventoryClickEvent -> {
                        player.addPotionEffect(new PotionEffect(
                                PotionEffectType.NIGHT_VISION,
                                Integer.MAX_VALUE,
                                0,
                                true,
                                false
                        ));
                        setNightVisionItem(player, contents);
                    }
            ));
        }
    }

    private void setNoClipItem(Player player, InventoryContents contents) {
        if (!player.hasPermission("builders.util.noclip")) {
            setNoPermission(6, contents);
            contents.set(1, 6, ClickableItem.empty(
                    Items.create(Material.COMPASS, "&6No Clip", "&7&lNo Permission")));
            return;
        }

        if (plugin.settingsController().isNoClip(player)) {
            setEnabledGlassPanes(6, true, contents);
            contents.set(1, 6, ClickableItem.of(
                    Items.create(Material.COMPASS,
                            "&6No Clip", ENABLED_LORE + NO_CLIP_LORE
                    ),
                    inventoryClickEvent -> {
                        plugin.settingsController().setNoClip(player, false);
                        if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                            player.setGameMode(GameMode.CREATIVE);
                        }
                        setNoClipItem(player, contents);
                    }
            ));
        } else {
            setEnabledGlassPanes(6, false, contents);
            contents.set(1, 6, ClickableItem.of(
                    Items.create(Material.COMPASS,
                            "&6No Clip", DISABLED_LORE + NO_CLIP_LORE
                    ),
                    inventoryClickEvent -> {
                        plugin.settingsController().setNoClip(player, true);
                        setNoClipItem(player, contents);
                    }
            ));
        }
    }

    private void setFlyItem(Player player, InventoryContents contents) {
        if (!player.hasPermission("builders.util.advancedfly")) {
            setNoPermission(7, contents);
            contents.set(1, 7, ClickableItem.empty(
                    Items.create(Material.FEATHER, "&6Advanced Fly", "&7&lNo Permission")));
            return;
        }

        if (plugin.settingsController().isAdvancedFly(player)) {
            setEnabledGlassPanes(7, true, contents);
            contents.set(1, 7, ClickableItem.of(
                    Items.create(Material.FEATHER,
                            "&6Advanced Fly", ENABLED_LORE + ADVANCED_FLY_LORE
                    ),
                    inventoryClickEvent -> {
                        plugin.settingsController().setAdvancedFly(player, false);
                        setFlyItem(player, contents);
                    }
            ));
        } else {
            setEnabledGlassPanes(7, false, contents);
            contents.set(1, 7, ClickableItem.of(
                    Items.create(Material.FEATHER,
                            "&6Advanced Fly", DISABLED_LORE + ADVANCED_FLY_LORE
                    ),
                    inventoryClickEvent -> {
                        plugin.settingsController().setAdvancedFly(player, true);
                        setFlyItem(player, contents);
                    }
            ));
        }
    }

    private void setEnabledGlassPanes(int col, boolean enabled, InventoryContents contents) {
        contents.set(0, col, ClickableItem.empty(enabled ? ENABLED : DISABLED));
        contents.set(2, col, ClickableItem.empty(enabled ? ENABLED : DISABLED));
    }

    private void setNoPermission(int col, InventoryContents contents) {
        contents.set(0, col, ClickableItem.empty(NO_PERMISSION));
        contents.set(2, col, ClickableItem.empty(NO_PERMISSION));
    }

}
