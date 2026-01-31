package net.thenextlvl.utilities.commands.aliases;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.utilities.UtilitiesPlugin;

public class CuboidSelectionAlias {
    public static LiteralCommandNode<CommandSourceStack> create(UtilitiesPlugin plugin) {
        return Commands.literal("/cuboid")
                .requires(source -> source.getSender().hasPermission("worldedit.analysis.sel"))
                .executes(context -> {
                    plugin.getServer().dispatchCommand(context.getSource().getSender(), "/sel cuboid");
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
