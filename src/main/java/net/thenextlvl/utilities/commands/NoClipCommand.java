package net.thenextlvl.utilities.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class NoClipCommand {
    public static LiteralCommandNode<CommandSourceStack> create(UtilitiesPlugin plugin) {
        return Commands.literal("noclip")
                .requires(stack -> stack.getSender().hasPermission("builders.util.no-clip")
                        && stack.getSender() instanceof Player)
                .executes(context -> {
                    var player = (Player) context.getSource().getSender();
                    var message = plugin.settingsController().toggleNoClip(player)
                            ? "command.no-clip.enabled"
                            : "command.no-clip.disabled";
                    plugin.bundle().sendMessage(player, message);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

}
