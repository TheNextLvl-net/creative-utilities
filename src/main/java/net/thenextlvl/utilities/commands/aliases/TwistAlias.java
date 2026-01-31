package net.thenextlvl.utilities.commands.aliases;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.Axis;

public final class TwistAlias extends DeformAlias {
    private TwistAlias(final UtilitiesPlugin plugin) {
        super(plugin, plugin.commands().twist, "worldedit.region.deform");
    }

    public static LiteralCommandNode<CommandSourceStack> create(final UtilitiesPlugin plugin) {
        return new TwistAlias(plugin).create().build();
    }

    @Override
    protected String getCommand(final Axis axis, final int degrees) {
        return "/deform rotate(%s,%s*(%s+1))".formatted(switch (axis) {
            case X -> "y,z";
            case Y -> "x,z";
            case Z -> "x,y";
        }, degrees * 0.0174533f / 2, axis.name().toLowerCase());
    }
}
