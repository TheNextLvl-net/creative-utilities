package net.thenextlvl.utilities.gui;

import core.paper.gui.GUI;
import core.paper.item.ItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

@NullMarked
public class UtilitiesGUI extends GUI<UtilitiesPlugin> {
    private final PotionEffect nightVision = new PotionEffect(
            PotionEffectType.NIGHT_VISION,
            PotionEffect.INFINITE_DURATION,
            0, true, false
    );

    public UtilitiesGUI(UtilitiesPlugin plugin, Player owner) {
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
                "builders.util.trapdoor",
                plugin.settingsController().isHandOpenable(owner),
                state -> plugin.settingsController().setHandOpenable(owner, state)
        );
    }

    private void updateCustomSlabBreaking() {
        updateFeature(11, Material.STONE_SLAB,
                "gui.item.custom-slab-breaking",
                "gui.item.custom-slab-breaking.description",
                "builders.util.slabs",
                plugin.settingsController().isSlabPartBreaking(owner),
                state -> plugin.settingsController().setSlabPartBreaking(owner, state)
        );
    }

    private void updateAirPlacing() {
        updateFeature(12, Material.STRUCTURE_VOID,
                "gui.item.air-placing",
                "gui.item.air-placing.description",
                "builders.util.air-placing",
                plugin.settingsController().isAirPlacing(owner),
                state -> plugin.settingsController().setAirPlacing(owner, state)
        );
    }

    private void updateNightVision() {
        updateFeature(14, Material.ENDER_EYE,
                "gui.item.nightvision",
                "gui.item.nightvision.description",
                "builders.util.nightvision",
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
                "builders.util.noclip",
                plugin.settingsController().isNoClip(owner),
                state -> plugin.settingsController().setNoClip(owner, state)
        );
    }

    private void updateAdvancedFly() {
        updateFeature(16, Material.FEATHER,
                "gui.item.advancedfly",
                "gui.item.advancedfly.description",
                "builders.util.advancedfly",
                plugin.settingsController().isAdvancedFly(owner),
                state -> plugin.settingsController().setAdvancedFly(owner, state)
        );
    }

    private void updateFeature(int slot, Material icon, String title, String description, String permission, boolean enabled, Consumer<Boolean> setter) {
        var item = ItemBuilder.of(icon).itemName(plugin.bundle().component(title, owner));

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

    private void updateState(int slot, @Nullable Boolean state) {
        var item = ItemStack.of(state == null ? Material.ORANGE_STAINED_GLASS_PANE
                : state ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
        item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true).build());
        setSlot(slot - 9, item);
        setSlot(slot + 9, item);
    }

    @Override
    protected void formatDefault() {
    }
}
