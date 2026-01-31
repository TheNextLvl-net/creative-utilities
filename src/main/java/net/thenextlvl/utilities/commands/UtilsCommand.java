package net.thenextlvl.utilities.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.interfaces.UtilitiesGUI;
import org.bukkit.entity.Player;

public class UtilsCommand {
    public static LiteralCommandNode<CommandSourceStack> create(final UtilitiesPlugin plugin) {
        return Commands.literal("utils")
                .requires(stack -> stack.getSender().hasPermission("builders.util.gui")
                        && stack.getSender() instanceof Player)
                .executes(context -> {
                    final var player = (Player) context.getSource().getSender();
                    new UtilitiesGUI(plugin, player).open();
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
