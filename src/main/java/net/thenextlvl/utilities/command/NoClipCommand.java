package net.thenextlvl.utilities.command;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.thenextlvl.utilities.UtilitiesPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class NoClipCommand {
    private final UtilitiesPlugin plugin;

    public NoClipCommand(UtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        var command = Commands.literal("noclip")
                .requires(stack -> stack.getSender().hasPermission("additions.command.no-clip")
                                   && stack.getSender() instanceof Player)
                .executes(context -> {
                    var player = (Player) context.getSource().getSender();
                    var message = plugin.settingsController().toggleNoClip(player)
                            ? "command.no-clip.enabled"
                            : "command.no-clip.disabled";
                    plugin.bundle().sendMessage(player, message);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event ->
                event.registrar().register(command, List.of("nc"))));
    }

}
