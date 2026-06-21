package net.thenextlvl.utilities.interfaces.banner;

import core.paper.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.thenextlvl.interfaces.Interface;
import net.thenextlvl.interfaces.Layout;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

final class ColorSelector {
    private static final List<DyeColor> COLORS = Arrays.stream(DyeColor.values())
            .sorted(Comparator.comparing(Enum::name))
            .toList();
    private static final UtilitiesPlugin plugin = JavaPlugin.getPlugin(UtilitiesPlugin.class);

    public static Interface getInstance(final ItemStack banner) {
        return Interface.builder()
                .title(player -> plugin.bundle().component("gui.title.banner.color", player))
                .layout(Layout.builder(
                                " r  p  b ",
                                "         ",
                                " XX   XX ",
                                " XXXXXXX ",
                                "  XXXXX  ",
                                "         ")
                        .mask(' ', ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip().build())
                        .build())
                .slot('r', context -> ItemBuilder.of(Material.PLAYER_HEAD).profileValue(BannerSelector.DICE)
                        .itemName(plugin.bundle().component("gui.item.randomize", context.player())).build(), context -> {
                    PatternSelector.getInstance(banner, COLORS.get(ThreadLocalRandom.current().nextInt(COLORS.size()))).open(context.player());
                })
                .slot('p', context -> ItemBuilder.of(banner.clone())
                        .itemName(plugin.bundle().component("gui.item.banner", context.player()))
                        .lore(Component.empty(), plugin.bundle().component("gui.item.banner.click", context.player())).build(), context -> {
                    context.player().getInventory().addItem(banner.clone());
                })
                .slot('b', context -> ItemBuilder.of(Material.BARRIER)
                        .itemName(plugin.bundle().component("gui.item.back", context.player())).build(), context -> {
                    BannerSelector.INSTANCE.open(context.player());
                })
                .slot('X', context -> {
                    final var color = COLORS.get(context.index() % COLORS.size());
                    return ItemBuilder.of(Material.valueOf(color.name() + "_DYE"))
                            .itemName(plugin.bundle().component("gui.item.banner.color." + color.name().toLowerCase(), context.player()))
                            .lore(Component.empty(), plugin.bundle().component("gui.item.banner.color.click", context.player())).build();
                }, context -> {
                    PatternSelector.getInstance(banner, COLORS.get(context.index() % COLORS.size())).open(context.player());
                })
                .build(plugin);
    }
}
