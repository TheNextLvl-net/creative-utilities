package net.thenextlvl.utilities.interfaces.pottery.parser.action;

import com.google.gson.JsonObject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotDecorations;
import net.thenextlvl.interfaces.InterfaceSession;
import net.thenextlvl.interfaces.reader.ActionParser;
import net.thenextlvl.interfaces.reader.ParserConditions;
import net.thenextlvl.interfaces.reader.ParserContext;
import net.thenextlvl.interfaces.reader.ParserException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public final class GetPotActionParser implements ActionParser<JsonObject> {
    @Override
    public Consumer<InterfaceSession> parse(final JsonObject element, final ParserContext context) throws ParserException {
        ParserConditions.checkState(element.isEmpty(), "Get pot data action takes no arguments");
        return session -> {
            final var item = ItemStack.of(Material.DECORATED_POT);
            session.state("pot_data", PotDecorations.class).ifPresent(data -> {
                item.setData(DataComponentTypes.POT_DECORATIONS, data);
            });
            session.player().getInventory().addItem(item);
        };
    }
}
