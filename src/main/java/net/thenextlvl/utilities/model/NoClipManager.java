package net.thenextlvl.utilities.model;

import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class NoClipManager {
    private final UtilitiesPlugin plugin;

    public NoClipManager(UtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, task -> plugin.settingsController()
                .getNoClip().forEach(this::updateNoClip), 1, 1);
    }

    public boolean checkSurrounding(Player player) {
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

    public void updateNoClip(Player player) {
        if (!player.getGameMode().isInvulnerable()) return;

        boolean noClip;

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
