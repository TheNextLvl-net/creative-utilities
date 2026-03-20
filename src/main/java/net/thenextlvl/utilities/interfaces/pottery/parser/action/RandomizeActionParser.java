package net.thenextlvl.utilities.interfaces.pottery.parser.action;

import com.google.gson.JsonObject;
import io.papermc.paper.datacomponent.item.PotDecorations;
import net.thenextlvl.interfaces.InterfaceSession;
import net.thenextlvl.interfaces.reader.ActionParser;
import net.thenextlvl.interfaces.reader.ParserConditions;
import net.thenextlvl.interfaces.reader.ParserContext;
import net.thenextlvl.interfaces.reader.ParserException;
import net.thenextlvl.utilities.interfaces.pottery.PotteryDesignerGUI;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static net.thenextlvl.utilities.interfaces.pottery.SherdSelectorGUI.SHERDS;
import static net.thenextlvl.utilities.interfaces.pottery.parser.action.DecorateActionParser.decorate;

public final class RandomizeActionParser implements ActionParser<JsonObject> {
    private final PotteryDesignerGUI.@Nullable Side side;

    public RandomizeActionParser(final PotteryDesignerGUI.@Nullable Side side) {
        this.side = side;
    }

    @Override
    public Consumer<InterfaceSession> parse(final JsonObject object, final ParserContext context) throws ParserException {
        ParserConditions.checkState(object.isEmpty(), "randomize does not take any arguments");
        return session -> {
            if (side == null) session.setState("pot_data", getRandom());
            else session.<PotDecorations>computeState("pot_data", (s, o) -> {
                return decorate(side, o, getRandomSherd(SHERDS.size()));
            });
            session.refresh();
        };
    }

    public static PotDecorations getRandom() {
        final var max = SHERDS.size() + 5;
        return PotDecorations.potDecorations(
                getRandomSherd(max), getRandomSherd(max),
                getRandomSherd(max), getRandomSherd(max)
        );
    }

    private static @Nullable ItemType getRandomSherd(final int max) {
        final var index = ThreadLocalRandom.current().nextInt(max);
        return index >= SHERDS.size() ? null : SHERDS.get(index).asItemType();
    }
}
