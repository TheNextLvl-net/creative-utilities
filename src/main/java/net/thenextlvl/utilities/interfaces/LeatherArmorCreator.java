package net.thenextlvl.utilities.interfaces;

import core.paper.item.ItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import net.kyori.adventure.text.Component;
import net.thenextlvl.interfaces.ActionItem;
import net.thenextlvl.interfaces.Interface;
import net.thenextlvl.interfaces.Layout;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ThreadLocalRandom;

public final class LeatherArmorCreator {
    private static final String DICE_BLUE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjMyOGYzNzhmMjhhOTg3MjIyNmY1Y2UwNGQ2ZTFkZmExMTE2MTg1ODdmNDhkZmExZmU4MmQwNDMyMTZhNWNmIn19fQ==";
    private static final String DICE_GREEN = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWMzY2VjNjg3NjlmZTljOTcxMjkxZWRiN2VmOTZhNGUzYjYwNDYyY2ZkNWZiNWJhYTFjYmIzYTcxNTEzZTdiIn19fQ==";
    private static final String DICE_RED = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTEzMWRlOGU5NTFmZGQ3YjlhM2QyMzlkN2NjM2FhM2U4NjU1YTMzNmI5OTliOWVkYmI0ZmIzMjljYmQ4NyJ9fX0=";
    private static final String SOLID_BLUE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjgzOWUzODFkOWZlZGFiNmY4YjU5Mzk2YTI3NjQyMzhkY2ViMmY3ZWVhODU2ZGM2ZmM0NDc2N2RhMzgyZjEifX19";
    private static final String SOLID_GREEN = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZmNjlmN2I3NTM4YjQxZGMzNDM5ZjM2NThhYmJkNTlmYWNjYTM2NmYxOTBiY2YxZDZkMGEwMjZjOGY5NiJ9fX0=";
    private static final String SOLID_RED = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y0NmMzMWQ2ZWU2ZWE2MTlmNzJlNzg1MjMyY2IwNDhhYjI3MDQ2MmRiMGNiMTQ1NDUxNDQzNjI1MWMxYSJ9fX0=";
    private static final UtilitiesPlugin plugin = JavaPlugin.getPlugin(UtilitiesPlugin.class);
    public static final Interface INSTANCE;

    static {
        final var builder = Interface.builder()
                .title(player -> plugin.bundle().component("gui.title.armor-color-creator", player))
                .layout(Layout.builder(
                                "         ",
                                " 1       ",
                                " 2  RGB  ",
                                " 3  rgb  ",
                                " 4       ",
                                "         ")
                        .mask(' ', ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip().build())
                        .build());
        addChannel(builder, 'r', "red", Material.RED_STAINED_GLASS, SOLID_RED, "gui.item.color.red");
        addChannel(builder, 'g', "green", Material.GREEN_STAINED_GLASS, SOLID_GREEN, "gui.item.color.green");
        addChannel(builder, 'b', "blue", Material.BLUE_STAINED_GLASS, SOLID_BLUE, "gui.item.color.blue");
        addDice(builder, 'R', "red", DICE_RED, "gui.item.color.randomize.red");
        addDice(builder, 'G', "green", DICE_GREEN, "gui.item.color.randomize.green");
        addDice(builder, 'B', "blue", DICE_BLUE, "gui.item.color.randomize.blue");
        addPiece(builder, '1', Material.LEATHER_HELMET);
        addPiece(builder, '2', Material.LEATHER_CHESTPLATE);
        addPiece(builder, '3', Material.LEATHER_LEGGINGS);
        addPiece(builder, '4', Material.LEATHER_BOOTS);

        INSTANCE = builder.build(plugin);
    }

    private static void addPiece(final Interface.Builder builder, final char key, final Material material) {
        builder.slot(key, new ActionItem(context -> {
            final var item = ItemStack.of(material);
            final var red = context.getState("red", int.class, 10);
            final var green = context.getState("green", int.class, 10);
            final var blue = context.getState("blue", int.class, 10);
            final var color = Color.fromRGB(red * 255 / 20, green * 255 / 20, blue * 255 / 20);
            item.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(color));
            return item;
        }, context -> {
            final var item = context.session().view().getTopInventory().getItem(context.slot());
            if (item != null) context.player().getInventory().addItem(item.clone());
        }));
    }

    private static void addDice(final Interface.Builder builder, final char key, final String state,
                                final String texture, final String name) {
        builder.slot(key, context -> ItemBuilder.of(Material.PLAYER_HEAD)
                .profileValue(texture)
                .itemName(plugin.bundle().component(name, context.player()))
                .build(), context -> {
            context.setState(state, ThreadLocalRandom.current().nextInt(21));
            context.session().refresh();
        });
    }

    private static void addChannel(
            final Interface.Builder builder, final char key, final String state,
            final Material fallback, final String texture, final String name
    ) {
        builder.slot(key, new ActionItem(context -> {
            final int amount = context.getState(state, int.class, 10);
            final var item = amount == 0
                    ? ItemBuilder.of(fallback)
                    : ItemBuilder.of(Material.PLAYER_HEAD).profileValue(texture).amount(amount);
            return item.itemName(plugin.bundle().component(name, context.player()))
                    .lore(Component.empty(),
                            plugin.bundle().component("gui.item.color.left", context.player()),
                            plugin.bundle().component("gui.item.color.right", context.player()),
                            plugin.bundle().component("gui.item.color.shift", context.player()))
                    .build();
        }, context -> {
            context.<Integer>computeState(state, (s, i) -> {
                final var current = i != null ? i : 10;
                return Math.clamp(current + switch (context.clickType()) {
                    case LEFT -> 1;
                    case RIGHT -> -1;
                    case SHIFT_LEFT -> 5;
                    case SHIFT_RIGHT -> -5;
                    default -> 0;
                }, 0, 20);
            });
            context.session().refresh();
        }));
    }
}
