package net.thenextlvl.utilities.commands.brigadier;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.jspecify.annotations.Nullable;

public abstract class SimpleCommand extends BrigadierCommand implements Command<CommandSourceStack> {
    protected SimpleCommand(final UtilitiesPlugin plugin, final String name, @Nullable final String permission) {
        super(plugin, name, permission);
    }
}
