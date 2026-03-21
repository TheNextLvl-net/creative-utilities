package net.thenextlvl.utilities.interfaces.utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.thenextlvl.interfaces.Interface;
import net.thenextlvl.interfaces.reader.InterfaceReader;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.interfaces.utilities.action.ToggleSettingActionParser;
import net.thenextlvl.utilities.interfaces.utilities.item.FeatureLoreParser;
import net.thenextlvl.utilities.interfaces.utilities.item.StateIndicatorParser;
import net.thenextlvl.utilities.setting.Settings;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Map;

public final class Utilities {
    public static final Interface INSTANCE;

    public static final Map<String, Settings> SETTINGS = Map.of(
            "hand_openable", Settings.HAND_OPENABLE,
            "slab_part_breaking", Settings.SLAB_PART_BREAKING,
            "air_placing", Settings.AIR_PLACING,
            "no_clip", Settings.NO_CLIP,
            "night_vision", Settings.NIGHT_VISION,
            "advanced_fly", Settings.ADVANCED_FLY
    );

    static {
        try {
            final var plugin = JavaPlugin.getPlugin(UtilitiesPlugin.class);
            INSTANCE = InterfaceReader.reader()
                    .textRenderer(plugin.bundle()::component)
                    .registerActionParser("toggle_setting", JsonPrimitive.class, new ToggleSettingActionParser())
                    .registerDynamicItemParser("state_indicator", JsonObject.class, new StateIndicatorParser())
                    .registerDynamicItemParser("feature_lore", JsonObject.class, new FeatureLoreParser())
                    .readResource("interfaces/utilities.json")
                    .build();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
