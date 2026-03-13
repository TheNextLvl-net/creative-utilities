package net.thenextlvl.utilities.interfaces.pottery.parser.action;

import com.google.gson.JsonPrimitive;
import net.thenextlvl.interfaces.Interface;
import net.thenextlvl.interfaces.InterfaceSession;
import net.thenextlvl.interfaces.reader.ActionParser;
import net.thenextlvl.interfaces.reader.ParserContext;
import net.thenextlvl.interfaces.reader.ParserException;
import net.thenextlvl.utilities.interfaces.pottery.PotteryDesignerGUI;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class OpenInterfaceActionParser implements ActionParser<JsonPrimitive> {
    private final Map<String, Supplier<Interface>> interfaces = Map.ofEntries(
            Map.entry("pottery-designer", () -> PotteryDesignerGUI.INSTANCE)
    );

    @Override
    public Consumer<InterfaceSession> parse(final JsonPrimitive element, final ParserContext context) throws ParserException {
        final var supplier = interfaces.get(element.getAsString());
        if (supplier == null) throw new ParserException("Invalid interface: %s", element.getAsString());
        return session -> supplier.get().open(session.player(), session);
    }
}
