package net.thenextlvl.utilities.interfaces.banner;

import core.paper.item.ItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.thenextlvl.interfaces.ActionItem;
import net.thenextlvl.interfaces.Interface;
import net.thenextlvl.interfaces.Layout;
import net.thenextlvl.interfaces.PaginatedInterface;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ThreadLocalRandom;

final class PatternSelector {
    /**
     * Represents the maximum number of patterns that can be applied to a banner.
     * <a href="https://minecraft.wiki/w/Banner#Trivia">Wiki</a>
     */
    private static final short MAX_PATTERN_AMOUNT = 16;
    private static final UtilitiesPlugin plugin = JavaPlugin.getPlugin(UtilitiesPlugin.class);

    public static PaginatedInterface<PatternType> getInstance(final ItemStack banner, final DyeColor color) {
        final var patterns = RegistryAccess.registryAccess()
                .getRegistry(RegistryKey.BANNER_PATTERN).stream()
                .filter(type -> !type.equals(PatternType.BASE))
                .toList();
        final var placeholder = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip().build();
        final var base = Interface.builder()
                .title(player -> plugin.bundle().component("gui.title.banner.pattern", player))
                .layout(Layout.builder(
                                " r <p> b ",
                                "         ",
                                " XX   XX ",
                                " XXXXXXX ",
                                "  XXXXX  ",
                                "         ")
                        .mask(' ', placeholder).build())
                .slot('r', context -> ItemBuilder.of(Material.PLAYER_HEAD)
                        .itemName(plugin.bundle().component("gui.item.randomize", context.player()))
                        .profileValue(BannerSelector.DICE).build(), player -> player.player().getScheduler().execute(plugin, () -> {
                    final var pattern = patterns.get(ThreadLocalRandom.current().nextInt(0, patterns.size()));
                    giveOrContinue(player.player(), apply(banner.clone(), pattern, color));
                }, null, 1))
                .slot('p', context -> ItemBuilder.of(banner.clone())
                                .itemName(plugin.bundle().component("gui.item.banner", context.player()))
                                .lore(Component.empty(), plugin.bundle().component("gui.item.banner.click", context.player())).build(),
                        context -> context.player().getInventory().addItem(banner.clone()))
                .slot('b', context -> ItemBuilder.of(Material.BARRIER)
                                .itemName(plugin.bundle().component("gui.item.back", context.player())).build(),
                        context -> ColorSelector.getInstance(banner).open(context.player()))
                .slot('<', context -> ItemBuilder.of(Material.ARROW)
                                .itemName(plugin.bundle().component("gui.page.previous", context.player())).build(),
                        context -> context.paginatedSession().ifPresent(s -> s.setPage(s.getCurrentPage() - 1)))
                .slot('>', context -> ItemBuilder.of(Material.ARROW)
                                .itemName(plugin.bundle().component("gui.page.next", context.player())).build(),
                        context -> context.paginatedSession().ifPresent(s -> s.setPage(s.getCurrentPage() + 1)));
        return PaginatedInterface.<PatternType>builder(base).mask('X').content(patterns)
                .transformer(type -> new ActionItem(context -> {
                    final var key = RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN).getKey(type);
                    final var name = key == null ? "gui.item.banner.pattern.unknown" : "gui.item.banner.pattern." + key.value();
                    return ItemBuilder.of(apply(banner.clone(), type, color))
                            .itemName(plugin.bundle().component(name, context.player()))
                            .lore(Component.empty(), plugin.bundle().component("gui.item.banner.color.click", context.player())).build();
                }, context -> {
                    final var result = apply(banner.clone(), type, color);
                    final var data = result.getData(DataComponentTypes.BANNER_PATTERNS);
                    if (data != null && data.patterns().size() >= 16) context.player().getInventory().addItem(result);
                    else ColorSelector.getInstance(result).open(context.player());
                })).build(plugin);
    }

    private static ItemStack apply(final ItemStack banner, final PatternType type, final DyeColor color) {
        final var old = banner.getData(DataComponentTypes.BANNER_PATTERNS);
        final var builder = BannerPatternLayers.bannerPatternLayers();
        if (old != null) builder.addAll(old.patterns());
        banner.setData(DataComponentTypes.BANNER_PATTERNS, builder.add(new Pattern(color, type)).build());
        return banner;
    }

    private static void giveOrContinue(final Player player, final ItemStack banner) {
        final var data = banner.getData(DataComponentTypes.BANNER_PATTERNS);
        if (data == null || data.patterns().size() >= MAX_PATTERN_AMOUNT) {
            plugin.bundle().sendMessage(player, "banner.patterns.maximum");
            player.playSound(player, Sound.UI_LOOM_TAKE_RESULT, SoundCategory.BLOCKS, 1, 1);
            player.getInventory().addItem(banner);
            player.getInventory().close();
        } else {
            player.playSound(player, Sound.UI_LOOM_SELECT_PATTERN, SoundCategory.BLOCKS, 1, 1);
            ColorSelector.getInstance(banner).open(player);
        }
    }
}
