package net.thenextlvl.utilities.utils;

import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public final class NoClipManager {
    public static void start(final UtilitiesPlugin plugin) {
        plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, task -> {
            Settings.getNoClip().forEach(NoClipManager::updateNoClip);
        }, 1, 1);
    }

    private static boolean checkSurrounding(final Player player) {
        return player.getLocation().add(+0.4, 0, 0).getBlock().isCollidable()
                || player.getLocation().add(-0.4, 0, 0).getBlock().isCollidable()
                || player.getLocation().add(0, 0, +0.4).getBlock().isCollidable()
                || player.getLocation().add(0, 0, -0.4).getBlock().isCollidable()
                || player.getLocation().add(+0.4, 1, 0).getBlock().isCollidable()
                || player.getLocation().add(-0.4, 1, 0).getBlock().isCollidable()
                || player.getLocation().add(0, 1, +0.4).getBlock().isCollidable()
                || player.getLocation().add(0, 1, -0.4).getBlock().isCollidable()
                || player.getLocation().add(0, +1.9, 0).getBlock().isCollidable();
    }

    private static void updateNoClip(final Player player) {
        if (!player.getGameMode().isInvulnerable()) return;

        final boolean noClip;

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            if (player.getLocation().add(0, -0.1, 0).getBlock().isCollidable() && player.isSneaking()) {
                noClip = true;
            } else {
                noClip = checkSurrounding(player);
            }

            if (noClip) player.setGameMode(GameMode.SPECTATOR);

        } else if (player.getGameMode().equals(GameMode.SPECTATOR)) {
            if (player.getLocation().add(0, -0.1, 0).getBlock().isCollidable()) {
                noClip = true;
            } else {
                noClip = checkSurrounding(player);
            }

            if (!noClip) player.setGameMode(GameMode.CREATIVE);
        }
    }
}
