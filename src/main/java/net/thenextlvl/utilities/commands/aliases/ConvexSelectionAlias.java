package net.thenextlvl.utilities.commands.aliases;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ConvexSelectionAlias {
    public static LiteralCommandNode<CommandSourceStack> create(UtilitiesPlugin plugin) {
        return Commands.literal("/convex")
                .requires(source -> source.getSender().hasPermission("worldedit.analysis.sel"))
                .executes(context -> {
                    plugin.getServer().dispatchCommand(context.getSource().getSender(), "/sel convex");
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
