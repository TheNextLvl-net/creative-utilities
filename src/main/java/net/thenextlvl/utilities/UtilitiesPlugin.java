package net.thenextlvl.utilities;

import core.file.formats.GsonFile;
import dev.faststats.bukkit.BukkitMetrics;
import dev.faststats.core.ErrorTracker;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.Key;
import net.thenextlvl.i18n.ComponentBundle;
import net.thenextlvl.utilities.commands.AdvancedFlyCommand;
import net.thenextlvl.utilities.commands.AirPlaceCommand;
import net.thenextlvl.utilities.commands.BannerCommand;
import net.thenextlvl.utilities.commands.ColorCommand;
import net.thenextlvl.utilities.commands.NightVisionCommand;
import net.thenextlvl.utilities.commands.NoClipCommand;
import net.thenextlvl.utilities.commands.PotteryCommand;
import net.thenextlvl.utilities.commands.UtilsCommand;
import net.thenextlvl.utilities.commands.aliases.ConvexSelectionAlias;
import net.thenextlvl.utilities.commands.aliases.CuboidSelectionAlias;
import net.thenextlvl.utilities.commands.aliases.DeformRotateAlias;
import net.thenextlvl.utilities.commands.aliases.ScaleAlias;
import net.thenextlvl.utilities.commands.aliases.TwistAlias;
import net.thenextlvl.utilities.listeners.AdvancedFlyListener;
import net.thenextlvl.utilities.listeners.AirPlacingListener;
import net.thenextlvl.utilities.listeners.BlockBreakListener;
import net.thenextlvl.utilities.listeners.BlockPhysicsListener;
import net.thenextlvl.utilities.listeners.ConnectionListener;
import net.thenextlvl.utilities.listeners.OpenableListener;
import net.thenextlvl.utilities.listeners.PlayerInteractListener;
import net.thenextlvl.utilities.listeners.SlimeListener;
import net.thenextlvl.utilities.listeners.TeleportListener;
import net.thenextlvl.utilities.listeners.WorldListener;
import net.thenextlvl.utilities.utils.Commands;
import net.thenextlvl.utilities.utils.NoClipManager;
import net.thenextlvl.utilities.utils.PluginConfig;
import net.thenextlvl.utilities.version.PluginVersionChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.Locale;

public final class UtilitiesPlugin extends JavaPlugin {
    public static final ErrorTracker ERROR_TRACKER = ErrorTracker.contextAware();

    private final Key key = Key.key("utilities", "translations");
    private final Path translations = getDataPath().resolve("translations");
    private final ComponentBundle bundle = ComponentBundle.builder(key, translations)
            .placeholder("prefix", "prefix")
            .resource("messages.properties", Locale.US)
            .resource("messages_german.properties", Locale.GERMANY)
            .build();

    private final PluginConfig config = new GsonFile<>(
            getDataPath().resolve("config.json"),
            new PluginConfig(true, true, false, true, true, true, true, true, true, false, true)
    ).validate().save().getRoot();

    private final Commands commands = new GsonFile<>(
            getDataPath().resolve("commands.json"),
            new Commands()
    ).validate().save().getRoot();

    private final PluginVersionChecker versionChecker = new PluginVersionChecker(this);

    private final BukkitMetrics fastStats = BukkitMetrics.factory()
            .token("524c53930762784671a459e9f3b45aa4")
            .errorTracker(ERROR_TRACKER)
            .create(this);
    private final Metrics metrics = new Metrics(this, 22858);

    @Override
    public void onLoad() {
        versionChecker.checkVersion();
    }

    @Override
    public void onEnable() {
        fastStats.ready();
        NoClipManager.start(this);
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        fastStats.shutdown();
        metrics.shutdown();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new AdvancedFlyListener(), this);
        getServer().getPluginManager().registerEvents(new AirPlacingListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPhysicsListener(this), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new OpenableListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new SlimeListener(this), this);
        getServer().getPluginManager().registerEvents(new TeleportListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
            event.registrar().register(AdvancedFlyCommand.create(this), commands.advancedFly.aliases());
            event.registrar().register(AirPlaceCommand.create(this), commands.airPlace.aliases());
            event.registrar().register(BannerCommand.create(this), commands.banner.aliases());
            event.registrar().register(ColorCommand.create(this), commands.color.aliases());
            event.registrar().register(NightVisionCommand.create(this), commands.nightVision.aliases());
            event.registrar().register(NoClipCommand.create(this), commands.noClip.aliases());
            event.registrar().register(PotteryCommand.create(this), commands.pottery.aliases());
            event.registrar().register(UtilsCommand.create(this), commands.utils.aliases());
            registerAliases(event.registrar());
        }));
    }

    private void registerAliases(final io.papermc.paper.command.brigadier.Commands registrar) {
        if (getServer().getPluginManager().getPlugin("FastAsyncWorldEdit") == null) return;
        registrar.register(ConvexSelectionAlias.create(this), commands.convex.aliases());
        registrar.register(CuboidSelectionAlias.create(this), commands.cuboid.aliases());
        registrar.register(DeformRotateAlias.create(this), commands.deformRotate.aliases());
        registrar.register(ScaleAlias.create(this), commands.scale.aliases());
        registrar.register(TwistAlias.create(this), commands.twist.aliases());
    }

    public ComponentBundle bundle() {
        return bundle;
    }

    public PluginConfig config() {
        return config;
    }
    
    public Commands commands() {
        return commands;
    }
}
