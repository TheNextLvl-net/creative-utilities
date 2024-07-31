package net.thenextlvl.utilities.api.controller;

import org.bukkit.entity.Player;

import java.util.Set;

/**
 * The NoClipController interface provides methods to manipulate a Player's NoClip mode.
 */
public interface NoClipController {
    /**
     * Returns an immutable set of all players who have NoClip mode enabled.
     *
     * @return a set of players with NoClip mode enabled
     */
    Set<Player> getPlayers();

    /**
     * Sets the NoClip mode for the specified player.
     *
     * @param player  the player for whom to set the NoClip mode
     * @param enabled true to enable NoClip mode, false to disable it
     * @return true if the NoClip mode was successfully set, false otherwise
     */
    boolean setNoClip(Player player, boolean enabled);

    /**
     * Toggles the NoClip mode for the specified player.
     *
     * @param player the player for whom to toggle the NoClip mode
     * @return true if the NoClip mode was enabled, false otherwise
     */
    boolean toggleNoClip(Player player);

    /**
     * Checks if the specified player has NoClip mode enabled.
     *
     * @param player the player to check
     * @return true if the player has NoClip mode enabled, false otherwise
     */
    boolean isNoClip(Player player);
}
