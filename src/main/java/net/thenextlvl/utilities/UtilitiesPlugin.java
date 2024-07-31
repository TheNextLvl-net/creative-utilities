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
import net.thenextlvl.utilities.command.*;
import net.thenextlvl.utilities.command.UtilsCommand;
import net.thenextlvl.utilities.command.aliases.ConvexSelectionAliasCommand;
import net.thenextlvl.utilities.controller.SettingsController;
import net.thenextlvl.utilities.gui.ColorMenuProvider;
import net.thenextlvl.utilities.gui.UtilitiesMenuProvider;
import net.thenextlvl.utilities.gui.banner.BannerColorMenuProvider;
import net.thenextlvl.utilities.gui.banner.BannerMenuProvider;
import net.thenextlvl.utilities.gui.banner.BannerPatternMenuProvider;
import net.thenextlvl.utilities.gui.inventory.InventoryListener;
import net.thenextlvl.utilities.gui.inventory.InventoryManager;
import net.thenextlvl.utilities.gui.inventory.SmartInventory;
import net.thenextlvl.utilities.listener.*;
import net.thenextlvl.utilities.model.NoClipManager;
import net.thenextlvl.utilities.model.PluginConfig;
import net.thenextlvl.utilities.version.PluginVersionChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

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
    private final @Getter InventoryManager inventoryManager = new InventoryManager(this);

    private final @Getter PluginConfig config = new GsonFile<>(
            IO.of(getDataFolder(), "config.json"),
            new PluginConfig(true, true, false, true, true, true, true, true, true, false, true)
    ).validate().save().getRoot();

    private final PluginVersionChecker versionChecker = new PluginVersionChecker(this);
    private final Metrics metrics = new Metrics(this, 5168);

    @Override
    public void onLoad() {
        versionChecker.retrieveLatestSupportedVersion(latest -> latest.ifPresentOrElse(version -> {
            if (version.equals(versionChecker.getVersionRunning())) {
                getComponentLogger().info("You are running the latest version of CreativeUtilities");
            } else if (version.compareTo(Objects.requireNonNull(versionChecker.getVersionRunning())) > 0) {
                getComponentLogger().warn("An update for CreativeUtilities is available");
                getComponentLogger().warn("You are running version {}, the latest supported version is {}", versionChecker.getVersionRunning(), version);
                getComponentLogger().warn("Update at https://hangar.papermc.io/TheNextLvl/CreativeUtilities");
            } else {
                getComponentLogger().warn("You are running a snapshot version of CreativeUtilities");
            }
        }, () -> getComponentLogger().error("Version check failed")));
    }

    @Override
    public void onEnable() {
        getInventoryManager().init();
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
        new UtilsCommand(this).register();

        if (!Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit")) return;

        new ConvexSelectionAliasCommand(this).register();
    }

    @Deprecated(forRemoval = true)
    public static UtilitiesPlugin getInstance() {
        return JavaPlugin.getPlugin(UtilitiesPlugin.class);
    }

    private final InventoryListener<InventoryCloseEvent> removeGhostItemsListener =
            new InventoryListener<>(InventoryCloseEvent.class, inventoryCloseEvent -> {
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    ((Player) inventoryCloseEvent.getPlayer()).updateInventory();
                }, 1L);
            });

    @Deprecated(forRemoval = true)
    public final SmartInventory bannerMenu = SmartInventory.builder()
            .manager(getInventoryManager())
            .id("buildersutilsbanner")
            .provider(new BannerMenuProvider(this))
            .size(6, 9)
            .listener(removeGhostItemsListener)
            .title(ChatColor.BLUE + "Select a base color")
            .closeable(true)
            .build();

    @Deprecated(forRemoval = true)
    public final SmartInventory bannerColorMenu = SmartInventory.builder()
            .manager(getInventoryManager())
            .id("buildersutilsbannercolor")
            .provider(new BannerColorMenuProvider(this))
            .size(6, 9)
            .listener(removeGhostItemsListener)
            .title(ChatColor.BLUE + "Select a color")
            .closeable(true)
            .build();

    @Deprecated(forRemoval = true)
    public final SmartInventory bannerPatternMenu = SmartInventory.builder()
            .manager(getInventoryManager())
            .id("buildersutilsbannerpattern")
            .provider(new BannerPatternMenuProvider(this))
            .size(6, 9)
            .listener(removeGhostItemsListener)
            .title(ChatColor.BLUE + "Select a pattern")
            .closeable(true)
            .build();

    @Deprecated(forRemoval = true)
    public final SmartInventory colorMenu = SmartInventory.builder()
            .manager(getInventoryManager())
            .id("buildersutilscolor")
            .provider(new ColorMenuProvider())
            .size(6, 9)
            .listener(removeGhostItemsListener)
            .title(ChatColor.BLUE + "Armor Color Creator")
            .closeable(true)
            .build();

    @Deprecated(forRemoval = true)
    public final SmartInventory toggleMenu = SmartInventory.builder()
            .manager(getInventoryManager())
            .id("buildersutilstoggle")
            .provider(new UtilitiesMenuProvider(this))
            .size(3, 9)
            .listener(removeGhostItemsListener)
            .title(ChatColor.BLUE + "Builder's Utilities")
            .closeable(true)
            .build();
}
