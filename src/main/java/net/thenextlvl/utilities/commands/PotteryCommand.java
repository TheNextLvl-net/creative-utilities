package net.thenextlvl.utilities.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.commands.brigadier.SimpleCommand;
import net.thenextlvl.utilities.interfaces.pottery.PotteryDesigner;
import net.thenextlvl.utilities.interfaces.pottery.action.RandomizeActionParser;
import net.thenextlvl.utilities.utils.Permissions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class PotteryCommand extends SimpleCommand {
    private PotteryCommand(final UtilitiesPlugin plugin) {
        super(plugin, plugin.commands().pottery, Permissions.POTTERY);
    }

    public static LiteralCommandNode<CommandSourceStack> create(final UtilitiesPlugin plugin) {
        final var command = new PotteryCommand(plugin);
        return command.create()
                .then(Commands.literal("random").executes(command::random))
                .executes(command).build();
    }

    @Override
    protected boolean canUse(final CommandSourceStack source) {
        return super.canUse(source) && source.getSender() instanceof Player;
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        PotteryDesigner.INSTANCE.open(player);
        return SINGLE_SUCCESS;
    }

    private int random(final CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        final var item = ItemStack.of(Material.DECORATED_POT);
        item.setData(DataComponentTypes.POT_DECORATIONS, RandomizeActionParser.getRandom());
        player.getInventory().addItem(item);
        return SINGLE_SUCCESS;
    }
}
