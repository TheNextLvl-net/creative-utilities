package net.thenextlvl.utilities.gui;

import core.paper.gui.GUI;
import core.paper.item.ItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
        super(plugin, owner, plugin.bundle().component(owner, "gui.title.utilities"), 3);
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
                "gui.item.iron-trapdoor-interaction.info",
                "builders.util.trapdoor",
                plugin.settingsController().isHandOpenable(owner),
                state -> plugin.settingsController().setHandOpenable(owner, state)
        );
    }

    private void updateCustomSlabBreaking() {
        updateFeature(11, Material.STONE_SLAB,
                "gui.item.custom-slab-breaking",
                "gui.item.custom-slab-breaking.info",
                "builders.util.slabs",
                plugin.settingsController().isSlabPartBreaking(owner),
                state -> plugin.settingsController().setSlabPartBreaking(owner, state)
        );
    }

    private void updateAirPlacing() {
        updateFeature(12, Material.STRUCTURE_VOID,
                "gui.item.air-placing",
                "gui.item.air-placing.info",
                "builders.util.air-placing",
                plugin.settingsController().isAirPlacing(owner),
                state -> plugin.settingsController().setAirPlacing(owner, state)
        );
    }

    private void updateNightVision() {
        updateFeature(14, Material.ENDER_EYE,
                "gui.item.nightvision",
                "gui.item.nightvision.info",
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
                "gui.item.noclip.info",
                "builders.util.noclip",
                plugin.settingsController().isNoClip(owner),
                state -> plugin.settingsController().setNoClip(owner, state)
        );
    }

    private void updateAdvancedFly() {
        updateFeature(16, Material.FEATHER,
                "gui.item.advancedfly",
                "gui.item.advancedfly.info",
                "builders.util.advancedfly",
                plugin.settingsController().isAdvancedFly(owner),
                state -> plugin.settingsController().setAdvancedFly(owner, state)
        );
    }

    private void updateFeature(int slot, Material icon, String title, String lore, String permission, boolean enabled, Consumer<Boolean> setter) {
        var item = new ItemBuilder(icon).itemName(plugin.bundle().component(owner, title));

        if (!owner.hasPermission(permission)) {
            setSlot(slot, item.lore(plugin.bundle().components(owner, "gui.item.permission")));
            updateState(slot, null);
            return;
        }

        var message = enabled ? "gui.state.enabled" : "gui.state.disabled";
        setSlot(slot, item.lore(plugin.bundle().components(owner, lore,
                        Placeholder.component("state", plugin.bundle().component(owner, message))))
                .withAction((type, player) -> {
                    if (type.equals(ClickType.DOUBLE_CLICK)) return;
                    setter.accept(!enabled);
                    updateFeature(slot, icon, title, lore, permission, !enabled, setter);
                }));
        updateState(slot, enabled);
    }

    private void updateState(int slot, @Nullable Boolean state) {
        var item = ItemStack.of(state == null ? Material.ORANGE_STAINED_GLASS_PANE
                : state ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
        item.setData(DataComponentTypes.HIDE_TOOLTIP);
        setSlot(slot - 9, item);
        setSlot(slot + 9, item);
    }

    @Override
    protected void formatDefault() {
    }
}
