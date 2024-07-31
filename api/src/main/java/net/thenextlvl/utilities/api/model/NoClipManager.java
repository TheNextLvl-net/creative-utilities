package net.thenextlvl.utilities.api.model;

import org.bukkit.entity.Player;

/**
 * The NoClipManager interface provides methods for managing the NoClip mode of players.
 */
public interface NoClipManager {
    /**
     * Checks the surrounding blocks of the player.
     *
     * @param player the player to check the surrounding blocks for
     * @return true if any of the surrounding blocks is collidable, false otherwise
     */
    boolean checkSurrounding(Player player);

    /**
     * Updates the NoClip mode for the specified player based on their game mode and surrounding blocks.
     *
     * @param player the player for whom to update the NoClip mode
     */
    void updateNoClip(Player player);
}
