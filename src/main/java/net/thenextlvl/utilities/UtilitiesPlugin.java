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
package net.thenextlvl.utilities;

import core.file.format.GsonFile;
import core.i18n.file.ComponentBundle;
import core.io.IO;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.thenextlvl.utilities.command.AdvancedFlyCommand;
import net.thenextlvl.utilities.command.BannerCommand;
import net.thenextlvl.utilities.command.ColorCommand;
import net.thenextlvl.utilities.command.NightVisionCommand;
import net.thenextlvl.utilities.command.NoClipCommand;
import net.thenextlvl.utilities.command.UtilsCommand;
import net.thenextlvl.utilities.command.aliases.ConvexSelectionAlias;
import net.thenextlvl.utilities.command.aliases.CuboidSelectionAlias;
import net.thenextlvl.utilities.command.aliases.DeformRotateAlias;
import net.thenextlvl.utilities.command.aliases.ScaleAlias;
import net.thenextlvl.utilities.command.aliases.TwistAlias;
import net.thenextlvl.utilities.controller.SettingsController;
import net.thenextlvl.utilities.gui.banner.BannerColorMenuProvider;
import net.thenextlvl.utilities.gui.banner.BannerMenuProvider;
import net.thenextlvl.utilities.gui.banner.BannerPatternMenuProvider;
import net.thenextlvl.utilities.gui.inventory.InventoryManager;
import net.thenextlvl.utilities.gui.inventory.SmartInventory;
import net.thenextlvl.utilities.listener.*;
import net.thenextlvl.utilities.model.NoClipManager;
import net.thenextlvl.utilities.model.PluginConfig;
import net.thenextlvl.utilities.version.PluginVersionChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.util.Locale;

@NullMarked
@Accessors(fluent = true)
public final class UtilitiesPlugin extends JavaPlugin {
    private final File translations = new File(getDataFolder(), "translations");
    private final @Getter ComponentBundle bundle = new ComponentBundle(translations, audience ->
            audience instanceof Player player ? player.locale() : Locale.US)
            .register("messages", Locale.US)
            .register("messages_german", Locale.GERMANY)
            .miniMessage(bundle -> MiniMessage.builder().tags(TagResolver.resolver(
                    TagResolver.standard(),
                    Placeholder.component("prefix", bundle.component(Locale.US, "prefix"))
            )).build());

    private final @Getter SettingsController settingsController = new SettingsController();

    private final @Getter NoClipManager noClipManager = new NoClipManager(this);

    @Accessors(fluent = false)
    @Deprecated(forRemoval = true)
    private final @Getter InventoryManager inventoryManager = new InventoryManager();

    private final @Getter PluginConfig config = new GsonFile<>(
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
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
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

    @Deprecated(forRemoval = true)
    public static UtilitiesPlugin getInstance() {
        return JavaPlugin.getPlugin(UtilitiesPlugin.class);
    }

    @Deprecated(forRemoval = true)
    public final SmartInventory bannerMenu = SmartInventory.builder(getInventoryManager(), new BannerMenuProvider(this))
            .size(6, 9)
            .title(ChatColor.BLUE + "Select a base color")
            .closeable(true)
            .build();

    @Deprecated(forRemoval = true)
    public final SmartInventory bannerColorMenu = SmartInventory.builder(getInventoryManager(), new BannerColorMenuProvider(this))
            .size(6, 9)
            .title(ChatColor.BLUE + "Select a color")
            .closeable(true)
            .build();

    @Deprecated(forRemoval = true)
    public final SmartInventory bannerPatternMenu = SmartInventory.builder(getInventoryManager(), new BannerPatternMenuProvider(this))
            .size(6, 9)
            .title(ChatColor.BLUE + "Select a pattern")
            .closeable(true)
            .build();
}
