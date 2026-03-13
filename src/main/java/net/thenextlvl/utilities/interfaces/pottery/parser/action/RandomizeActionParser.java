package net.thenextlvl.utilities.interfaces.pottery.parser.action;

import com.google.gson.JsonObject;
import io.papermc.paper.datacomponent.item.PotDecorations;
import net.thenextlvl.interfaces.InterfaceSession;
import net.thenextlvl.interfaces.reader.ActionParser;
import net.thenextlvl.interfaces.reader.ParserConditions;
import net.thenextlvl.interfaces.reader.ParserContext;
import net.thenextlvl.interfaces.reader.ParserException;
import net.thenextlvl.utilities.interfaces.pottery.PotteryDesignerGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static net.thenextlvl.utilities.interfaces.pottery.parser.action.DecorateActionParser.decorate;

public final class RandomizeActionParser implements ActionParser<JsonObject> {
    private static final List<Material> sherds = List.of(
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
    private final PotteryDesignerGUI.@Nullable Side side;

    public RandomizeActionParser(final PotteryDesignerGUI.@Nullable Side side) {
        this.side = side;
    }

    @Override
    public Consumer<InterfaceSession> parse(final JsonObject object, final ParserContext context) throws ParserException {
        ParserConditions.checkState(object.isEmpty(), "randomize does not take any arguments");
        return session -> {
            if (side == null) session.state("pot_data", getRandom());
            else session.<PotDecorations>computeState("pot_data", (s, o) -> {
                return decorate(side, o, getRandomSherd(sherds.size()));
            });
            session.refresh();
        };
    }

    public static PotDecorations getRandom() {
        final var max = sherds.size() + 5;
        return PotDecorations.potDecorations(
                getRandomSherd(max), getRandomSherd(max),
                getRandomSherd(max), getRandomSherd(max)
        );
    }

    private static @Nullable ItemType getRandomSherd(final int max) {
        final var index = ThreadLocalRandom.current().nextInt(max);
        return index >= sherds.size() ? null : sherds.get(index).asItemType();
    }
}
