package net.thenextlvl.utilities.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class AdvancedFlyCommand {
    public static LiteralCommandNode<CommandSourceStack> create(UtilitiesPlugin plugin) {
        return Commands.literal("advancedfly")
                .requires(stack -> stack.getSender().hasPermission("builders.util.advancedfly")
                                   && stack.getSender() instanceof Player)
                .executes(context -> {
                    var player = (Player) context.getSource().getSender();
                    var message = plugin.settingsController().toggleAdvancedFly(player)
                            ? "command.advanced-fly.enabled"
                            : "command.advanced-fly.disabled";
                    plugin.bundle().sendMessage(player, message);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
