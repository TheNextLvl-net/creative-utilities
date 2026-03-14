package net.thenextlvl.utilities.interfaces.pottery;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.papermc.paper.datacomponent.item.PotDecorations;
import net.thenextlvl.interfaces.ActionItem;
import net.thenextlvl.interfaces.PaginatedInterface;
import net.thenextlvl.interfaces.reader.InterfaceReader;
import net.thenextlvl.utilities.UtilitiesPlugin;
import net.thenextlvl.utilities.interfaces.pottery.parser.action.DecorateActionParser;
import net.thenextlvl.utilities.interfaces.pottery.parser.action.GetPotActionParser;
import net.thenextlvl.utilities.interfaces.pottery.parser.action.OpenInterfaceActionParser;
import net.thenextlvl.utilities.interfaces.pottery.parser.action.RandomizeActionParser;
import net.thenextlvl.utilities.interfaces.pottery.parser.action.UndecorateActionParser;
import net.thenextlvl.utilities.interfaces.pottery.parser.item.UsePotDataParser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public final class SherdSelectorGUI {
    public static final List<Material> SHERDS = List.of(
            Material.ANGLER_POTTERY_SHERD,
            Material.ARCHER_POTTERY_SHERD,
            Material.ARMS_UP_POTTERY_SHERD,
            Material.BLADE_POTTERY_SHERD,
            Material.BREWER_POTTERY_SHERD,
            Material.BURN_POTTERY_SHERD,
            Material.DANGER_POTTERY_SHERD,
            Material.EXPLORER_POTTERY_SHERD,
            Material.FLOW_POTTERY_SHERD,
            Material.FRIEND_POTTERY_SHERD,
            Material.GUSTER_POTTERY_SHERD,
            Material.HEART_POTTERY_SHERD,
            Material.HEARTBREAK_POTTERY_SHERD,
            Material.HOWL_POTTERY_SHERD,
            Material.MINER_POTTERY_SHERD,
            Material.MOURNER_POTTERY_SHERD,
            Material.PLENTY_POTTERY_SHERD,
            Material.PRIZE_POTTERY_SHERD,
            Material.SCRAPE_POTTERY_SHERD,
            Material.SHEAF_POTTERY_SHERD,
            Material.SHELTER_POTTERY_SHERD,
            Material.SKULL_POTTERY_SHERD,
            Material.SNORT_POTTERY_SHERD
    );

    private static final Map<PotteryDesignerGUI.Side, PaginatedInterface<Material>> instances = Map.of(
            PotteryDesignerGUI.Side.LEFT, create(PotteryDesignerGUI.Side.LEFT),
            PotteryDesignerGUI.Side.RIGHT, create(PotteryDesignerGUI.Side.RIGHT),
            PotteryDesignerGUI.Side.FRONT, create(PotteryDesignerGUI.Side.FRONT),
            PotteryDesignerGUI.Side.BACK, create(PotteryDesignerGUI.Side.BACK)
    );

    private static PaginatedInterface<Material> create(final PotteryDesignerGUI.Side side) {
        try {
            final var plugin = JavaPlugin.getPlugin(UtilitiesPlugin.class);
            return InterfaceReader.reader()
                    .textRenderer(plugin.bundle()::component)
                    .registerActionParser("apply_decoration", JsonPrimitive.class, new DecorateActionParser(side))
                    .registerActionParser("get_pot", JsonObject.class, new GetPotActionParser())
                    .registerActionParser("open_interface", JsonPrimitive.class, new OpenInterfaceActionParser())
                    .registerActionParser("randomize", JsonObject.class, new RandomizeActionParser(side))
                    .registerActionParser("remove_decoration", JsonPrimitive.class, new UndecorateActionParser())
                    .registerDynamicItemParser("use_pot_data", JsonObject.class, new UsePotDataParser())
                    .<Material>readPaginatedResource("interfaces/pottery/sherd-selector.json")
                    .content(SHERDS)
                    .transformer(material -> new ActionItem(context -> {
                        return ItemStack.of(material);
                    }, context -> {
                        var potData = context.state("pot_data", PotDecorations.class).orElse(null);
                        final var data = DecorateActionParser.decorate(side, potData, material.asItemType());
                        context.state("pot_data", data);
                        PotteryDesignerGUI.INSTANCE.open(context.player(), context);
                    }))
                    .build();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PaginatedInterface<Material> instance(final PotteryDesignerGUI.Side side) {
        final var interface_ = instances.get(side);
        Preconditions.checkState(interface_ != null, "No interface for side %s", side);
        return interface_;
    }
}
