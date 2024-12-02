package net.thenextlvl.utilities.gui.pottery;

import core.paper.gui.PagedGUI;
import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotDecorations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.gui.banner.BannerGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@NullMarked
public class SherdSelectorGUI extends PagedGUI<UtilitiesPlugin, Material> {
    private static final List<Material> sherds = List.of(
            Material.ANGLER_POTTERY_SHERD,
            Material.ARCHER_POTTERY_SHERD,
            Material.ARMS_UP_POTTERY_SHERD,
            Material.BLADE_POTTERY_SHERD,
            Material.BREWER_POTTERY_SHERD,
            Material.BURN_POTTERY_SHERD,
            Material.DANGER_POTTERY_SHERD,
            Material.EXPLORER_POTTERY_SHERD,
            Material.FLOW_POTTERY_SHERD,
            Material.FRIEND_POTTERY_SHERD,
            Material.GUSTER_POTTERY_SHERD,
            Material.HEART_POTTERY_SHERD,
            Material.HEARTBREAK_POTTERY_SHERD,
            Material.HOWL_POTTERY_SHERD,
            Material.MINER_POTTERY_SHERD,
            Material.MOURNER_POTTERY_SHERD,
            Material.PLENTY_POTTERY_SHERD,
            Material.PRIZE_POTTERY_SHERD,
            Material.SCRAPE_POTTERY_SHERD,
            Material.SHEAF_POTTERY_SHERD,
            Material.SHELTER_POTTERY_SHERD,
            Material.SKULL_POTTERY_SHERD,
            Material.SNORT_POTTERY_SHERD
    );
    private final Options options;
    private final ItemStack pot;
    private final PotteryDesignerGUI.Side side;

    public SherdSelectorGUI(UtilitiesPlugin plugin, Player owner, ItemStack pot, PotteryDesignerGUI.Side side) {
        super(plugin, owner, plugin.bundle().component(owner, "gui.title.pottery.sherd"), 5);
        var slots = IntStream.of(19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);
        this.options = new Options(slots.toArray(), 3, 5);
        this.pot = pot;
        this.side = side;
        loadPage(getCurrentPage());
    }

    @Override
    public void pageLoaded() {
        setSlot(1, new ItemBuilder(Material.PLAYER_HEAD)
                .itemName(plugin.bundle().component(owner, "gui.item.randomize"))
                .headValue(BannerGUI.DICE)
                .withAction(player -> {
                    var data = pot.getData(DataComponentTypes.POT_DECORATIONS);
                    if (data != null) pot.setData(
                            DataComponentTypes.POT_DECORATIONS,
                            decorate(side, data, getRandomSherd(sherds.size()))
                    );
                    new PotteryDesignerGUI(plugin, owner, pot).open();
                }));
        setSlot(4, new ItemBuilder(pot)
                .itemName(plugin.bundle().component(owner, "gui.item.pottery"))
                .lore(plugin.bundle().components(owner, "gui.item.pottery.get"))
                .withAction(player -> player.getInventory().addItem(pot)));
        setSlot(7, new ItemBuilder(Material.BARRIER)
                .itemName(plugin.bundle().component(owner, "gui.item.back"))
                .withAction(player -> new PotteryDesignerGUI(plugin, owner, pot).open()));
        super.pageLoaded();
    }

    @Override
    public ActionItem constructItem(Material sherd) {
        return new ItemBuilder(sherd)
                .itemName(Component.translatable(sherd.translationKey(), NamedTextColor.GOLD))
                .withAction(player -> {
                    var data = pot.getData(DataComponentTypes.POT_DECORATIONS);
                    if (data != null) pot.setData(
                            DataComponentTypes.POT_DECORATIONS,
                            decorate(side, data, sherd.asItemType())
                    );
                    new PotteryDesignerGUI(plugin, owner, pot).open();
                });
    }

    static PotDecorations decorate(PotteryDesignerGUI.Side side, PotDecorations data, @Nullable ItemType sherd) {
        return switch (side) {
            case BACK -> PotDecorations.potDecorations(sherd, data.left(), data.right(), data.front());
            case FRONT -> PotDecorations.potDecorations(data.back(), data.left(), data.right(), sherd);
            case LEFT -> PotDecorations.potDecorations(data.back(), sherd, data.right(), data.front());
            case RIGHT -> PotDecorations.potDecorations(data.back(), data.left(), sherd, data.front());
        };
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
    public Collection<Material> getElements() {
        return sherds;
    }

    @Override
    public Options getOptions() {
        return this.options;
    }

    static PotDecorations getRandom() {
        var max = sherds.size() + 5;
        return PotDecorations.potDecorations(
                getRandomSherd(max), getRandomSherd(max),
                getRandomSherd(max), getRandomSherd(max)
        );
    }

    static @Nullable ItemType getRandomSherd(int max) {
        var index = ThreadLocalRandom.current().nextInt(max);
        return index >= sherds.size() ? null : sherds.get(index).asItemType();
    }
}
