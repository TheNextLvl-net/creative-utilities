package net.thenextlvl.utilities.setting;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVisionSetting implements Settings {
    private static final PotionEffect nightVision = new PotionEffect(
            PotionEffectType.NIGHT_VISION,
            PotionEffect.INFINITE_DURATION,
            0, true, false
    );

    @Override
    public boolean toggle(final Player player) {
        final var enabled = !get(player);
        set(player, enabled);
        return enabled;
    }

    @Override
    public void set(final Player player, final boolean enabled) {
        if (enabled) player.addPotionEffect(nightVision);
        else player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    @Override
    public boolean get(final Player player) {
        return player.hasPotionEffect(PotionEffectType.NIGHT_VISION);
    }
}
