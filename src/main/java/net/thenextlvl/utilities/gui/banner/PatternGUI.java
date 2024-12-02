package net.thenextlvl.utilities.gui.banner;

import core.paper.gui.PagedGUI;
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
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@NullMarked
public class PatternGUI extends PagedGUI<UtilitiesPlugin, PatternType> {
    /**
     * Represents the maximum number of patterns that can be applied to a banner.
     * <a href="https://minecraft.wiki/w/Banner#Trivia">Wiki</a>
     */
    private static final short MAX_PATTERN_AMOUNT = 16;

    private final List<PatternType> patterns = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.BANNER_PATTERN).stream()
            .filter(patternType -> !patternType.equals(PatternType.BASE))
            .toList();
    private final Options options;
    private final ItemStack banner;
    private final DyeColor color;

    public PatternGUI(UtilitiesPlugin plugin, Player owner, ItemStack banner, DyeColor color) {
        super(plugin, owner, plugin.bundle().component(owner, "gui.title.banner.pattern"), 6);

        var slots = IntStream.of(19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
        this.options = new Options(slots.toArray(), 3, 5);
        this.banner = banner;
        this.color = color;

        loadPage(getCurrentPage());
    }

    @Override
    public void pageLoaded() {
        setSlot(1, new ItemBuilder(Material.PLAYER_HEAD)
                .itemName(plugin.bundle().component(owner, "gui.item.banner.randomize"))
                .headValue(BannerGUI.DICE)
                .withAction(player -> player.getScheduler().execute(plugin, () -> {
                    var pattern = patterns.get(ThreadLocalRandom.current().nextInt(0, patterns.size()));
                    giveOrContinue(applyPattern(banner, pattern, color));
                }, null, 1)));
        setSlot(4, new ItemBuilder(banner)
                .itemName(plugin.bundle().component(owner, "gui.item.banner"))
                .lore(plugin.bundle().components(owner, "gui.item.banner.get"))
                .withAction(player -> {
                    player.playSound(player, Sound.UI_LOOM_TAKE_RESULT, SoundCategory.BLOCKS, 1, 1);
                    player.getInventory().addItem(banner);
                }));
        setSlot(7, new ItemBuilder(Material.BARRIER)
                .itemName(plugin.bundle().component(owner, "gui.item.back"))
                .withAction(player -> new ColorGUI(plugin, player, banner).open()));
        super.pageLoaded();
    }

    @Override
    public ActionItem constructItem(PatternType element) {
        var key = RegistryAccess.registryAccess()
                .getRegistry(RegistryKey.BANNER_PATTERN)
                .getKey(element);
        var title = key != null ? "gui.item.banner.pattern." + key.value() : "gui.item.banner.pattern.unknown";
        var item = applyPattern(banner.clone(), element, color);
        return new ItemBuilder(item)
                .itemName(plugin.bundle().component(owner, title))
                .lore(plugin.bundle().components(owner, "gui.item.banner.color.info"))
                .withAction(player -> player.getScheduler().execute(plugin, () -> giveOrContinue(item), null, 1));
    }

    private void giveOrContinue(ItemStack banner) {
        var data = banner.getData(DataComponentTypes.BANNER_PATTERNS);
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

    private ItemStack applyPattern(ItemStack banner, PatternType pattern, DyeColor color) {
        var data = banner.getData(DataComponentTypes.BANNER_PATTERNS);
        if (data == null) return banner;
        var patterns = BannerPatternLayers.bannerPatternLayers()
                .addAll(data.patterns())
                .add(new Pattern(color, pattern))
                .build();
        banner.setData(DataComponentTypes.BANNER_PATTERNS, patterns);
        return banner;
    }

    @Override
    protected void formatDefault() {
        var placeholder = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).hideTooltip(true);
        IntStream.range(0, getSize()).forEach(slot -> setSlotIfAbsent(slot, placeholder));
    }

    @Override
    public Component getPageFormat(int page) {
        return plugin.bundle().component(owner, getCurrentPage() < page ? "gui.page.next" : "gui.page.previous");
    }

    @Override
    public Collection<PatternType> getElements() {
        return this.patterns;
    }

    @Override
    public Options getOptions() {
        return this.options;
    }
}
