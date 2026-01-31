package net.thenextlvl.utilities.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.commands.brigadier.SimpleCommand;
import net.thenextlvl.utilities.interfaces.pottery.PotteryDesignerGUI;
import net.thenextlvl.utilities.model.Commands;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class PotteryCommand extends SimpleCommand {
    private PotteryCommand(final UtilitiesPlugin plugin) {
        super(plugin, Commands.INSTANCE.pottery, "builders.util.pottery-designer");
    }

    public static LiteralCommandNode<CommandSourceStack> create(final UtilitiesPlugin plugin) {
        final var command = new PotteryCommand(plugin);
        return command.create().executes(command).build();
    }

    @Override
    protected boolean canUse(final CommandSourceStack source) {
        return super.canUse(source) && source.getSender() instanceof Player;
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        new PotteryDesignerGUI(plugin, player, ItemStack.of(Material.DECORATED_POT)).open();
        return SINGLE_SUCCESS;
    }
}
