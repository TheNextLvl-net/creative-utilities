package net.thenextlvl.utilities.command.aliases;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@RequiredArgsConstructor
public class ConvexSelectionAlias {
    private final UtilitiesPlugin plugin;

    public void register() {
        var command = Commands.literal("/convex")
                .requires(source -> source.getSender().hasPermission("worldedit.analysis.sel"))
                .executes(context -> {
                    plugin.getServer().dispatchCommand(context.getSource().getSender(), "/sel convex");
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event ->
                event.registrar().register(command, List.of("/con"))));
    }
}
