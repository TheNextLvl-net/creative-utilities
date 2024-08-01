package net.thenextlvl.utilities.command.aliases;

import com.mojang.brigadier.arguments.StringArgumentType;
import core.paper.command.WrappedArgumentType;
import org.bukkit.Axis;

import java.util.Arrays;

class AxisArgumentType extends WrappedArgumentType<String, Axis> {
    AxisArgumentType() {
        super(StringArgumentType.word(), (reader, type) -> Axis.valueOf(type.toUpperCase()), (context, builder) -> {
            Arrays.stream(Axis.values()).map(Axis::name).forEach(builder::suggest);
            return builder.buildFuture();
        });
    }
}
