package net.thenextlvl.utilities.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.commands.brigadier.SimpleCommand;
import net.thenextlvl.utilities.utils.Permissions;
import net.thenextlvl.utilities.utils.Settings;
import org.bukkit.entity.Player;

public final class AdvancedFlyCommand extends SimpleCommand {
    private AdvancedFlyCommand(final UtilitiesPlugin plugin) {
        super(plugin, plugin.commands().advancedFly, Permissions.ADVANCED_FLY);
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
        final var message = Settings.toggle(player, Settings.ADVANCED_FLY)
                ? "command.advanced-fly.enabled"
                : "command.advanced-fly.disabled";
        plugin.bundle().sendMessage(player, message);
        return SINGLE_SUCCESS;
    }
}
