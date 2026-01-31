package net.thenextlvl.utilities.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.commands.brigadier.SimpleCommand;
import org.bukkit.entity.Player;

public final class AdvancedFlyCommand extends SimpleCommand {
    private AdvancedFlyCommand(final UtilitiesPlugin plugin) {
        super(plugin, "advancedfly", "builders.util.advanced-fly");
    }

    public static LiteralCommandNode<CommandSourceStack> create(final UtilitiesPlugin plugin) {
        final var command = new AdvancedFlyCommand(plugin);
        return command.create().executes(command).build();
    }

    @Override
    protected boolean canUse(final CommandSourceStack source) {
        return super.canUse(source) && source.getSender() instanceof Player;
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        final var message = plugin.settingsController().toggleAdvancedFly(player)
                ? "command.advanced-fly.enabled"
                : "command.advanced-fly.disabled";
        plugin.bundle().sendMessage(player, message);
        return SINGLE_SUCCESS;
    }
}
