package net.thenextlvl.utilities.controller;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@Getter
@NullMarked
@SuppressWarnings({"UnusedReturnValue", "BooleanMethodIsAlwaysInverted"})
public class SettingsController {
    private final Set<Player> advancedFly = Collections.newSetFromMap(new WeakHashMap<>());
    private final Set<Player> airPlacing = Collections.newSetFromMap(new WeakHashMap<>());
    private final Set<Player> noClip = Collections.newSetFromMap(new WeakHashMap<>());
    private final Set<Player> openable = Collections.newSetFromMap(new WeakHashMap<>());
    private final Set<Player> slabPartBreaking = Collections.newSetFromMap(new WeakHashMap<>());

    public boolean setAdvancedFly(Player player, boolean enabled) {
        return enabled ? advancedFly.add(player) : advancedFly.remove(player);
    }

    public boolean toggleAdvancedFly(Player player) {
        return !advancedFly.remove(player) && advancedFly.add(player);
    }

    public boolean isAdvancedFly(Player player) {
        return advancedFly.contains(player);
    }

    public boolean setAirPlacing(Player player, boolean enabled) {
        return enabled ? airPlacing.add(player) : airPlacing.remove(player);
    }

    public boolean toggleAirPlacing(Player player) {
        return !airPlacing.remove(player) && airPlacing.add(player);
    }

    public boolean isAirPlacing(Player player) {
        return airPlacing.contains(player);
    }

    public boolean setHandOpenable(Player player, boolean enabled) {
        return enabled ? openable.add(player) : openable.remove(player);
    }

    public boolean toggleHandOpenable(Player player) {
        return !openable.remove(player) && openable.add(player);
    }

    public boolean isHandOpenable(Player player) {
        return openable.contains(player);
    }

    public boolean setNoClip(Player player, boolean enabled) {
        return enabled ? noClip.add(player) : noClip.remove(player);
    }

    public boolean toggleNoClip(Player player) {
        return !noClip.remove(player) && noClip.add(player);
    }

    public boolean isNoClip(Player player) {
        return noClip.contains(player);
    }

    public boolean setSlabPartBreaking(Player player, boolean enabled) {
        return enabled ? slabPartBreaking.add(player) : slabPartBreaking.remove(player);
    }

    public boolean toggleSlabPartBreaking(Player player) {
        return !slabPartBreaking.remove(player) && slabPartBreaking.add(player);
    }

    public boolean isSlabPartBreaking(Player player) {
        return slabPartBreaking.contains(player);
    }

    public void purge(Player player) {
        advancedFly.remove(player);
        airPlacing.remove(player);
        noClip.remove(player);
        openable.remove(player);
        slabPartBreaking.remove(player);
    }
}
