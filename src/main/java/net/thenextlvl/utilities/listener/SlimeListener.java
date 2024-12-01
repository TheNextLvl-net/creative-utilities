package net.thenextlvl.utilities.listener;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class SlimeListener implements Listener {
    private final UtilitiesPlugin plugin;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerSlimePiston(PlayerInteractEvent event) {
        if (!plugin.config().pistonSliming()) return;
        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) return;
        var block = event.getClickedBlock();
        if (block == null || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!event.getMaterial().equals(Material.SLIME_BALL) || !EquipmentSlot.HAND.equals(event.getHand())) return;
        if (!(block.getBlockData() instanceof Directional face)) return;
        if (!face.getFacing().equals(event.getBlockFace())) return;
        if (block.getType().equals(Material.PISTON_HEAD)) {
            var head = (PistonHead) block.getBlockData();
            if (head.getType().equals(TechnicalPiston.Type.STICKY)) return;
            head.setType(TechnicalPiston.Type.STICKY);
            block.setBlockData(head, false);
        } else if (block.getType().equals(Material.PISTON) && block.getBlockData() instanceof Piston piston
                   && !piston.isExtended()) block.setBlockData(Material.STICKY_PISTON.createBlockData(data -> {
            if (data instanceof Directional directional) directional.setFacing(face.getFacing());
        }), true);
        else return;
        event.getPlayer().swingMainHand();
        event.getPlayer().playSound(block.getLocation(), Sound.BLOCK_SLIME_BLOCK_PLACE, 1, 1);
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if (event.getItem() != null) event.getItem().subtract();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerShearPiston(PlayerInteractEvent event) {
        if (!plugin.config().pistonSliming()) return;
        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) return;
        var block = event.getClickedBlock();
        if (block == null || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!event.getMaterial().equals(Material.SHEARS) || !EquipmentSlot.HAND.equals(event.getHand())) return;
        if (!(block.getBlockData() instanceof Directional face)) return;
        if (!face.getFacing().equals(event.getBlockFace())) return;
        if (block.getType().equals(Material.PISTON_HEAD)) {
            var head = (PistonHead) block.getBlockData();
            if (head.getType().equals(TechnicalPiston.Type.NORMAL)) return;
            head.setType(TechnicalPiston.Type.NORMAL);
            block.setBlockData(head, false);
        } else if (block.getType().equals(Material.STICKY_PISTON) && block.getBlockData() instanceof Piston piston
                   && !piston.isExtended()) block.setBlockData(Material.PISTON.createBlockData(data -> {
            if (data instanceof Directional directional) directional.setFacing(piston.getFacing());
        }), true);
        else return;
        event.getPlayer().swingMainHand();
        event.getPlayer().playSound(block.getLocation(), Sound.BLOCK_SLIME_BLOCK_BREAK, 1, 1);
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if (event.getItem() != null) event.getItem().damage(1, event.getPlayer());
    }
}
