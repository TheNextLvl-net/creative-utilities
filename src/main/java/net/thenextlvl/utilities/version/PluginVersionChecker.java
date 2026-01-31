package net.thenextlvl.utilities.version;

import net.thenextlvl.version.SemanticVersion;
import net.thenextlvl.version.modrinth.paper.PaperModrinthVersionChecker;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class PluginVersionChecker extends PaperModrinthVersionChecker<SemanticVersion> {
    public PluginVersionChecker(final Plugin plugin) {
        super(plugin, "zAcZq5oV");
    }

    @Override
    public @Nullable SemanticVersion parseVersion(final String version) {
        return SemanticVersion.parse(version);
    }
}
