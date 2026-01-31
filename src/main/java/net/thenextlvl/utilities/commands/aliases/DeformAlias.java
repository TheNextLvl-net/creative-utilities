package net.thenextlvl.utilities.commands.aliases;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import core.paper.brigadier.arguments.EnumArgumentType;
import core.paper.brigadier.arguments.codecs.EnumStringCodec;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.commands.brigadier.SimpleCommand;
import org.bukkit.Axis;

abstract class DeformAlias extends SimpleCommand {
    protected DeformAlias(final UtilitiesPlugin plugin, final String name, final String permission) {
        super(plugin, name, permission);
    }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> create() {
        final var axis = Commands.argument("axis", EnumArgumentType.of(Axis.class, EnumStringCodec.lowerHyphen()));
        final var degrees = Commands.argument("degrees", IntegerArgumentType.integer());
        return super.create().then(axis
                        .then(degrees.executes(this))
                        .executes(this::syntaxError))
                .executes(this::syntaxError);
    }

    protected abstract String getCommand(final Axis axis, final int degrees);

    private int syntaxError(final CommandContext<CommandSourceStack> context) {
        plugin.getServer().dispatchCommand(context.getSource().getSender(), "/deform");
        return SINGLE_SUCCESS;
    }
    
    @Override
    public final int run(final CommandContext<CommandSourceStack> context) {
        final var axis = context.getArgument("axis", Axis.class);
        final var degrees = context.getArgument("degrees", int.class);
        plugin.getServer().dispatchCommand(context.getSource().getSender(), getCommand(axis, degrees));
        return SINGLE_SUCCESS;
    }
}
