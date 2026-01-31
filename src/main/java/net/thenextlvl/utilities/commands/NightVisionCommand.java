package net.thenextlvl.utilities.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.commands.brigadier.SimpleCommand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class NightVisionCommand extends SimpleCommand {
    private static final PotionEffect nightVision = new PotionEffect(
            PotionEffectType.NIGHT_VISION,
            PotionEffect.INFINITE_DURATION,
            0, true, false
    );

    private NightVisionCommand(final UtilitiesPlugin plugin) {
        super(plugin, plugin.commands().nightVision, "builders.util.night-vision");
    }

    public static LiteralCommandNode<CommandSourceStack> create(final UtilitiesPlugin plugin) {
        final var command = new NightVisionCommand(plugin);
        return command.create().executes(command).build();
    }

    @Override
    protected boolean canUse(final CommandSourceStack source) {
        return super.canUse(source) && source.getSender() instanceof Player;
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        final var message = toggleNightVision(player)
                ? "command.night-vision.enabled"
                : "command.night-vision.disabled";
        plugin.bundle().sendMessage(player, message);
        return SINGLE_SUCCESS;
    }

    private static boolean toggleNightVision(final Player player) {
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            return false;
        } else {
            player.addPotionEffect(nightVision);
            return true;
        }
    }
}
