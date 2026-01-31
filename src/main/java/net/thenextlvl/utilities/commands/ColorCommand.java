package net.thenextlvl.utilities.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.interfaces.ArmorCreatorGUI;
import org.bukkit.entity.Player;

public class ColorCommand {
    public static LiteralCommandNode<CommandSourceStack> create(final UtilitiesPlugin plugin) {
        return Commands.literal("armorcolor")
                .requires(stack -> stack.getSender().hasPermission("builders.util.color")
                        && stack.getSender() instanceof Player)
                .executes(context -> {
                    final var player = (Player) context.getSource().getSender();
                    new ArmorCreatorGUI(plugin, player).open();
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
