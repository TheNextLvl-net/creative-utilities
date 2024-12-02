package net.thenextlvl.utilities.gui.banner;

import core.paper.gui.GUI;
import core.paper.item.ItemBuilder;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@NullMarked
public class BannerGUI extends GUI<UtilitiesPlugin> {
    static final String DICE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzk3OTU1NDYyZTRlNTc2NjY0NDk5YWM0YTFjNTcyZjYxNDNmMTlhZDJkNjE5NDc3NjE5OGY4ZDEzNmZkYjIifX19";

    private static final List<Item> items = List.of(
            new Item(19, Material.WHITE_BANNER, "gui.item.banner.color.white"),
            new Item(20, Material.ORANGE_BANNER, "gui.item.banner.color.orange"),
            new Item(21, Material.MAGENTA_BANNER, "gui.item.banner.color.magenta"),
            new Item(23, Material.LIGHT_BLUE_BANNER, "gui.item.banner.color.light_blue"),
            new Item(24, Material.YELLOW_BANNER, "gui.item.banner.color.yellow"),
            new Item(25, Material.LIME_BANNER, "gui.item.banner.color.lime"),
            new Item(28, Material.PINK_BANNER, "gui.item.banner.color.pink"),
            new Item(29, Material.GRAY_BANNER, "gui.item.banner.color.gray"),
            new Item(30, Material.LIGHT_GRAY_BANNER, "gui.item.banner.color.light_gray"),
            new Item(31, Material.CYAN_BANNER, "gui.item.banner.color.cyan"),
            new Item(32, Material.PURPLE_BANNER, "gui.item.banner.color.purple"),
            new Item(33, Material.BLUE_BANNER, "gui.item.banner.color.blue"),
            new Item(34, Material.BROWN_BANNER, "gui.item.banner.color.brown"),
            new Item(39, Material.GREEN_BANNER, "gui.item.banner.color.green"),
            new Item(40, Material.RED_BANNER, "gui.item.banner.color.red"),
            new Item(41, Material.BLACK_BANNER, "gui.item.banner.color.black")
    );

    public BannerGUI(UtilitiesPlugin plugin, Player owner) {
        super(plugin, owner, plugin.bundle().component(owner, "gui.title.banner.base"), 6);
        setSlot(1, new ItemBuilder(Material.PLAYER_HEAD)
                .itemName(plugin.bundle().component(owner, "gui.item.banner.randomize"))
                .headValue(DICE)
                .withAction(player -> {
                    var item = items.get(ThreadLocalRandom.current().nextInt(0, items.size()));
                    new ColorGUI(plugin, player, ItemStack.of(item.type())).open();
                }));
        setSlot(4, new ItemBuilder(Material.WHITE_BANNER)
                .itemName(plugin.bundle().component(owner, "gui.item.banner")));
        setSlot(7, new ItemBuilder(Material.BARRIER)
                .itemName(plugin.bundle().component(owner, "gui.item.close"))
                .withAction(player -> player.getScheduler().execute(plugin, player::closeInventory, null, 1)));
        items.forEach(item -> setSlot(item.slot(), new ItemBuilder(item.type())
                .itemName(plugin.bundle().component(owner, item.name()))
                .lore(plugin.bundle().components(owner, "gui.item.banner.color.info"))
                .withAction(player -> new ColorGUI(plugin, player, ItemStack.of(item.type())).open())));
    }

    @Override
    protected void formatDefault() {
        var placeholder = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).hideTooltip(true);
        IntStream.range(0, getSize()).forEach(slot -> setSlotIfAbsent(slot, placeholder));
    }

    private record Item(int slot, Material type, String name) {
    }
}
