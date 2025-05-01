package net.thenextlvl.utilities.gui.pottery;

import core.paper.gui.GUI;
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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.stream.IntStream;

@NullMarked
public class PotteryDesignerGUI extends GUI<UtilitiesPlugin> {

    public PotteryDesignerGUI(UtilitiesPlugin plugin, Player owner, ItemStack pot) {
        super(plugin, owner, plugin.bundle().component("gui.title.pottery", owner), 5);
        setSlot(10, ItemBuilder.of(Material.PLAYER_HEAD)
                .itemName(plugin.bundle().component("gui.item.randomize", owner))
                .profileValue(BannerGUI.DICE)
                .withAction(player -> {
                    pot.setData(DataComponentTypes.POT_DECORATIONS, SherdSelectorGUI.getRandom());
                    updatePot(pot);
                }));
        setSlot(16, ItemBuilder.of(Material.BARRIER)
                .itemName(plugin.bundle().component("gui.item.close", owner))
                .withAction(player -> player.getScheduler().execute(plugin, player::closeInventory, null, 1)));
        updatePot(pot);
    }

    private void updatePot(ItemStack pot) {
        setSlot(22, ItemBuilder.of(pot)
                .itemName(plugin.bundle().component("gui.item.pottery", owner))
                .lore(Component.empty(), plugin.bundle().component("gui.item.pottery.get", owner))
                .withAction(player -> player.getInventory().addItem(pot)));
        var data = pot.getData(DataComponentTypes.POT_DECORATIONS);
        updateSlot(13, Side.BACK, pot, data);
        updateSlot(21, Side.LEFT, pot, data);
        updateSlot(23, Side.RIGHT, pot, data);
        updateSlot(31, Side.FRONT, pot, data);
    }

    private void updateSlot(int slot, Side side, ItemStack pot, @Nullable PotDecorations data) {
        var item = getItem(side, data);
        setSlot(slot, ItemBuilder.of(item != null ? item : ItemStack.of(Material.BRICK))
                .itemName(item == null ? plugin.bundle().component(side.name, owner)
                        : Component.translatable(item.translationKey(), NamedTextColor.GOLD))
                .lore(Component.empty(), plugin.bundle().component("gui.item.pottery.set", owner),
                        plugin.bundle().component("gui.item.pottery.remove", owner))
                .withAction((type, player) -> {
                    if (type.isLeftClick()) new SherdSelectorGUI(plugin, owner, pot, side).open();
                    else if (type.isRightClick() && data != null) {
                        var decoration = SherdSelectorGUI.decorate(side, data, null);
                        pot.setData(DataComponentTypes.POT_DECORATIONS, decoration);
                        updatePot(pot);
                    }
                }));
    }

    private @Nullable ItemStack getItem(Side side, @Nullable PotDecorations data) {
        if (data == null) return null;
        return switch (side) {
            case LEFT -> data.left() != null ? data.left().createItemStack() : null;
            case RIGHT -> data.right() != null ? data.right().createItemStack() : null;
            case BACK -> data.back() != null ? data.back().createItemStack() : null;
            case FRONT -> data.front() != null ? data.front().createItemStack() : null;
        };
    }

    @Override
    protected void formatDefault() {
        var placeholder = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip();
        IntStream.range(0, getSize()).forEach(slot -> setSlotIfAbsent(slot, placeholder));
    }

    public enum Side {
        LEFT("gui.item.pottery.left"),
        RIGHT("gui.item.pottery.right"),
        FRONT("gui.item.pottery.front"),
        BACK("gui.item.pottery.back");

        private final String name;

        Side(String name) {
            this.name = name;
        }
    }
}
