package net.thenextlvl.utilities.api.controller;

import org.bukkit.entity.Player;

import java.util.Set;

/**
 * The OpenableController interface manages the ability of players to open iron doors and iron trapdoors with their hand.
 */
public interface OpenableController {
    /**
     * Returns an immutable set of all players that can open iron doors and iron trapdoors with their hand.
     *
     * @return a set of players with hand opening enabled
     */
    Set<Player> getPlayers();

    /**
     * Sets whether a player can open iron doors or iron trapdoors with their hand.
     *
     * @param player  the player whose ability to open doors or trapdoors with hand is to be set
     * @param enabled  true if the player should be able to open doors or trapdoors with hand, false otherwise
     * @return true if the ability to open doors or trapdoors with hand was successfully set, false otherwise
     */
    boolean setHandOpenable(Player player, boolean enabled);

    /**
     * Toggles whether a player can open iron doors or iron trapdoors with their hand.
     *
     * @param player the player whose ability to open doors or trapdoors with hand is to be toggled
     * @return the new state of the ability to open doors or trapdoors with hand
     */
    boolean toggleHandOpenable(Player player);

    /**
     * Checks whether a player can open iron doors or iron trapdoors with their hand.
     *
     * @param player the player to check
     * @return true if the player can open iron doors or iron trapdoors with their hand, false otherwise
     */
    boolean isHandOpenable(Player player);
}
