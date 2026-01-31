package net.thenextlvl.utilities.commands.brigadier;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.utils.Commands;
import org.jspecify.annotations.Nullable;

public abstract class SimpleCommand extends BrigadierCommand implements Command<CommandSourceStack> {
    protected SimpleCommand(final UtilitiesPlugin plugin, final Commands.Command command, @Nullable final String permission) {
        super(plugin, command, permission);
    }
}
