package net.thenextlvl.utilities.command.aliases;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Axis;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DeformRotateAlias {
    private final UtilitiesPlugin plugin;

    public DeformRotateAlias(UtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        var command = Commands.literal("/derot")
                .requires(source -> source.getSender().hasPermission("worldedit.region.deform"))
                .then(Commands.argument("axis", new AxisArgumentType())
                        .then(Commands.argument("degrees", IntegerArgumentType.integer())
                                .executes(context -> {
                                    var axis = context.getArgument("axis", Axis.class);
                                    var degrees = context.getArgument("degrees", int.class);
                                    var line = "/deform rotate(%s,%s)".formatted(switch (axis) {
                                        case X -> "y,z";
                                        case Y -> "x,z";
                                        case Z -> "x,y";
                                    }, degrees * 0.0174533f);
                                    plugin.getServer().dispatchCommand(context.getSource().getSender(), line);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .executes(context -> {
                            plugin.getServer().dispatchCommand(context.getSource().getSender(), "/deform");
                            return Command.SINGLE_SUCCESS;
                        }))
                .executes(context -> {
                    plugin.getServer().dispatchCommand(context.getSource().getSender(), "/deform");
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event ->
                event.registrar().register(command)));
    }
}
