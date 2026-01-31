package net.thenextlvl.utilities.commands.aliases;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.utilities.UtilitiesPlugin;

public class ScaleAlias {
    public static LiteralCommandNode<CommandSourceStack> create(UtilitiesPlugin plugin) {
        return Commands.literal("/scale")
                .requires(source -> source.getSender().hasPermission("worldedit.region.deform"))
                .then(Commands.argument("size", IntegerArgumentType.integer())
                        .executes(context -> {
                            var size = context.getArgument("size", int.class);
                            var line = "/deform x/=%s;y/=%s;z/=%s".formatted(size, size, size);
                            plugin.getServer().dispatchCommand(context.getSource().getSender(), line);
                            return Command.SINGLE_SUCCESS;
                        }))
                .executes(context -> {
                    plugin.getServer().dispatchCommand(context.getSource().getSender(), "/deform");
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
