package net.thenextlvl.utilities.command.aliases;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import core.paper.command.argument.EnumArgumentType;
import core.paper.command.argument.codec.EnumStringCodec;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Axis;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TwistAlias {
    public static LiteralCommandNode<CommandSourceStack> create(UtilitiesPlugin plugin) {
        return Commands.literal("/twist")
                .requires(source -> source.getSender().hasPermission("worldedit.region.deform"))
                .then(Commands.argument("axis", EnumArgumentType.of(Axis.class, EnumStringCodec.lowerHyphen()))
                        .then(Commands.argument("degrees", IntegerArgumentType.integer())
                                .executes(context -> {
                                    var axis = context.getArgument("axis", Axis.class);
                                    var degrees = context.getArgument("degrees", int.class);
                                    var line = "/deform rotate(%s,%s*(%s+1))".formatted(switch (axis) {
                                        case X -> "y,z";
                                        case Y -> "x,z";
                                        case Z -> "x,y";
                                    }, degrees * 0.0174533f / 2, axis.name().toLowerCase());
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
    }

}
