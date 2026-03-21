package net.thenextlvl.utilities.setting;

import org.bukkit.entity.Player;

public interface Settings {
    SimpleSetting ADVANCED_FLY = new SimpleSetting();
    SimpleSetting AIR_PLACING = new SimpleSetting();
    SimpleSetting NO_CLIP = new SimpleSetting();
    SimpleSetting HAND_OPENABLE = new SimpleSetting();
    SimpleSetting SLAB_PART_BREAKING = new SimpleSetting();
    Settings NIGHT_VISION = new NightVisionSetting();

    boolean toggle(Player player);

    void set(Player player, boolean enabled);

    boolean get(Player player);

    static void invalidate(final Player player) {
        ADVANCED_FLY.remove(player);
        AIR_PLACING.remove(player);
        NO_CLIP.remove(player);
        HAND_OPENABLE.remove(player);
        SLAB_PART_BREAKING.remove(player);
    }
}
