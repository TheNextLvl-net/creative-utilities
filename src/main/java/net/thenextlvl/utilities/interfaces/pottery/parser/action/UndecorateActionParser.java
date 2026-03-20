package net.thenextlvl.utilities.interfaces.pottery.parser.action;

import com.google.gson.JsonPrimitive;
import io.papermc.paper.datacomponent.item.PotDecorations;
import net.thenextlvl.interfaces.InterfaceSession;
import net.thenextlvl.interfaces.reader.ActionParser;
import net.thenextlvl.interfaces.reader.ParserContext;
import net.thenextlvl.interfaces.reader.ParserException;
import net.thenextlvl.utilities.interfaces.pottery.PotteryDesignerGUI;

import java.util.function.Consumer;

public final class UndecorateActionParser implements ActionParser<JsonPrimitive> {
    @Override
    public Consumer<InterfaceSession> parse(final JsonPrimitive element, final ParserContext context) throws ParserException {
        final var side = PotteryDesignerGUI.Side.fromName(element.getAsString());
        return session -> {
            var potData = session.getState("pot_data", PotDecorations.class, null);
            final var data = DecorateActionParser.decorate(side, potData, null);
            session.setState("pot_data", data);
            session.refresh();
        };
    }
}
