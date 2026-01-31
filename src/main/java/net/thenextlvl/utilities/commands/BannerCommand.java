package net.thenextlvl.utilities.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.interfaces.banner.BannerGUI;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BannerCommand {
    public static LiteralCommandNode<CommandSourceStack> create(UtilitiesPlugin plugin) {
        return Commands.literal("banner")
                .requires(stack -> stack.getSender().hasPermission("builders.util.banner")
                        && stack.getSender() instanceof Player)
                .executes(context -> {
                    var player = (Player) context.getSource().getSender();
                    new BannerGUI(plugin, player).open();
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
