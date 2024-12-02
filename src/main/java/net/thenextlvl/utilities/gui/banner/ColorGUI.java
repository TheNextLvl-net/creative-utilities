package net.thenextlvl.utilities.gui.banner;

import core.paper.gui.GUI;
import core.paper.item.ItemBuilder;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@NullMarked
public class ColorGUI extends GUI<UtilitiesPlugin> {

    private static final List<Item> items = List.of(
            new Item(19, Material.WHITE_DYE, DyeColor.WHITE, "gui.item.banner.color.white"),
            new Item(20, Material.ORANGE_DYE, DyeColor.ORANGE, "gui.item.banner.color.orange"),
            new Item(21, Material.MAGENTA_DYE, DyeColor.MAGENTA, "gui.item.banner.color.magenta"),
            new Item(23, Material.LIGHT_BLUE_DYE, DyeColor.LIGHT_BLUE, "gui.item.banner.color.light_blue"),
            new Item(24, Material.YELLOW_DYE, DyeColor.YELLOW, "gui.item.banner.color.yellow"),
            new Item(25, Material.LIME_DYE, DyeColor.LIME, "gui.item.banner.color.lime"),
            new Item(28, Material.PINK_DYE, DyeColor.PINK, "gui.item.banner.color.pink"),
            new Item(29, Material.GRAY_DYE, DyeColor.GRAY, "gui.item.banner.color.gray"),
            new Item(30, Material.LIGHT_GRAY_DYE, DyeColor.LIGHT_GRAY, "gui.item.banner.color.light_gray"),
            new Item(31, Material.CYAN_DYE, DyeColor.CYAN, "gui.item.banner.color.cyan"),
            new Item(32, Material.PURPLE_DYE, DyeColor.PURPLE, "gui.item.banner.color.purple"),
            new Item(33, Material.BLUE_DYE, DyeColor.BLUE, "gui.item.banner.color.blue"),
            new Item(34, Material.BROWN_DYE, DyeColor.BROWN, "gui.item.banner.color.brown"),
            new Item(39, Material.GREEN_DYE, DyeColor.GREEN, "gui.item.banner.color.green"),
            new Item(40, Material.RED_DYE, DyeColor.RED, "gui.item.banner.color.red"),
            new Item(41, Material.BLACK_DYE, DyeColor.BLACK, "gui.item.banner.color.black")
    );

    public ColorGUI(UtilitiesPlugin plugin, Player owner, ItemStack banner) {
        super(plugin, owner, plugin.bundle().component(owner, "gui.title.banner.color"), 6);
        setSlot(1, new ItemBuilder(Material.PLAYER_HEAD)
                .itemName(plugin.bundle().component(owner, "gui.item.randomize"))
                .headValue(BannerGUI.DICE)
                .withAction(player -> {
                    player.playSound(player, Sound.UI_LOOM_SELECT_PATTERN, SoundCategory.BLOCKS, 1, 1);
                    var item = items.get(ThreadLocalRandom.current().nextInt(0, items.size()));
                    new PatternGUI(plugin, player, banner, item.color()).open();
                }));
        setSlot(4, new ItemBuilder(banner)
                .itemName(plugin.bundle().component(owner, "gui.item.banner"))
                .lore(plugin.bundle().components(owner, "gui.item.banner.get"))
                .withAction(player -> {
                    player.playSound(player, Sound.UI_LOOM_TAKE_RESULT, SoundCategory.BLOCKS, 1, 1);
                    player.getInventory().addItem(banner);
                }));
        setSlot(7, new ItemBuilder(Material.BARRIER)
                .itemName(plugin.bundle().component(owner, "gui.item.back"))
                .withAction(player -> new BannerGUI(plugin, player).open()));
        items.forEach(item -> setSlot(item.slot(), new ItemBuilder(item.type())
                .itemName(plugin.bundle().component(owner, item.name()))
                .lore(plugin.bundle().components(owner, "gui.item.banner.color.info"))
                .withAction(player -> {
                    player.playSound(player, Sound.UI_LOOM_SELECT_PATTERN, SoundCategory.BLOCKS, 1, 1);
                    new PatternGUI(plugin, player, banner, item.color()).open();
                })));
    }

    @Override
    protected void formatDefault() {
        var placeholder = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).hideTooltip(true);
        IntStream.range(0, getSize()).forEach(slot -> setSlotIfAbsent(slot, placeholder));
    }

    private record Item(int slot, Material type, DyeColor color, String name) {
    }
}
