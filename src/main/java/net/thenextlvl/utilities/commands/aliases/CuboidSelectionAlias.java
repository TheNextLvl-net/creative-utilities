package net.thenextlvl.utilities.commands.aliases;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.commands.brigadier.SimpleCommand;
import net.thenextlvl.utilities.model.Commands;

public final class CuboidSelectionAlias extends SimpleCommand {
    private CuboidSelectionAlias(final UtilitiesPlugin plugin) {
        super(plugin, Commands.INSTANCE.cuboid, "worldedit.analysis.sel");
    }

    public static LiteralCommandNode<CommandSourceStack> create(final UtilitiesPlugin plugin) {
        final var command = new CuboidSelectionAlias(plugin);
        return command.create().executes(command).build();
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        plugin.getServer().dispatchCommand(context.getSource().getSender(), "/sel cuboid");
        return SINGLE_SUCCESS;
    }
}
