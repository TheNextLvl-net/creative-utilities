package net.thenextlvl.utilities.interfaces.pottery;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.thenextlvl.interfaces.Interface;
import net.thenextlvl.interfaces.reader.InterfaceReader;
import net.thenextlvl.interfaces.reader.ParserException;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.interfaces.pottery.action.GetPotActionParser;
import net.thenextlvl.utilities.interfaces.pottery.action.OpenSherdEditorActionParser;
import net.thenextlvl.utilities.interfaces.pottery.action.RandomizeActionParser;
import net.thenextlvl.utilities.interfaces.pottery.action.UndecorateActionParser;
import net.thenextlvl.utilities.interfaces.pottery.item.SideParser;
import net.thenextlvl.utilities.interfaces.pottery.item.UsePotDataParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class PotteryDesigner {
    public static final Interface INSTANCE;

    static {
        try {
            final var plugin = JavaPlugin.getPlugin(UtilitiesPlugin.class);
            INSTANCE = InterfaceReader.reader()
                    .textRenderer(plugin.bundle()::component)
                    .registerActionParser("remove_decoration", JsonPrimitive.class, new UndecorateActionParser())
                    .registerActionParser("get_pot", JsonObject.class, new GetPotActionParser())
                    .registerActionParser("open_sherd_editor", JsonPrimitive.class, new OpenSherdEditorActionParser())
                    .registerActionParser("randomize", JsonObject.class, new RandomizeActionParser(null))
                    .registerDynamicItemParser("side", JsonPrimitive.class, new SideParser())
                    .registerDynamicItemParser("use_pot_data", JsonObject.class, new UsePotDataParser())
                    .readResource("interfaces/pottery/pottery-designer.json")
                    .build();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public enum Side {
        LEFT, RIGHT, FRONT, BACK;

        public static Side fromName(final String name) throws ParserException {
            return switch (name) {
                case "back" -> BACK;
                case "left" -> LEFT;
                case "right" -> RIGHT;
                case "front" -> FRONT;
                default -> throw new ParserException("Invalid sherd editor side: %s", name);
            };
        }
    }
}
