package net.thenextlvl.utilities.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.commands.brigadier.SimpleCommand;
import net.thenextlvl.utilities.interfaces.UtilitiesGUI;
import org.bukkit.entity.Player;

public final class UtilsCommand extends SimpleCommand {
    private UtilsCommand(final UtilitiesPlugin plugin) {
        super(plugin, "utils", "builders.util.gui");
    }

    public static LiteralCommandNode<CommandSourceStack> create(final UtilitiesPlugin plugin) {
        final var command = new UtilsCommand(plugin);
        return command.create().executes(command).build();
    }

    @Override
    protected boolean canUse(final CommandSourceStack source) {
        return super.canUse(source) && source.getSender() instanceof Player;
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        new UtilitiesGUI(plugin, player).open();
        return SINGLE_SUCCESS;
    }
}
