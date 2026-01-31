package net.thenextlvl.utilities.utils;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public enum Settings {
    ADVANCED_FLY,
    AIR_PLACING,
    NO_CLIP,
    HAND_OPENABLE,
    SLAB_PART_BREAKING;

    private static final Map<Player, Integer> settings = new ConcurrentHashMap<>();

    private int mask() {
        return 1 << ordinal();
    }

    public static boolean toggle(final Player player, final Settings setting) {
        final var enabled = !get(player, setting);
        set(player, setting, enabled);
        return enabled;
    }

    public static void set(final Player player, final Settings setting, final boolean enabled) {
        final var mask = setting.mask();
        settings.compute(player, (p, v) -> {
            if (enabled) return (v == null ? 0 : v) | mask;
            else return v == null ? null : v & ~mask;
        });
    }

    public static boolean get(final Player player, final Settings setting) {
        return (settings.getOrDefault(player, 0) & setting.mask()) != 0;
    }

    public static void invalidate(final Player player) {
        settings.remove(player);
    }

    public static Stream<Player> getNoClip() {
        return settings.entrySet().stream()
                .filter(entry -> (entry.getValue() & NO_CLIP.mask()) != 0)
                .map(Map.Entry::getKey);
    }
}
