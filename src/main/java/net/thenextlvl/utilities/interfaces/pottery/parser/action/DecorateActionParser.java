package net.thenextlvl.utilities.interfaces.pottery.parser.action;

import com.google.gson.JsonPrimitive;
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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public final class DecorateActionParser implements ActionParser<JsonPrimitive> {
    private static final Map<String, @Nullable Material> sherds = new LinkedHashMap<>();

    static {
        sherds.put("angler", Material.ANGLER_POTTERY_SHERD);
        sherds.put("archer", Material.ARCHER_POTTERY_SHERD);
        sherds.put("arms_up", Material.ARMS_UP_POTTERY_SHERD);
        sherds.put("blade", Material.BLADE_POTTERY_SHERD);
        sherds.put("brewer", Material.BREWER_POTTERY_SHERD);
        sherds.put("burn", Material.BURN_POTTERY_SHERD);
        sherds.put("danger", Material.DANGER_POTTERY_SHERD);
        sherds.put("explorer", Material.EXPLORER_POTTERY_SHERD);
        sherds.put("flow", Material.FLOW_POTTERY_SHERD);
        sherds.put("friend", Material.FRIEND_POTTERY_SHERD);
        sherds.put("guster", Material.GUSTER_POTTERY_SHERD);
        sherds.put("heart", Material.HEART_POTTERY_SHERD);
        sherds.put("heartbreak", Material.HEARTBREAK_POTTERY_SHERD);
        sherds.put("howl", Material.HOWL_POTTERY_SHERD);
        sherds.put("miner", Material.MINER_POTTERY_SHERD);
        sherds.put("mourner", Material.MOURNER_POTTERY_SHERD);
        sherds.put("plenty", Material.PLENTY_POTTERY_SHERD);
        sherds.put("prize", Material.PRIZE_POTTERY_SHERD);
        sherds.put("scrape", Material.SCRAPE_POTTERY_SHERD);
        sherds.put("sheaf", Material.SHEAF_POTTERY_SHERD);
        sherds.put("shelter", Material.SHELTER_POTTERY_SHERD);
        sherds.put("skull", Material.SKULL_POTTERY_SHERD);
        sherds.put("snort", Material.SNORT_POTTERY_SHERD);
        sherds.put("none", null);
    }

    private final PotteryDesignerGUI.Side side;

    public DecorateActionParser(final PotteryDesignerGUI.Side side) {
        this.side = side;
    }

    @Override
    public Consumer<InterfaceSession> parse(final JsonPrimitive element, final ParserContext context) throws ParserException {
        ParserConditions.checkState(sherds.containsKey(element.getAsString()), "Invalid sherd: %s", element.getAsString());
        final var sherd = Optional.ofNullable(sherds.get(element.getAsString())).map(Material::asItemType).orElse(null);
        return session -> {
            final var data = decorate(side, session.state("pot_data", PotDecorations.class).orElse(null), sherd);
            session.state("pot_data", data);
            PotteryDesignerGUI.INSTANCE.open(session.player(), session);
        };
    }

    static PotDecorations decorate(final PotteryDesignerGUI.Side side, final @Nullable PotDecorations data, @Nullable final ItemType sherd) {
        final var decoration = PotDecorations.potDecorations();
        if (data != null) {
            decoration.back(data.back());
            decoration.left(data.left());
            decoration.right(data.right());
            decoration.front(data.front());
        }
        switch (side) {
            case BACK -> decoration.back(sherd);
            case FRONT -> decoration.front(sherd);
            case LEFT -> decoration.left(sherd);
            case RIGHT -> decoration.right(sherd);
        }
        return decoration.build();
    }
}
