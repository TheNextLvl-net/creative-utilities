package net.thenextlvl.utilities.interfaces.banner;

import core.paper.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.thenextlvl.interfaces.Interface;
import net.thenextlvl.interfaces.Layout;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class BannerSelector {
    public static final String DICE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzk3OTU1NDYyZTRlNTc2NjY0NDk5YWM0YTFjNTcyZjYxNDNmMTlhZDJkNjE5NDc3NjE5OGY4ZDEzNmZkYjIifX19";
    private static final List<Map.Entry<Material, String>> BANNERS = List.of(
            Map.entry(Material.BLACK_BANNER, "gui.item.banner.color.black"),
            Map.entry(Material.BLUE_BANNER, "gui.item.banner.color.blue"),
            Map.entry(Material.BROWN_BANNER, "gui.item.banner.color.brown"),
            Map.entry(Material.CYAN_BANNER, "gui.item.banner.color.cyan"),
            Map.entry(Material.GRAY_BANNER, "gui.item.banner.color.gray"),
            Map.entry(Material.GREEN_BANNER, "gui.item.banner.color.green"),
            Map.entry(Material.LIGHT_BLUE_BANNER, "gui.item.banner.color.light_blue"),
            Map.entry(Material.LIGHT_GRAY_BANNER, "gui.item.banner.color.light_gray"),
            Map.entry(Material.LIME_BANNER, "gui.item.banner.color.lime"),
            Map.entry(Material.MAGENTA_BANNER, "gui.item.banner.color.magenta"),
            Map.entry(Material.ORANGE_BANNER, "gui.item.banner.color.orange"),
            Map.entry(Material.PINK_BANNER, "gui.item.banner.color.pink"),
            Map.entry(Material.PURPLE_BANNER, "gui.item.banner.color.purple"),
            Map.entry(Material.RED_BANNER, "gui.item.banner.color.red"),
            Map.entry(Material.WHITE_BANNER, "gui.item.banner.color.white"),
            Map.entry(Material.YELLOW_BANNER, "gui.item.banner.color.yellow")
    );
    private static final UtilitiesPlugin plugin = JavaPlugin.getPlugin(UtilitiesPlugin.class);
    public static final Interface INSTANCE = Interface.builder()
            .title(player -> plugin.bundle().component("gui.title.banner.base", player))
            .layout(Layout.builder(
                            " r  p  c ",
                            "         ",
                            " XX   XX ",
                            " XXXXXXX ",
                            "  XXXXX  ",
                            "         ")
                    .mask(' ', ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip().build())
                    .mask('p', context -> ItemBuilder.of(Material.WHITE_BANNER)
                            .itemName(plugin.bundle().component("gui.item.banner", context.player())).build())
                    .build())
            .slot('r', context -> ItemBuilder.of(Material.PLAYER_HEAD).profileValue(DICE)
                    .itemName(plugin.bundle().component("gui.item.randomize", context.player())).build(), context -> {
                final var entry = BANNERS.get(ThreadLocalRandom.current().nextInt(BANNERS.size()));
                ColorSelector.getInstance(ItemStack.of(entry.getKey())).open(context.player());
            })
            .slot('c', context -> ItemBuilder.of(Material.BARRIER)
                    .itemName(plugin.bundle().component("gui.item.close", context.player())).build(), context -> {
                context.player().closeInventory();
            })
            .slot('X', context -> {
                final var entry = BANNERS.get(context.index() % BANNERS.size());
                return ItemBuilder.of(entry.getKey()).itemName(plugin.bundle().component(entry.getValue(), context.player()))
                        .lore(Component.empty(), plugin.bundle().component("gui.item.banner.color.click", context.player())).build();
            }, context -> {
                final var entry = BANNERS.get(context.index() % BANNERS.size());
                ColorSelector.getInstance(ItemStack.of(entry.getKey())).open(context.player());
            }).build(plugin);
}
