package net.thenextlvl.utilities;

import core.file.format.GsonFile;
import core.i18n.file.ComponentBundle;
import core.io.IO;
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
        registerAliases();
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
        new AdvancedFlyCommand(this).register();
        new BannerCommand(this).register();
        new ColorCommand(this).register();
        new NightVisionCommand(this).register();
        new NoClipCommand(this).register();
        new PotteryCommand(this).register();
        new UtilsCommand(this).register();
    }

    private void registerAliases() {
        if (!getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit")) return;
        new ConvexSelectionAlias(this).register();
        new CuboidSelectionAlias(this).register();
        new DeformRotateAlias(this).register();
        new ScaleAlias(this).register();
        new TwistAlias(this).register();
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
