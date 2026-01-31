package net.thenextlvl.utilities.interfaces;

import core.paper.gui.GUI;
import core.paper.item.ItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.utils.Permissions;
import net.thenextlvl.utilities.utils.Settings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public final class UtilitiesGUI extends GUI<UtilitiesPlugin> {
    private final PotionEffect nightVision = new PotionEffect(
            PotionEffectType.NIGHT_VISION,
            PotionEffect.INFINITE_DURATION,
            0, true, false
    );

    public UtilitiesGUI(final UtilitiesPlugin plugin, final Player owner) {
        super(plugin, owner, plugin.bundle().component("gui.title.utilities", owner), 3);
        updateIronInteraction();
        updateCustomSlabBreaking();
        updateAirPlacing();
        updateNightVision();
        updateNoClip();
        updateAdvancedFly();
    }

    private void updateIronInteraction() {
        updateFeature(10, Material.IRON_TRAPDOOR,
                "gui.item.iron-trapdoor-interaction",
                "gui.item.iron-trapdoor-interaction.description",
                Permissions.TRAPDOOR,
                Settings.get(owner, Settings.HAND_OPENABLE),
                state -> Settings.set(owner, Settings.HAND_OPENABLE, state)
        );
    }

    private void updateCustomSlabBreaking() {
        updateFeature(11, Material.STONE_SLAB,
                "gui.item.custom-slab-breaking",
                "gui.item.custom-slab-breaking.description",
                Permissions.SLABS,
                Settings.get(owner, Settings.SLAB_PART_BREAKING),
                state -> Settings.set(owner, Settings.SLAB_PART_BREAKING, state)
        );
    }

    private void updateAirPlacing() {
        updateFeature(12, Material.STRUCTURE_VOID,
                "gui.item.air-placing",
                "gui.item.air-placing.description",
                Permissions.AIR_PLACING,
                Settings.get(owner, Settings.AIR_PLACING),
                state -> Settings.set(owner, Settings.AIR_PLACING, state)
        );
    }

    private void updateNightVision() {
        updateFeature(14, Material.ENDER_EYE,
                "gui.item.nightvision",
                "gui.item.nightvision.description",
                Permissions.NIGHT_VISION,
                owner.hasPotionEffect(PotionEffectType.NIGHT_VISION), state -> {
                    if (state) owner.addPotionEffect(nightVision);
                    else owner.removePotionEffect(PotionEffectType.NIGHT_VISION);
                }
        );
    }

    private void updateNoClip() {
        updateFeature(15, Material.COMPASS,
                "gui.item.noclip",
                "gui.item.noclip.description",
                Permissions.NO_CLIP,
                Settings.get(owner, Settings.NO_CLIP),
                state -> Settings.set(owner, Settings.NO_CLIP, state)
        );
    }

    private void updateAdvancedFly() {
        updateFeature(16, Material.FEATHER,
                "gui.item.advancedfly",
                "gui.item.advancedfly.description",
                Permissions.ADVANCED_FLY,
                Settings.get(owner, Settings.ADVANCED_FLY),
                state -> Settings.set(owner, Settings.ADVANCED_FLY, state)
        );
    }

    private void updateFeature(final int slot, final Material icon, final String title, final String description, final String permission, final boolean enabled, final Consumer<Boolean> setter) {
        final var item = ItemBuilder.of(icon).itemName(plugin.bundle().component(title, owner));

        if (!owner.hasPermission(permission)) {
            setSlot(slot, item.lore(plugin.bundle().component("gui.item.permission", owner)));
            updateState(slot, null);
            return;
        }

        setSlot(slot, item.lore(
                plugin.bundle().component("gui.state", owner, Formatter.booleanChoice("state", enabled)),
                Component.empty(),
                plugin.bundle().component("gui.toggle.click", owner), Component.empty(),
                plugin.bundle().component(description, owner)
                        .applyFallbackStyle(TextDecoration.ITALIC.withState(false))
                        .colorIfAbsent(NamedTextColor.DARK_GRAY)
        ).withAction((type, player) -> {
            if (type.equals(ClickType.DOUBLE_CLICK)) return;
            setter.accept(!enabled);
            updateFeature(slot, icon, title, description, permission, !enabled, setter);
        }));
        updateState(slot, enabled);
    }

    private void updateState(final int slot, @Nullable final Boolean state) {
        final var item = ItemStack.of(state == null ? Material.ORANGE_STAINED_GLASS_PANE
                : state ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
        item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true).build());
        setSlot(slot - 9, item);
        setSlot(slot + 9, item);
    }

    @Override
    protected void formatDefault() {
    }
}
