package net.thenextlvl.utilities.version;

import core.paper.version.PaperHangarVersionChecker;
import core.version.SemanticVersion;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Getter
@SuppressWarnings("UnstableApiUsage")
public class PluginVersionChecker extends PaperHangarVersionChecker<SemanticVersion> {
    private final SemanticVersion versionRunning;

    public PluginVersionChecker(Plugin plugin) {
        super("CreativeUtilities");
        this.versionRunning = Objects.requireNonNull(parseVersion(plugin.getPluginMeta().getVersion()));
    }

    @Override
    public @Nullable SemanticVersion parseVersion(String version) {
        return SemanticVersion.parse(version);
    }
}
