package net.thenextlvl.utilities.command;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@RequiredArgsConstructor
public class AdvancedFlyCommand {
    private final UtilitiesPlugin plugin;

    public void register() {
        var command = Commands.literal("advancedfly")
                .requires(stack -> stack.getSender().hasPermission("builders.util.advancedfly")
                                   && stack.getSender() instanceof Player)
                .executes(context -> {
                    var player = (Player) context.getSource().getSender();
                    var message = plugin.settingsController().toggleAdvancedFly(player)
                            ? "command.advanced-fly.enabled"
                            : "command.advanced-fly.disabled";
                    plugin.bundle().sendMessage(player, message);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event ->
                event.registrar().register(command, List.of("advfly", "af"))));
    }
}
