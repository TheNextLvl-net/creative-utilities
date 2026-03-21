package net.thenextlvl.utilities.interfaces.utilities.action;

import com.google.gson.JsonPrimitive;
import net.thenextlvl.interfaces.InterfaceSession;
import net.thenextlvl.interfaces.reader.ActionParser;
import net.thenextlvl.interfaces.reader.ParserContext;
import net.thenextlvl.interfaces.reader.ParserException;
import net.thenextlvl.utilities.interfaces.utilities.Utilities;

import java.util.function.Consumer;

public final class ToggleSettingActionParser implements ActionParser<JsonPrimitive> {
    @Override
    public Consumer<InterfaceSession> parse(final JsonPrimitive element, final ParserContext context) throws ParserException {
        final var key = element.getAsString();
        final var setting = Utilities.SETTINGS.get(key);
        if (setting == null) throw new ParserException("Invalid setting: %s", key);
        return session -> {
            setting.toggle(session.player());
            session.refresh();
        };
    }
}
