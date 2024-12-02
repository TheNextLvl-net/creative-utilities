package net.thenextlvl.utilities.command.aliases;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@RequiredArgsConstructor
public class ScaleAlias {
    private final UtilitiesPlugin plugin;

    public void register() {
        var command = Commands.literal("/scale")
                .requires(source -> source.getSender().hasPermission("worldedit.region.deform"))
                .then(Commands.argument("size", IntegerArgumentType.integer())
                        .executes(context -> {
                            var size = context.getArgument("size", int.class);
                            var line = "/deform x/=%s;y/=%s;z/=%s".formatted(size, size, size);
                            plugin.getServer().dispatchCommand(context.getSource().getSender(), line);
                            return Command.SINGLE_SUCCESS;
                        }))
                .executes(context -> {
                    plugin.getServer().dispatchCommand(context.getSource().getSender(), "/deform");
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event ->
                event.registrar().register(command, List.of("/cub"))));
    }
}
