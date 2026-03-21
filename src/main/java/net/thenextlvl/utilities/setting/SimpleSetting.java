package net.thenextlvl.utilities.setting;

import org.bukkit.entity.Player;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

public class SimpleSetting implements Settings {
    private final Set<Player> settings = new CopyOnWriteArraySet<>();

    @Override
    public boolean toggle(final Player player) {
        final var enabled = !get(player);
        set(player, enabled);
        return enabled;
    }

    @Override
    public void set(final Player player, final boolean enabled) {
        if (enabled) settings.add(player);
        else remove(player);
    }

    @Override
    public boolean get(final Player player) {
        return settings.contains(player);
    }

    public void forEachPlayer(final Consumer<Player> consumer) {
        settings.forEach(consumer);
    }

    void remove(final Player player) {
        settings.remove(player);
    }
}
