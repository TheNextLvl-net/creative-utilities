package net.thenextlvl.utilities.commands.aliases;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.commands.brigadier.SimpleCommand;

public final class ScaleAlias extends SimpleCommand {
    private ScaleAlias(final UtilitiesPlugin plugin) {
        super(plugin, "/scale", "worldedit.region.deform");
    }

    public static LiteralCommandNode<CommandSourceStack> create(final UtilitiesPlugin plugin) {
        final var command = new ScaleAlias(plugin);
        final var size = Commands.argument("size", IntegerArgumentType.integer());
        return command.create().then(size.executes(command))
                .executes(command::syntaxError)
                .build();
    }

    private int syntaxError(final CommandContext<CommandSourceStack> context) {
        plugin.getServer().dispatchCommand(context.getSource().getSender(), "/deform");
        return SINGLE_SUCCESS;
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var size = context.getArgument("size", int.class);
        final var line = "/deform x/=%s;y/=%s;z/=%s".formatted(size, size, size);
        plugin.getServer().dispatchCommand(context.getSource().getSender(), line);
        return SINGLE_SUCCESS;
    }
}
