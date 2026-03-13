package net.thenextlvl.utilities.interfaces.pottery.parser.action;

import com.google.gson.JsonPrimitive;
import net.thenextlvl.interfaces.InterfaceSession;
import net.thenextlvl.interfaces.reader.ActionParser;
import net.thenextlvl.interfaces.reader.ParserContext;
import net.thenextlvl.interfaces.reader.ParserException;
import net.thenextlvl.utilities.interfaces.pottery.PotteryDesignerGUI;
import net.thenextlvl.utilities.interfaces.pottery.SherdSelectorGUI;

import java.util.function.Consumer;

public final class OpenSherdEditorActionParser implements ActionParser<JsonPrimitive> {
    @Override
    public Consumer<InterfaceSession> parse(final JsonPrimitive element, final ParserContext context) throws ParserException {
        final var interface_ = SherdSelectorGUI.instance(PotteryDesignerGUI.Side.fromName(element.getAsString()));
        return session -> interface_.open(session.player(), session);
    }
}
