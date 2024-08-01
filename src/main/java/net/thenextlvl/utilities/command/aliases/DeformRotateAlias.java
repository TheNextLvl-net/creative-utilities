/*
 * Builder's Utilities is a collection of a lot of tiny features that help with building.
 * Copyright (C) Arcaniax-Development
 * Copyright (C) Arcaniax team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.thenextlvl.utilities.command.aliases;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import core.paper.command.WrappedArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Axis;
import org.bukkit.Bukkit;

import java.util.Arrays;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class DeformRotateAlias {
    private final UtilitiesPlugin plugin;

    public void register() {
        var command = Commands.literal("/derot")
                .requires(source -> source.getSender().hasPermission("worldedit.region.deform"))
                .then(Commands.argument("axis", new AxisArgumentType())
                        .then(Commands.argument("degrees", IntegerArgumentType.integer(1, 359))
                                .executes(context -> {
                                    var axis = context.getArgument("axis", Axis.class);
                                    var degrees = context.getArgument("degrees", int.class);
                                    var line = "/deform rotate(%s,%s)".formatted(switch (axis) {
                                        case X -> "y,z";
                                        case Y -> "x,z";
                                        case Z -> "x,y";
                                    }, degrees * 0.0174533f);
                                    Bukkit.dispatchCommand(context.getSource().getSender(), line);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .executes(context -> {
                            Bukkit.dispatchCommand(context.getSource().getSender(), "/deform");
                            return Command.SINGLE_SUCCESS;
                        }))
                .executes(context -> {
                    Bukkit.dispatchCommand(context.getSource().getSender(), "/deform");
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event ->
                event.registrar().register(command)));
    }

    private static class AxisArgumentType extends WrappedArgumentType<String, Axis> {
        public AxisArgumentType() {
            super(StringArgumentType.word(), (reader, type) -> Axis.valueOf(type), (context, builder) -> {
                Arrays.stream(Axis.values()).map(Axis::name).forEach(builder::suggest);
                return builder.buildFuture();
            });
        }
    }
}