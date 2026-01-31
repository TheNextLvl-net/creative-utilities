package net.thenextlvl.utilities.interfaces;

import core.paper.gui.GUI;
import core.paper.item.ItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import net.kyori.adventure.text.Component;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Random;
import java.util.function.Consumer;

public class ArmorCreatorGUI extends GUI<UtilitiesPlugin> {
    private static final String DICE_RED = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTEzMWRlOGU5NTFmZGQ3YjlhM2QyMzlkN2NjM2FhM2U4NjU1YTMzNmI5OTliOWVkYmI0ZmIzMjljYmQ4NyJ9fX0=";
    private static final String DICE_GREEN = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWMzY2VjNjg3NjlmZTljOTcxMjkxZWRiN2VmOTZhNGUzYjYwNDYyY2ZkNWZiNWJhYTFjYmIzYTcxNTEzZTdiIn19fQ==";
    private static final String DICE_BLUE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjMyOGYzNzhmMjhhOTg3MjIyNmY1Y2UwNGQ2ZTFkZmExMTE2MTg1ODdmNDhkZmExZmU4MmQwNDMyMTZhNWNmIn19fQ==";

    private static final String SOLID_RED = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y0NmMzMWQ2ZWU2ZWE2MTlmNzJlNzg1MjMyY2IwNDhhYjI3MDQ2MmRiMGNiMTQ1NDUxNDQzNjI1MWMxYSJ9fX0=";
    private static final String SOLID_GREEN = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZmNjlmN2I3NTM4YjQxZGMzNDM5ZjM2NThhYmJkNTlmYWNjYTM2NmYxOTBiY2YxZDZkMGEwMjZjOGY5NiJ9fX0=";
    private static final String SOLID_BLUE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjgzOWUzODFkOWZlZGFiNmY4YjU5Mzk2YTI3NjQyMzhkY2ViMmY3ZWVhODU2ZGM2ZmM0NDc2N2RhMzgyZjEifX19";

    private final Random random = new Random();

    private int red = 10;
    private int green = 10;
    private int blue = 10;

    public ArmorCreatorGUI(final UtilitiesPlugin plugin, final Player owner) {
        super(plugin, owner, plugin.bundle().component("gui.title.armor-color-creator", owner), 6);
        updateDices();
        updateSelector();
        updateArmor();
    }

    private void updateDices() {
        updateDice(22, DICE_RED, "gui.item.color.randomize.red", () -> this.red = random.nextInt(0, 21));
        updateDice(23, DICE_GREEN, "gui.item.color.randomize.green", () -> this.green = random.nextInt(0, 21));
        updateDice(24, DICE_BLUE, "gui.item.color.randomize.blue", () -> this.blue = random.nextInt(0, 21));
    }

    private void updateDice(final int slot, final String head, final String name, final Runnable randomization) {
        setSlot(slot, ItemBuilder.of(Material.PLAYER_HEAD)
                .itemName(plugin.bundle().component(name, owner))
                .profileValue(head)
                .withAction((type, player) -> {
                    if (type.equals(ClickType.DOUBLE_CLICK)) return;
                    randomization.run();
                    updateSelector();
                    updateArmor();
                }));
    }

    private void updateSelector() {
        updateSelector(31, red, Material.RED_STAINED_GLASS, SOLID_RED, "gui.item.color.red", amount -> this.red = amount);
        updateSelector(32, green, Material.GREEN_STAINED_GLASS, SOLID_GREEN, "gui.item.color.green", amount -> this.green = amount);
        updateSelector(33, blue, Material.BLUE_STAINED_GLASS, SOLID_BLUE, "gui.item.color.blue", amount -> this.blue = amount);
    }

    private void updateSelector(final int slot, final int amount, final Material fallback, final String head, final String name, final Consumer<Integer> setter) {
        final var item = amount == 0 ? ItemBuilder.of(fallback) : ItemBuilder.of(Material.PLAYER_HEAD)
                .profileValue(head).amount(amount);
        setSlot(slot, item.itemName(plugin.bundle().component(name, owner)).lore(
                Component.empty(),
                plugin.bundle().component("gui.item.color.left", owner),
                plugin.bundle().component("gui.item.color.right", owner),
                plugin.bundle().component("gui.item.color.shift", owner)
        ).withAction((type, player) -> {
            final var newAmount = Math.clamp(amount + switch (type) {
                case LEFT -> 1;
                case RIGHT -> -1;
                case SHIFT_LEFT -> 5;
                case SHIFT_RIGHT -> -5;
                default -> 0;
            }, 0, 20);
            if (amount == newAmount) return;
            setter.accept(newAmount);
            updateSelector(slot, newAmount, fallback, head, name, setter);
            updateArmor();
        }));
    }

    private void updateArmor() {
        updateArmor(10, Material.LEATHER_HELMET);
        updateArmor(19, Material.LEATHER_CHESTPLATE);
        updateArmor(28, Material.LEATHER_LEGGINGS);
        updateArmor(37, Material.LEATHER_BOOTS);
    }

    private void updateArmor(final int slot, final Material material) {
        final var builder = ItemBuilder.of(material);
        final var color = Color.fromRGB(red * 255 / 20, green * 255 / 20, blue * 255 / 20);
        final var dye = DyedItemColor.dyedItemColor().color(color).build();
        setSlot(slot, builder.data(DataComponentTypes.DYED_COLOR, dye)
                .withAction(player -> player.getInventory().addItem(builder.item())));
    }

    @Override
    protected void formatDefault() {
    }
}
