package net.thenextlvl.utilities.command;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@RequiredArgsConstructor
public class NightVisionCommand {
    private final PotionEffect nightVision = new PotionEffect(
            PotionEffectType.NIGHT_VISION,
            PotionEffect.INFINITE_DURATION,
            0, true, false
    );
    private final UtilitiesPlugin plugin;

    public void register() {
        var command = Commands.literal("nightvision")
                .requires(stack -> stack.getSender().hasPermission("builders.util.nightvision")
                                   && stack.getSender() instanceof Player)
                .executes(context -> {
                    var player = (Player) context.getSource().getSender();
                    var message = toggleNightVision(player)
                            ? "command.night-vision.enabled"
                            : "command.night-vision.disabled";
                    plugin.bundle().sendMessage(player, message);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event ->
                event.registrar().register(command, List.of("nv", "n"))));
    }

    private boolean toggleNightVision(Player player) {
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            return false;
        } else {
            player.addPotionEffect(nightVision);
            return true;
        }
    }
}
