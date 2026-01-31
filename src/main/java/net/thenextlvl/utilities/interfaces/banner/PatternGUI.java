package net.thenextlvl.utilities.interfaces.banner;

import core.paper.gui.PaginatedGUI;
import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class PatternGUI extends PaginatedGUI<UtilitiesPlugin, PatternType> {
    /**
     * Represents the maximum number of patterns that can be applied to a banner.
     * <a href="https://minecraft.wiki/w/Banner#Trivia">Wiki</a>
     */
    private static final short MAX_PATTERN_AMOUNT = 16;

    private final List<PatternType> patterns = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.BANNER_PATTERN).stream()
            .filter(patternType -> !patternType.equals(PatternType.BASE))
            .toList();
    private final Pagination pagination;
    private final ItemStack banner;
    private final DyeColor color;

    public PatternGUI(final UtilitiesPlugin plugin, final Player owner, final ItemStack banner, final DyeColor color) {
        super(plugin, owner, plugin.bundle().component("gui.title.banner.pattern", owner), 6);

        final var slots = IntStream.of(19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
        this.pagination = new Pagination(slots.toArray(), 3, 5);
        this.banner = banner;
        this.color = color;

        loadPage(getCurrentPage());
    }

    @Override
    public void pageLoaded() {
        setSlot(1, ItemBuilder.of(Material.PLAYER_HEAD)
                .itemName(plugin.bundle().component("gui.item.randomize", owner))
                .profileValue(BannerGUI.DICE)
                .withAction(player -> player.getScheduler().execute(plugin, () -> {
                    final var pattern = patterns.get(ThreadLocalRandom.current().nextInt(0, patterns.size()));
                    giveOrContinue(applyPattern(banner.clone(), pattern, color));
                }, null, 1)));
        setSlot(4, ItemBuilder.of(banner.clone())
                .itemName(plugin.bundle().component("gui.item.banner", owner))
                .lore(Component.empty(), plugin.bundle().component("gui.item.banner.click", owner))
                .withAction(player -> {
                    player.playSound(player, Sound.UI_LOOM_TAKE_RESULT, SoundCategory.BLOCKS, 1, 1);
                    player.getInventory().addItem(banner);
                }));
        setSlot(7, ItemBuilder.of(Material.BARRIER)
                .itemName(plugin.bundle().component("gui.item.back", owner))
                .withAction(player -> new ColorGUI(plugin, player, banner).open()));
        super.pageLoaded();
    }

    @Override
    public ActionItem constructItem(final PatternType element) {
        final var key = RegistryAccess.registryAccess()
                .getRegistry(RegistryKey.BANNER_PATTERN)
                .getKey(element);
        final var title = key != null ? "gui.item.banner.pattern." + key.value() : "gui.item.banner.pattern.unknown";
        final var item = applyPattern(banner.clone(), element, color);
        return ItemBuilder.of(item.clone())
                .itemName(plugin.bundle().component(title, owner))
                .lore(Component.empty(), plugin.bundle().component("gui.item.banner.color.click", owner))
                .withAction(player -> player.getScheduler().execute(plugin, () -> giveOrContinue(item), null, 1));
    }

    private void giveOrContinue(final ItemStack banner) {
        final var data = banner.getData(DataComponentTypes.BANNER_PATTERNS);
        if (data == null || data.patterns().size() >= MAX_PATTERN_AMOUNT) {
            plugin.bundle().sendMessage(owner, "banner.patterns.maximum");
            owner.playSound(owner, Sound.UI_LOOM_TAKE_RESULT, SoundCategory.BLOCKS, 1, 1);
            owner.getInventory().addItem(banner);
            owner.getInventory().close();
        } else {
            owner.playSound(owner, Sound.UI_LOOM_SELECT_PATTERN, SoundCategory.BLOCKS, 1, 1);
            new ColorGUI(plugin, owner, banner).open();
        }
    }

    private ItemStack applyPattern(final ItemStack banner, final PatternType pattern, final DyeColor color) {
        final var data = banner.getData(DataComponentTypes.BANNER_PATTERNS);
        if (data == null) return banner;
        final var patterns = BannerPatternLayers.bannerPatternLayers()
                .addAll(data.patterns())
                .add(new Pattern(color, pattern))
                .build();
        banner.setData(DataComponentTypes.BANNER_PATTERNS, patterns);
        return banner;
    }

    @Override
    protected void formatDefault() {
        final var placeholder = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip();
        IntStream.range(0, getSize()).forEach(slot -> setSlotIfAbsent(slot, placeholder));
    }

    @Override
    public Component getPageFormat(final int page) {
        final var message = getCurrentPage() < page ? "gui.page.next" : "gui.page.previous";
        return plugin.bundle().component(message, owner);
    }

    @Override
    public Collection<PatternType> getElements() {
        return this.patterns;
    }

    @Override
    public Pagination getPagination() {
        return pagination;
    }
}
