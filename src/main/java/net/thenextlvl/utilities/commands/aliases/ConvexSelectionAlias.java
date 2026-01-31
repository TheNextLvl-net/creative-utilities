package net.thenextlvl.utilities.commands.aliases;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.commands.brigadier.SimpleCommand;

public final class ConvexSelectionAlias extends SimpleCommand {
    private ConvexSelectionAlias(final UtilitiesPlugin plugin) {
        super(plugin, "/convex", "worldedit.analysis.sel");
    }

    public static LiteralCommandNode<CommandSourceStack> create(final UtilitiesPlugin plugin) {
        final var command = new ConvexSelectionAlias(plugin);
        return command.create().executes(command).build();
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        plugin.getServer().dispatchCommand(context.getSource().getSender(), "/sel convex");
        return SINGLE_SUCCESS;
    }
}
