package net.thenextlvl.utilities.api.controller;

import org.bukkit.entity.Player;

import java.util.Set;

/**
 * The FlyController interface defines methods for controlling the advanced fly mode for players.
 */
public interface FlyController {
    /**
     * Returns an immutable set of all players who have advanced fly mode enabled.
     *
     * @return a set of players with advanced fly mode enabled
     */
    Set<Player> getPlayers();

    /**
     * Sets the advanced fly mode for the specified player.
     *
     * @param player  the player for whom to set the advanced fly mode
     * @param enabled {@code true} to enable advanced fly mode, {@code false} to disable it
     * @return {@code true} if the advanced fly mode was successfully set, {@code false} otherwise
     */
    boolean setAdvancedFly(Player player, boolean enabled);

    /**
     * Toggles the advanced fly mode for the specified player.
     *
     * @param player the player for whom to toggle the advanced fly mode
     * @return {@code true} if the advanced fly mode was enabled, {@code false} otherwise
     */
    boolean toggleAdvancedFly(Player player);

    /**
     * Determines if the advanced fly mode is enabled for the specified player.
     *
     * @param player the player to check for advanced fly mode
     * @return {@code true} if the advanced fly mode is enabled for the player, {@code false} otherwise
     */
    boolean isAdvancedFly(Player player);
}
