package net.thenextlvl.utilities.interfaces.pottery.action;

import com.google.gson.JsonPrimitive;
import net.thenextlvl.interfaces.InterfaceSession;
import net.thenextlvl.interfaces.reader.ActionParser;
import net.thenextlvl.interfaces.reader.ParserContext;
import net.thenextlvl.interfaces.reader.ParserException;
import net.thenextlvl.utilities.interfaces.pottery.PotteryDesigner;
import net.thenextlvl.utilities.interfaces.pottery.SherdSelector;

import java.util.function.Consumer;

public final class OpenSherdEditorActionParser implements ActionParser<JsonPrimitive> {
    @Override
    public Consumer<InterfaceSession> parse(final JsonPrimitive element, final ParserContext context) throws ParserException {
        final var interface_ = SherdSelector.instance(PotteryDesigner.Side.fromName(element.getAsString()));
        return session -> interface_.open(session.player(), session);
    }
}
