package net.thenextlvl.utilities.listener;

import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

@NullMarked
public class AdvancedFlyListener implements Listener {
    static final Map<Player, Double> lastVelocity = new WeakHashMap<>();
    static final Set<Player> slower1 = Collections.newSetFromMap(new WeakHashMap<>());
    static final Set<Player> slower2 = Collections.newSetFromMap(new WeakHashMap<>());
    private final UtilitiesPlugin plugin;

    public AdvancedFlyListener(UtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getPlayer().isFlying()) return;

        if (!plugin.settingsController().isAdvancedFly(event.getPlayer())) return;

        if (Math.abs(event.getFrom().getYaw() - event.getTo().getYaw()) > 2.5) return;
        if (Math.abs(event.getFrom().getPitch() - event.getTo().getPitch()) > 2.5) return;

        var speed = event.getFrom().clone().add(0, -event.getFrom().getY(), 0)
                .distance(event.getTo().clone().add(0, -event.getTo().getY(), 0));

        double lastSpeed = lastVelocity.getOrDefault(event.getPlayer(), 0d);

        if (speed > lastSpeed) {
            lastVelocity.put(event.getPlayer(), speed);
            slower1.remove(event.getPlayer());
            slower2.remove(event.getPlayer());
            return;
        }

        if (speed * 1.2 >= lastSpeed) return;

        if (!slower1.add(event.getPlayer())) return;
        if (!slower2.add(event.getPlayer())) return;

        var vector = event.getPlayer().getVelocity().clone();
        vector.setX(0);
        vector.setZ(0);
        event.getPlayer().setVelocity(vector);

        lastVelocity.remove(event.getPlayer());
        slower1.remove(event.getPlayer());
        slower2.remove(event.getPlayer());
    }
}
