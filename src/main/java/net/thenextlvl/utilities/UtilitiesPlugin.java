package net.thenextlvl.utilities;

import core.file.format.GsonFile;
import core.i18n.file.ComponentBundle;
import core.io.IO;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.thenextlvl.utilities.command.AdvancedFlyCommand;
import net.thenextlvl.utilities.command.BannerCommand;
import net.thenextlvl.utilities.command.ColorCommand;
import net.thenextlvl.utilities.command.NightVisionCommand;
import net.thenextlvl.utilities.command.NoClipCommand;
import net.thenextlvl.utilities.command.PotteryCommand;
import net.thenextlvl.utilities.command.UtilsCommand;
import net.thenextlvl.utilities.command.aliases.ConvexSelectionAlias;
import net.thenextlvl.utilities.command.aliases.CuboidSelectionAlias;
import net.thenextlvl.utilities.command.aliases.DeformRotateAlias;
import net.thenextlvl.utilities.command.aliases.ScaleAlias;
import net.thenextlvl.utilities.command.aliases.TwistAlias;
import net.thenextlvl.utilities.controller.SettingsController;
import net.thenextlvl.utilities.listener.AdvancedFlyListener;
import net.thenextlvl.utilities.listener.AirPlacingListener;
import net.thenextlvl.utilities.listener.BlockBreakListener;
import net.thenextlvl.utilities.listener.BlockPhysicsListener;
import net.thenextlvl.utilities.listener.ConnectionListener;
import net.thenextlvl.utilities.listener.OpenableListener;
import net.thenextlvl.utilities.listener.PlayerInteractListener;
import net.thenextlvl.utilities.listener.SlimeListener;
import net.thenextlvl.utilities.listener.TeleportListener;
import net.thenextlvl.utilities.listener.WorldListener;
import net.thenextlvl.utilities.model.NoClipManager;
import net.thenextlvl.utilities.model.PluginConfig;
import net.thenextlvl.utilities.version.PluginVersionChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.util.List;
import java.util.Locale;

@NullMarked
public final class UtilitiesPlugin extends JavaPlugin {
    private final File translations = new File(getDataFolder(), "translations");
    private final ComponentBundle bundle = new ComponentBundle(translations, audience ->
            audience instanceof Player player ? player.locale() : Locale.US)
            .register("messages", Locale.US)
            .register("messages_german", Locale.GERMANY)
            .miniMessage(bundle -> MiniMessage.builder().tags(TagResolver.resolver(
                    TagResolver.standard(),
                    Placeholder.component("prefix", bundle.component(Locale.US, "prefix"))
            )).build());

    private final SettingsController settingsController = new SettingsController();
    private final NoClipManager noClipManager = new NoClipManager(this);

    private final PluginConfig config = new GsonFile<>(
            IO.of(getDataFolder(), "config.json"),
            new PluginConfig(true, true, false, true, true, true, true, true, true, false, true)
    ).validate().save().getRoot();

    private final PluginVersionChecker versionChecker = new PluginVersionChecker(this);
    private final Metrics metrics = new Metrics(this, 22858);

    @Override
    public void onLoad() {
        versionChecker.checkVersion();
    }

    @Override
    public void onEnable() {
        noClipManager().start();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        metrics.shutdown();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new AdvancedFlyListener(this), this);
        getServer().getPluginManager().registerEvents(new AirPlacingListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPhysicsListener(this), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new OpenableListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new SlimeListener(this), this);
        getServer().getPluginManager().registerEvents(new TeleportListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
            event.registrar().register(AdvancedFlyCommand.create(this), List.of("advfly", "fly"));
            event.registrar().register(BannerCommand.create(this), List.of("bm"));
            event.registrar().register(ColorCommand.create(this), List.of("color"));
            event.registrar().register(NightVisionCommand.create(this), List.of("nv", "n"));
            event.registrar().register(NoClipCommand.create(this), List.of("nc"));
            event.registrar().register(PotteryCommand.create(this));
            event.registrar().register(UtilsCommand.create(this), List.of("butil", "bu"));
            registerAliases(event.registrar());
        }));
    }

    private void registerAliases(Commands commands) {
        if (!getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit")) return;
        commands.register(ConvexSelectionAlias.create(this), List.of("/con"));
        commands.register(CuboidSelectionAlias.create(this), List.of("/cub"));
        commands.register(DeformRotateAlias.create(this));
        commands.register(ScaleAlias.create(this));
        commands.register(TwistAlias.create(this));
    }

    public ComponentBundle bundle() {
        return bundle;
    }

    public SettingsController settingsController() {
        return settingsController;
    }

    public NoClipManager noClipManager() {
        return noClipManager;
    }

    public PluginConfig config() {
        return config;
    }
}
