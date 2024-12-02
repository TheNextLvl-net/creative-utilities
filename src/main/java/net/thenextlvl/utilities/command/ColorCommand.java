package net.thenextlvl.utilities.command;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.gui.ArmorCreatorGUI;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@RequiredArgsConstructor
public class ColorCommand {
    private final UtilitiesPlugin plugin;

    public void register() {
        var command = Commands.literal("armorcolor")
                .requires(stack -> stack.getSender().hasPermission("builders.util.color")
                                   && stack.getSender() instanceof Player)
                .executes(context -> {
                    var player = (Player) context.getSource().getSender();
                    new ArmorCreatorGUI(plugin, player).open();
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event ->
                event.registrar().register(command, List.of("color"))));
    }
}
