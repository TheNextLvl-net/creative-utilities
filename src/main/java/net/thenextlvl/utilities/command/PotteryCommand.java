package net.thenextlvl.utilities.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.gui.pottery.PotteryDesignerGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PotteryCommand {
    public static LiteralCommandNode<CommandSourceStack> create(UtilitiesPlugin plugin) {
        return Commands.literal("pottery")
                .requires(stack -> stack.getSender().hasPermission("builders.util.pottery-designer")
                                   && stack.getSender() instanceof Player)
                .executes(context -> {
                    var player = (Player) context.getSource().getSender();
                    new PotteryDesignerGUI(plugin, player, ItemStack.of(Material.DECORATED_POT)).open();
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
