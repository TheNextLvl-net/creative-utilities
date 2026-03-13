package net.thenextlvl.utilities.interfaces.pottery;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.thenextlvl.interfaces.Interface;
import net.thenextlvl.interfaces.reader.InterfaceReader;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.interfaces.pottery.parser.action.DecorateActionParser;
import net.thenextlvl.utilities.interfaces.pottery.parser.action.GetPotActionParser;
import net.thenextlvl.utilities.interfaces.pottery.parser.action.OpenInterfaceActionParser;
import net.thenextlvl.utilities.interfaces.pottery.parser.action.RandomizeActionParser;
import net.thenextlvl.utilities.interfaces.pottery.parser.item.UsePotDataParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Map;

public final class SherdSelectorGUI {
    private static final Map<PotteryDesignerGUI.Side, Interface> instances = Map.of(
            PotteryDesignerGUI.Side.LEFT, create(PotteryDesignerGUI.Side.LEFT),
            PotteryDesignerGUI.Side.RIGHT, create(PotteryDesignerGUI.Side.RIGHT),
            PotteryDesignerGUI.Side.FRONT, create(PotteryDesignerGUI.Side.FRONT),
            PotteryDesignerGUI.Side.BACK, create(PotteryDesignerGUI.Side.BACK)
    );

    private static Interface create(final PotteryDesignerGUI.Side side) {
        try {
            final var plugin = JavaPlugin.getPlugin(UtilitiesPlugin.class);
            return InterfaceReader.reader()
                    .textRenderer(plugin.bundle()::component)
                    .registerActionParser("apply_decoration", JsonPrimitive.class, new DecorateActionParser(side))
                    .registerActionParser("get_pot", JsonObject.class, new GetPotActionParser())
                    .registerActionParser("open_interface", JsonPrimitive.class, new OpenInterfaceActionParser())
                    .registerActionParser("randomize", JsonObject.class, new RandomizeActionParser(side))
                    .registerDynamicItemParser("use_pot_data", JsonObject.class, new UsePotDataParser())
                    .readResource("interfaces/pottery/sherd-selector.json");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Interface instance(final PotteryDesignerGUI.Side side) {
        final var interface_ = instances.get(side);
        Preconditions.checkState(interface_ != null, "No interface for side %s", side);
        return interface_;
    }
}
